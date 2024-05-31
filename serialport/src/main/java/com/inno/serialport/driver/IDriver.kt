package com.inno.serialport.driver

interface IDriver {
    fun send(frame: ByteArray)
    fun receive(): String?
    fun parseFrame(frame: ByteArray): String?
    fun close()
}