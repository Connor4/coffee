package com.inno.serialport.function.driver

import com.inno.common.utils.toHexString
import com.inno.serialport.utilities.FRAME_CMD_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_CMD_INDEX_LOW
import com.inno.serialport.utilities.FRAME_CONTENT_START_INDEX
import com.inno.serialport.utilities.FRAME_DATA_START_INDEX
import com.inno.serialport.utilities.FRAME_FLAG_INDEX
import com.inno.serialport.utilities.FRAME_LENGTH_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_LENGTH_INDEX_LOW
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.fcstab
import com.inno.serialport.utilities.statusenum.SerialErrorTypeEnum
import java.nio.ByteBuffer

private const val TAG = "RS485DriverTest"
private const val FRAME_FLAG = 0x7E.toByte()
private const val MINIMUM_FRAME_PACK_SIZE = 12
private const val FD = 0x5D.toByte()
private const val FE = 0x5E.toByte()
private const val SE = 0X7E.toByte()
private const val SD = 0x7D.toByte()

fun main() {
//    testReceivedEscape()
    testReceivedData()
}

private fun testReceivedData() {
    var receivedData: PullBufInfo? = null
    val buffer = byteArrayOf(
        0x7e.toByte(), 0x02.toByte(), 0x02.toByte(), 0x14.toByte(), 0x00.toByte(), 0x01.toByte(),
        0x00.toByte(), 0xee.toByte(), 0x03.toByte(), 0x5d.toByte(), 0x00.toByte(), 0x5b.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x7d.toByte(), 0x5e.toByte(), 0x0d.toByte(), 0x7e.toByte(),
    )

    val multiInfo = slicePullInfo(buffer)
    if (multiInfo.isEmpty()) {
        receivedData = PullBufInfo(command = SerialErrorTypeEnum.FRAME_FORMAT_ILLEGAL.value)
    }
    for (info in multiInfo) {
        receivedData = validPullInfo(info) ?: parsePullBuffInfo(info)
    }
    println("receivedData $receivedData")
}

private fun testReceivedEscape() {
    val buffer = byteArrayOf(
        0x7e.toByte(), 0x02.toByte(), 0x02.toByte(), 0x14.toByte(), 0x00.toByte(), 0x01.toByte(),
        0x00.toByte(), 0xee.toByte(), 0x03.toByte(), 0x5d.toByte(), 0x00.toByte(), 0x5b.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
        0x00.toByte(), 0x7d.toByte(), 0x5e.toByte(), 0x0d.toByte(), 0x7e.toByte(),
    )
    println("buffer ${buffer.toHexString()}  size ${buffer.size}")
    val escape = escapeReceivedData(buffer)
    println("new ${escape.toHexString()}")
    val data = escape.sliceArray(FRAME_DATA_START_INDEX until escape.size - 3)
    println("data ${data.toHexString()}")
    val newBuffer = ByteBuffer.allocate(data.size)
    newBuffer.put(data)
    newBuffer.flip()
    val calculateCRC = calculateCRC(newBuffer)
    println("crc: ${calculateCRC.toHexString()}")
}

private fun escapeReceivedData(data: ByteArray): ByteArray {
    val transformedData = mutableListOf<Byte>()
    var i = 1
    val end = data.size - 1
    transformedData.add(data[0])

    while (i < end) {
        when (data[i]) {
            SD -> {
                if (i + 1 < end) {
                    when (data[i + 1]) {
                        FD -> {
                            transformedData.add(SD)
                            i++
                        }
                        FE -> {
                            transformedData.add(SE)
                            i++
                        }
                        else -> transformedData.add(data[i])
                    }
                } else {
                    transformedData.add(data[i])
                }
            }
            else -> transformedData.add(data[i])
        }
        i++
    }
    transformedData.add(data[i])
    return transformedData.toByteArray()
}

private fun slicePullInfo(buffer: ByteArray): List<ByteArray> {
    println("slicePullInfo() called with: buffer = ${buffer.toHexString()}")
    val multiPullInfo = mutableListOf<ByteArray>()
    var start = -1
    for (i in buffer.indices) {
        if (buffer[i] == FRAME_FLAG) {
            if (start == -1) {
                start = i
            } else {
                val info = buffer.sliceArray(start until i + 1)
                val escapeReceivedData = escapeReceivedData(info)
                multiPullInfo.add(escapeReceivedData)
                println("slicePullInfo: ${escapeReceivedData.toHexString()}")
                start = -1
            }
        }
    }
    return multiPullInfo
}

private fun validPullInfo(buffer: ByteArray): PullBufInfo? {
    val size = buffer.size
    if (size < MINIMUM_FRAME_PACK_SIZE) {
        println("frame size error")
        return PullBufInfo(command = SerialErrorTypeEnum.FRAME_SIZE_ERROR.value)
    }
    if (buffer[FRAME_FLAG_INDEX] != FRAME_FLAG || buffer[size - 1] != FRAME_FLAG) {
        println("Invalid packet header or footer")
        return PullBufInfo(command = SerialErrorTypeEnum.FRAME_FORMAT_ILLEGAL.value)
    }

    val receivedCRC = ((buffer[size - 2].toUByte().toInt() shl 8) or (buffer[size - 3].toUByte()
        .toInt())).toShort()
    println(
        "buffer: ${buffer.toHexString()}, Received CRC: ${receivedCRC.toHexString()}")

    // exclude frame flag and crc
    val payload = buffer.sliceArray(FRAME_DATA_START_INDEX until size - 3)
    val payloadBuffer = ByteBuffer.allocate(payload.size)
    payloadBuffer.put(payload)
    payloadBuffer.flip()
    println(
        "payload: ${payload.toHexString()}， payloadBuffer: ${payloadBuffer.toHexString()}")

    val calculatedCRC = calculateCRC(payloadBuffer)
    if (receivedCRC != calculatedCRC) {
        println("CRC check failed: received ${receivedCRC.toHexString()}," +
                " calculated ${calculatedCRC.toHexString()}")
        return PullBufInfo(command = SerialErrorTypeEnum.CRC_CHECK_FAILED.value)
    }
    return null
}

private fun parsePullBuffInfo(buffer: ByteArray): PullBufInfo {
    val length = ((buffer[FRAME_LENGTH_INDEX_LOW].toInt() and 0xFF) shl 8) or
            (buffer[FRAME_LENGTH_INDEX_HIGH].toInt() and 0xFF)
    val command = ((buffer[FRAME_CMD_INDEX_LOW].toInt() and 0xFF) shl 8) or
            (buffer[FRAME_CMD_INDEX_HIGH].toInt() and 0xFF)
    val content = buffer.sliceArray(FRAME_CONTENT_START_INDEX until buffer.size - 2)
    return PullBufInfo(length, command.toShort(), content)
}

private fun calculateCRC(buffer: ByteBuffer): Short {
    val hexString = buffer.toHexString()
    val data = hexString.windowed(2, 3).map { it.trim().lowercase().toInt(16) }
    var crc = 0xFFFF
    for (item in data) {
        val index = (crc xor (item and 0xFF)) and 0xFF
        crc = (crc ushr 8) xor fcstab[index]
    }
    return (crc and 0xFFFF).toShort()
}
