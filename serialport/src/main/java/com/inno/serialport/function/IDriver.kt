package com.inno.serialport.function

import com.inno.serialport.bean.PullBufInfo

interface IDriver {
    fun send(frame: ByteArray)
    fun receive(): PullBufInfo
    fun parseFrame(frame: ByteArray): String?
    fun close()
}