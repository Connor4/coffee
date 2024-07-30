package com.inno.serialport.function.chain

import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.SerialErrorTypeEnum

class ErrorChain : Chain() {
    companion object {
        private const val ERROR_ID = 0
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return pullBufInfo.command < ERROR_ID
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val errorMsg = SerialErrorTypeEnum.getErrorMsgByValue(pullBufInfo.command)
        return ReceivedData.ErrorData(errorMsg)
    }

}