package com.inno.common.utils

import java.nio.ByteBuffer

fun ByteArray.toHexString(): String {
    return joinToString("") { String.format("%02x ", it) }
}

fun ByteBuffer.toHexString(): String {
    val originPosition = this.position()
    this.position(0)
    val hexString = StringBuilder()
    while (this.hasRemaining()) {
        val byte = this.get()
        hexString.append(String.format("%02X ", byte))
    }
    this.position(originPosition)
    return hexString.toString()
}

fun Short.toHexString(): String {
    return String.format("%04x ", this.toInt() and 0xFFFF)
}