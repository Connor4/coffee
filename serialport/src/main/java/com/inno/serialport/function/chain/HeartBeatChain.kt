package com.inno.serialport.function.chain

import com.inno.serialport.utilities.HEARTBEAT_COMMAND_ID
import com.inno.serialport.utilities.HeartBeatReply
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import com.inno.serialport.utilities.statusenum.CleanMachineEnum
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import com.inno.serialport.utilities.statusenum.GrinderStatusEnum
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

class HeartBeatChain : Chain() {
    companion object {
        private const val VALUE_STEP = 34 // id(16bit)2 + content (8bit*32)32
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return HEARTBEAT_COMMAND_ID == pullBufInfo.command
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val heartBeatList = ReceivedData.HeatBeatList()
        val data = pullBufInfo.pollBuf
        val length = pullBufInfo.length
        if ((length - 2) % VALUE_STEP != 0) {
            return heartBeatList
        }
        for (i in data.indices step VALUE_STEP) {
            val info = ReceivedData.HeartBeat()
            val id = ((data[i + 1].toInt() and 0xFF) shl 8) or (data[i].toInt() and 0xFF)
            val value = ((data[i + 3].toInt() and 0xFF) shl 8) or (data[i + 2].toInt() and 0xFF)

            when (id) {
                in MakeDrinkStatusEnum.LEFT_BREWING.value..
                        MakeDrinkStatusEnum.RIGHT_BREW_COMPLETED.value,
                    -> {// 1000 - 1003
                    val drinkStatus = MakeDrinkStatusEnum.getStatus(id)
                    info.makeDrinkStatus = HeartBeatReply.MakeDrinkStatus(drinkStatus, value)
                }
                in MakeDrinkStatusEnum.LEFT_FINISHED.value..
                        MakeDrinkStatusEnum.RIGHT_FINISHED.value,
                    -> { // 1004 - 1005
                    val drinkStatus = MakeDrinkStatusEnum.getStatus(id)
                    val params = data.sliceArray(4 until data.size - 1)
                    info.makeDrinkStatus =
                        HeartBeatReply.MakeDrinkStatus(drinkStatus, value, params)
                }
                BoilerStatusEnum.BOILER_TEMPERATURE.value -> { // 1006
                    val temperature = BoilerStatusEnum.getStatus(id)
                    val params = data.sliceArray(2 until data.size - 1)
                    info.temperature = HeartBeatReply.BoilerTemperature(temperature, params)
                }
                in GrinderStatusEnum.LEFT_GRINDER_POWDER_DOSAGE.value..
                        GrinderStatusEnum.RIGHT_GRINDER_POWDER_DOSAGE.value,
                    -> {// 1009 - 1010
                    val grinderStatus = GrinderStatusEnum.getStatus(id)
                    info.grinder = HeartBeatReply.GrinderPowderDosage(grinderStatus, value)
                }
                in CleanMachineEnum.CLEAN_COFFEE_FINISH.value..
                        CleanMachineEnum.CLEAN_FOAM_FINISH.value,
                    -> {
                    val status = CleanMachineEnum.getStatus(id)
                    info.cleanMachine = HeartBeatReply.CleanMachineStatus(status)
                }
                in ErrorStatusEnum.FRONT_VAT_EMPTY.value..
                        ErrorStatusEnum.NO_WATER_ERROR.value,
                    -> { // 2000 - 4000
                    val error = ErrorStatusEnum.getStatus(id)
                    info.error = HeartBeatReply.Error(error, value)
                }
            }

            heartBeatList.list.add(info)
        }
        return heartBeatList
    }

}