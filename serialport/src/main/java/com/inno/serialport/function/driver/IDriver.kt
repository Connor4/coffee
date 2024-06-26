package com.inno.serialport.function.driver

import com.inno.serialport.bean.PullBufInfo

interface IDriver {
    fun send(command: String)
    suspend fun receive(): PullBufInfo
    fun open()
    fun close()
}