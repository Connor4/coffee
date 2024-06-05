package com.inno.serialport.function.processor

class GrindProcessor : Processor {
    companion object {
        private const val level = 1000
    }

    override fun intercept(chain: Processor.Chain): Boolean {
        return true
    }

}