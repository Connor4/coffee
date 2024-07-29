package com.inno.serialport.utilities

import kotlinx.serialization.Serializable

@Serializable
data class PullBufInfo(val length: Int = 0, val command: Int = 0,
    val pollBuf: ByteArray = byteArrayOf())

// poll指令
//7e 02 01 02 00 01 00 cc 2b 7e //send
//7E 02 02 04 00 01 00 00 00 4F 4C 7E //reply