package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo

abstract class Handler {
    private var nextHandler: Handler? = null

    fun setNextHandler(handler: Handler): Handler {
        nextHandler = handler
        return handler
    }

    fun handleRequest(pullBufInfo: PullBufInfo): Int {
        if (canHandle(pullBufInfo)) {
            return handle(pullBufInfo)
        } else {
            nextHandler?.handleRequest(pullBufInfo)
        }
        return -1
    }

    abstract fun canHandle(pullBufInfo: PullBufInfo): Boolean
    abstract fun handle(pullBufInfo: PullBufInfo): Int

}