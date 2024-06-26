package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult
import com.inno.serialport.bean.PullBufInfo

class BoilerChain : Chain() {
    companion object {
        private const val level = 2000
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        // TEST CODE
        val buffer = pullBufInfo.pollBuf
        val command = ((buffer[5].toInt() and 0xFF) shl 8) or (buffer[6].toInt() and 0xFF)
        return command == 100
    }

    override fun handle(pullBufInfo: PullBufInfo): HandleResult {
        TODO("Not yet implemented")
    }

}