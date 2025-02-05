package com.inno.serialport.function.chain

import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
import com.inno.serialport.utilities.CLEAN_FOAM_MODULE_ID
import com.inno.serialport.utilities.GET_FW_VERSION_ID
import com.inno.serialport.utilities.GRINDER_ADJ_GRIND_ID
import com.inno.serialport.utilities.MACHINE_PARAM_COMMAND_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class CommonReplyChain : Chain() {
    private val commonReplyCommandList = MACHINE_PARAM_COMMAND_ID until GRINDER_ADJ_GRIND_ID + 1
    private val commonReplyCommandList2 = GET_FW_VERSION_ID until CLEAN_FOAM_MODULE_ID + 1

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return pullBufInfo.command in commonReplyCommandList || pullBufInfo.command in commonReplyCommandList2
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        Logger.d("CommonReplyChain", "reply  ${pullBufInfo.pollBuf.toHexString()}")
        return ReceivedData.CommonReply(pullBufInfo.command, pullBufInfo.pollBuf)
    }

}