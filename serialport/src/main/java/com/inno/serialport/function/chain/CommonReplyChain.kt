package com.inno.serialport.function.chain

import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
import com.inno.serialport.utilities.COFFEE_INPUT_COMMAND_ID
import com.inno.serialport.utilities.MACHINE_PARAM_COMMAND_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class CommonReplyChain : Chain() {
    private val commonReplyCommandList =
        shortArrayOf(MACHINE_PARAM_COMMAND_ID, COFFEE_INPUT_COMMAND_ID)

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return pullBufInfo.command in commonReplyCommandList
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        Logger.d("CommonReplyChain", "reply  ${pullBufInfo.pollBuf.toHexString()}")
        return ReceivedData.CommonReply(pullBufInfo.command, pullBufInfo.pollBuf)
    }

}