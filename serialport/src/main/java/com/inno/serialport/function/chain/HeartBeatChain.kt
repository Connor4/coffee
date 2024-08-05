package com.inno.serialport.function.chain

import com.inno.serialport.utilities.HEARTBEAT_COMMAND_ID
import com.inno.serialport.utilities.HeartBeatReply
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

class HeartBeatChain : Chain() {
    companion object {
        private const val VALUE_STEP = 4
    }

    private var info = ReceivedData.HeartBeat()

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return HEARTBEAT_COMMAND_ID == pullBufInfo.command
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val data = pullBufInfo.pollBuf
        val length = pullBufInfo.length

        if (length % VALUE_STEP != 0) {
            return info
        }
        for (i in data.indices step VALUE_STEP) {
            val id = ((data[i].toInt() and 0xFF) shl 8) or (data[i + 1].toInt() and 0xFF)
            val value = ((data[i + 2].toInt() and 0xFF) shl 8) or (data[i + 3].toInt() and 0xFF)
            if ((id >= MakeDrinkStatusEnum.LEFT_BREWING.value) and (id < MakeDrinkStatusEnum
                    .RIGHT_FINISHED.value)) { // 1000 - 1006
                val drinkStatus = MakeDrinkStatusEnum.getStatus(id)
                info.makeDrink = HeartBeatReply.MakeDrink(drinkStatus, value)
            } else if ((id >= ErrorStatusEnum.FRONT_VAT_EMPTY.value) and (id < ErrorStatusEnum
                    .STREAM_BOILER_ERROR.value)) { // 2000 - 3006
                info.error = HeartBeatReply.Error(id, value)
            }
        }
        return info
    }

}