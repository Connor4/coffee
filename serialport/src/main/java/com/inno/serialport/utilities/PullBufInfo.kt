package com.inno.serialport.utilities

import kotlinx.serialization.Serializable

@Serializable
data class PullBufInfo(val id: Int, val pollBuf: ByteArray = byteArrayOf())
