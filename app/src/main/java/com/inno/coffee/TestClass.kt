package com.inno.coffee

import com.inno.common.utils.toHexString

fun main() {
    val result = 10
    println("result $result")
    val shortResult = 10.toShort()
    val byte = byteArrayOf(result.toByte(), shortResult.toByte())
    println("short $shortResult")
    println("byte ${byte.toHexString()}")
}