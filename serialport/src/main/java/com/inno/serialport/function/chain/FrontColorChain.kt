package com.inno.serialport.function.chain

import com.inno.serialport.utilities.FRONT_SINGLE_COLOR_ID
import com.inno.serialport.utilities.FRONT_TWINKLE_COLOR_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class FrontColorChain : Chain() {
    private val frontColorCommandList = FRONT_SINGLE_COLOR_ID until FRONT_TWINKLE_COLOR_ID + 1

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return pullBufInfo.command in frontColorCommandList
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        return ReceivedData.FrontColor(pullBufInfo.command, pullBufInfo.pollBuf)
    }
}