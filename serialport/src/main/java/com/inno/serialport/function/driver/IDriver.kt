package com.inno.serialport.function.driver

import com.inno.serialport.utilities.PullBufInfo

interface IDriver {
    fun send(command: Short, content: String)
    suspend fun receive(): PullBufInfo
    fun open()
    fun close()
}