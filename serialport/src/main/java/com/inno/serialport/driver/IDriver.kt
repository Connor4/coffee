package com.inno.serialport.driver

interface IDriver {
    fun send(frame: ByteArray)
    fun receive(): ByteArray?
    fun parseFrame(frame: ByteArray)
    fun close()
}