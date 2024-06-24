package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult

class BoilerChain : Chain() {
    companion object {
        private const val level = 2000
    }

    override fun canHandle(handleResult: HandleResult): Boolean {
        TODO("Not yet implemented")
    }

    override fun handle(handleResult: HandleResult): HandleResult {
        TODO("Not yet implemented")
    }

}