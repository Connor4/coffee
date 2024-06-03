package com.inno.serialport.function

interface IDriver {
    fun send(frame: ByteArray)
    fun receive(): String?
    fun parseFrame(frame: ByteArray): String?
    fun close()
}