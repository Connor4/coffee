package com.inno.serialport.utilities

import kotlinx.serialization.Serializable

@Serializable
data class PullBufInfo(val id: Int, val pollBuf: ByteArray = byteArrayOf())

// poll指令
//7e 02 01 02 00 01 00 cc 2b 7e //send
//7E 02 02 04 00 01 00 00 00 4F 4C 7E //reply
//7E //包头
//02 //地址
//02 //控制
//04 00 //长度
//64 00 //指令
//00 00 //结果： 0：success, 其他： fault
//FC BB //crc
//7E //reply
