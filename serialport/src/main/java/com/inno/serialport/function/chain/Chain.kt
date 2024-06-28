package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.bean.ReceivedData

abstract class Chain {
    private var nextHandler: Chain? = null

    fun setNextHandler(handler: Chain): Chain {
        nextHandler = handler
        return handler
    }

    fun handleRequest(pullBufInfo: PullBufInfo): ReceivedData? {
        return if (canHandle(pullBufInfo)) {
            handle(pullBufInfo)
        } else {
            nextHandler?.handleRequest(pullBufInfo)
        }
    }

    abstract fun canHandle(pullBufInfo: PullBufInfo): Boolean
    abstract fun handle(pullBufInfo: PullBufInfo): ReceivedData?

}