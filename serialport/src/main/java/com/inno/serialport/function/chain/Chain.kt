package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult

abstract class Chain {
    private var nextHandler: Chain? = null

    fun setNextHandler(handler: Chain): Chain {
        nextHandler = handler
        return handler
    }

    fun handleRequest(handleResult: HandleResult): HandleResult {
        if (canHandle(handleResult)) {
            return handle(handleResult)
        } else {
            nextHandler?.handleRequest(handleResult)
        }
        return HandleResult()
    }

    abstract fun canHandle(handleResult: HandleResult): Boolean
    abstract fun handle(handleResult: HandleResult): HandleResult

}