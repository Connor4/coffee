package com.inno.serialport.function.driver

import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.profile.ProductProfile

interface IDriver {
    fun send(command: Short, productProfile: ProductProfile)
    suspend fun receive(): List<PullBufInfo>
    fun open()
    fun close()
}