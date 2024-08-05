package com.inno.serialport.function.chain

import com.inno.serialport.utilities.ERROR_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.SerialErrorTypeEnum

class SerialPortErrorChain : Chain() {

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return pullBufInfo.command < ERROR_ID
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        val errorMsg = SerialErrorTypeEnum.getErrorMsgByValue(pullBufInfo.command)
        return ReceivedData.SerialErrorData(errorMsg)
    }

}