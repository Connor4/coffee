package com.inno.serialport.function.chain

import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.SerialErrorType

class ErrorChain : Chain() {
    companion object {
        private const val LEVEL = 0
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return pullBufInfo.id < LEVEL
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val errorMsg = SerialErrorType.getErrorMsgByValue(pullBufInfo.id)
        return ReceivedData.ErrorData(errorMsg)
    }

}