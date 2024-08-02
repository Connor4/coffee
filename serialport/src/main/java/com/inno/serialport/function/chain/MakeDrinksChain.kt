package com.inno.serialport.function.chain

import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND
import com.inno.serialport.utilities.MakeDrinkStatusEnum
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class MakeDrinksChain : Chain() {

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return MAKE_DRINKS_COMMAND == pullBufInfo.command
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val data = pullBufInfo.pollBuf
        var productId = -1
        var status = -1
        var i = 0
        while (i < data.size) {
            val end = (i + 2).coerceAtMost(data.size)
            val item = data.copyOfRange(i, end)
            val value = ((item[0].toInt() and 0xFF) shl 8) or (item[1].toInt() and 0xFF)
            if (i == 0) {
                status = value
            } else if (i == 2) {
                productId = value
            }
            i += 2
        }
        val statusEnum = MakeDrinkStatusEnum.getStatus(status)
        return ReceivedData.DrinkData(statusEnum, productId)
    }

}