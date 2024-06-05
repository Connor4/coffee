package com.inno.serialport.function.processor

class BoilerProcessor : Processor {
    companion object {
        private const val level = 2000
    }

    override fun intercept(chain: Processor.Chain): Boolean {

        return true
    }
}