package com.inno.serialport.driver

interface Driver {
    fun send(data: ByteArray)
    fun receive(): ByteArray?
}