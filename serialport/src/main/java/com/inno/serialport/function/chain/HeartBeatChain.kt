package com.inno.serialport.function.chain

import com.inno.serialport.utilities.HEARTBEAT_COMMAND_ID
import com.inno.serialport.utilities.HeartBeatReply
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

class HeartBeatChain : Chain() {
    companion object {
        private const val VALUE_STEP = 18 // id(16bit)2 + content (8bit*16)16
    }

//    private val heartBeatList = ReceivedData.HeatBeatList()

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return HEARTBEAT_COMMAND_ID == pullBufInfo.command
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val heartBeatList = ReceivedData.HeatBeatList()
        val data = pullBufInfo.pollBuf
        val length = pullBufInfo.length

        if (length % VALUE_STEP != 0) {
            return heartBeatList
        }
        for (i in data.indices step VALUE_STEP) {
            val info = ReceivedData.HeartBeat()
            val id = ((data[i].toInt() and 0xFF) shl 8) or (data[i + 1].toInt() and 0xFF)
            val value = ((data[i + 2].toInt() and 0xFF) shl 8) or (data[i + 3].toInt() and 0xFF)

            if ((id >= MakeDrinkStatusEnum.LEFT_BREWING.value) and (id <= MakeDrinkStatusEnum
                    .RIGHT_BREW_COMPLETED.value)) { // 1000 - 1003
                val drinkStatus = MakeDrinkStatusEnum.getStatus(id)
                info.makeDrink = HeartBeatReply.MakeDrink(drinkStatus, value)

            } else if ((id >= MakeDrinkStatusEnum.LEFT_FINISHED.value) and (id <=
                        MakeDrinkStatusEnum.RIGHT_FINISHED.value)) { // 1004 - 1005
                val params = ((data[i + 4].toInt() and 0xFF) shl 8) or (data[i + 5].toInt() and
                        0xFF)
                val drinkStatus = MakeDrinkStatusEnum.getStatus(id)
                info.makeDrink = HeartBeatReply.MakeDrink(drinkStatus, value, params)

            } else if ((id >= BoilerStatusEnum.LEFT_BOILER_TEMPERATURE.value) and (id <=
                        BoilerStatusEnum.RIGHT_GRINDER_POWDER_DOSAGE.value)) { // 1006 - 1010
                val temperature = BoilerStatusEnum.getStatus(id)
                info.temperature = HeartBeatReply.BoilerTemperature(temperature, value)

            } else if ((id >= ErrorStatusEnum.FRONT_VAT_EMPTY.value) and (id <=
                        ErrorStatusEnum.STREAM_BOILER_ERROR.value)) { // 2000 - 3006
                val error = ErrorStatusEnum.getStatus(id)
                info.error = HeartBeatReply.Error(error, value)

            }

            heartBeatList.list.add(info)
        }
        return heartBeatList
    }

}