package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo

class ErrorHandler : Handler() {
    companion object {
        private const val level = 1
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        TODO("Not yet implemented")
    }

    override fun handle(pullBufInfo: PullBufInfo): Int {
        TODO("Not yet implemented")
    }
}