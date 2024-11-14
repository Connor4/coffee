package com.inno.coffee

import com.inno.common.utils.toHexString

fun main() {
    val intArrayOf = intArrayOf()
    val bytes = intArrayConvertByte(intArrayOf)
    println("size ${bytes.size}" + bytes.toHexString())
}

private fun intArrayConvertByte(commandInfo: IntArray): ByteArray {
    val byteArray = ByteArray(commandInfo.size * 2)

    commandInfo.forEachIndexed { index, value ->
        byteArray[index * 2] = (value shr 8).toByte()
        byteArray[index * 2 + 1] = (value and 0xFF).toByte()
    }
    return byteArray
}