package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult
import com.inno.serialport.bean.PullBufInfo

class HeartBeatChain : Chain() {

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        // determine based on the command filed
        val buffer = pullBufInfo.pollBuf
        val command = ((buffer[5].toInt() and 0xFF) shl 8) or (buffer[6].toInt() and 0xFF)
        return command == 1
    }

    override fun handle(pullBufInfo: PullBufInfo): HandleResult {
        val buffer = pullBufInfo.pollBuf
        val result = ((buffer[7].toInt() and 0xFF) shl 8) or (buffer[8].toInt() and 0xFF)
        return HandleResult(heartbeat = (result == 0))
    }
}