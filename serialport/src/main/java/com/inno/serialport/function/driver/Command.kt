package com.inno.serialport.function.driver

data class Command(
    val id: Short,
    val size: Int,
    val address: Byte,
    val data: ByteArray,
)

