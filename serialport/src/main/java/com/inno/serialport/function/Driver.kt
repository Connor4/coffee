package com.inno.serialport.function

interface Driver {
    fun send(data: ByteArray)
    fun receive(): ByteArray?
}