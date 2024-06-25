package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult
import com.inno.serialport.bean.PullBufInfo

abstract class Chain {
    private var nextHandler: Chain? = null

    fun setNextHandler(handler: Chain): Chain {
        nextHandler = handler
        return handler
    }

    fun handleRequest(pullBufInfo: PullBufInfo): HandleResult {
        if (canHandle(pullBufInfo)) {
            return handle(pullBufInfo)
        } else {
            nextHandler?.handleRequest(pullBufInfo)
        }
        return HandleResult()
    }

    abstract fun canHandle(pullBufInfo: PullBufInfo): Boolean
    abstract fun handle(pullBufInfo: PullBufInfo): HandleResult

}