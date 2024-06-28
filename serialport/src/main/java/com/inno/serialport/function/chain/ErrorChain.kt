package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.bean.ReceivedData
import com.inno.serialport.bean.SerialErrorType

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