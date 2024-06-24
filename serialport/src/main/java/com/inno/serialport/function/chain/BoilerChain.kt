package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult
import com.inno.serialport.bean.PullBufInfo

class BoilerChain : Chain() {
    companion object {
        private const val level = 2000
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        TODO("Not yet implemented")
    }

    override fun handle(pullBufInfo: PullBufInfo): HandleResult {
        TODO("Not yet implemented")
    }

}