package com.inno.serialport.function.driver

import com.inno.serialport.utilities.PullBufInfo

interface IDriver {
    fun send(command: Short, infoSize: Int, commandInfo: ByteArray)
    suspend fun receive(): List<PullBufInfo>
    fun open()
    fun close()
}