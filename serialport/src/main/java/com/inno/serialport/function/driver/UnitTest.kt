package com.inno.serialport.function.driver

import com.inno.common.utils.toHexString
import com.inno.serialport.utilities.fcstab
import com.inno.serialport.utilities.profile.ComponentProfile
import com.inno.serialport.utilities.profile.ComponentProfileList
import com.inno.serialport.utilities.profile.ProductProfile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val MAX_COMPONENT: Short = 7
// ProductProfile = id2 + preFlush2 + postFlush2 + ComponentProfileList: num2 + MAX_COMPONENT * (comid2 + 2*para6)
private const val COMMAND_BUFFER_CAPACITY = 8 + 14 * MAX_COMPONENT
// 6 = addr1 + control1 + len2 + cmd2
private const val CONTENT_BUFFER_CAPACITY = COMMAND_BUFFER_CAPACITY + 6
// 8 = addr1 + control1 + len2 + cmd2 + crc2
private const val TOTAL_BUFFER_SIZE = COMMAND_BUFFER_CAPACITY + 8
private const val PACK_BUFFER_SIZE = TOTAL_BUFFER_SIZE + 16
private const val FRAME_FLAG = 0x7E.toByte()
private const val FRAME_ADDRESS = 0x2.toByte()
private const val FRAME_CONTROL = 0X1.toByte()
private val lock = ReentrantLock()

fun main() {
    testProductCommand()
}

fun testProductCommand() {
    val productProfile = createProductProfile()
    println("string: $productProfile")

    lock.withLock {
        val componentSize = productProfile.componentProfileList.componentList.size
        val productFileSize = 8 + 14 * componentSize
        val serializeBuffer = ByteBuffer.allocate(productFileSize)
        serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)
        serializeBuffer.putShort(productProfile.productId)
        serializeBuffer.putShort(productProfile.preFlush)
        serializeBuffer.putShort(productProfile.postFlush)
        serializeBuffer.putShort(productProfile.componentProfileList.componentNum)

        for (i in 0 until componentSize) {
            val componentProfile = productProfile.componentProfileList.componentList[i]
            serializeBuffer.putShort(componentProfile.componentId)
            for (para in componentProfile.para) {
                serializeBuffer.putShort(para)
            }
        }
        serializeBuffer.flip()
        val serializeProductInfo = ByteArray(serializeBuffer.limit())
        serializeBuffer.get(serializeProductInfo)
        println("serialize ${serializeProductInfo.toHexString()}")

        val contentSize = productFileSize + 6
        val contentBuffer = ByteBuffer.allocate(contentSize)
        contentBuffer.order(ByteOrder.LITTLE_ENDIAN)
        contentBuffer.put(FRAME_ADDRESS)
        contentBuffer.put(FRAME_CONTROL)
        // length
        contentBuffer.putShort((COMMAND_BUFFER_CAPACITY + 2).toShort())
        // cmd
        contentBuffer.putShort(100.toShort())
        contentBuffer.put(serializeProductInfo)
        // crc
        val crc = calculateCRC(contentBuffer)

        val commandSize = contentSize + 2
        val commandBuffer = ByteBuffer.allocate(commandSize)
        commandBuffer.order(ByteOrder.LITTLE_ENDIAN)
        commandBuffer.clear()
        commandBuffer.put(contentBuffer.array())
        commandBuffer.putShort(crc)
        val contentArray = commandBuffer.array()
        println("content ${contentArray.toHexString()}")
        val escapeData = escapeData(contentArray)
        val escapeSize = escapeData.size
        println("escape ${escapeData.toHexString()}")

        val packSize = escapeSize + 2
        val packBuffer = ByteBuffer.allocate(packSize)
        packBuffer.order(ByteOrder.LITTLE_ENDIAN)
        packBuffer.clear()
        packBuffer.put(FRAME_FLAG)
        packBuffer.put(escapeData)
        packBuffer.put(FRAME_FLAG)
        packBuffer.flip()
        val packFrame = ByteArray(packBuffer.limit())
        packBuffer.get(packFrame)
        println("pack ${packFrame.toHexString()}")
    }
}

fun createProductProfile(): ProductProfile {
    val profile = ProductProfile(
        productId = 0x01,
        preFlush = 0,
        postFlush = 0,
        componentProfileList = ComponentProfileList(
            componentNum = MAX_COMPONENT,
            componentList = listOf(
                ComponentProfile(
                    componentId = 0xbb8,
                    para = shortArrayOf(
                        0x8c.toShort(),
                        0, 0, 0, 0, 0
                    ),
                ),
                ComponentProfile(
                    componentId = 0x3e8,
                    para = shortArrayOf(
                        0x14.toShort(),
                        0x320.toShort(),
                        0x7d0.toShort(),
                        0, 0, 0
                    )
                ),
                ComponentProfile(
                    componentId = 0x7d0,
                    para = shortArrayOf(
                        0x32.toShort(),
                        0x96.toShort(),
                        0, 1, 0, 0
                    )
                ),
                ComponentProfile(
                    componentId = 0x1388,
                    para = shortArrayOf(
                        0, 0, 0, 0, 0, 0
                    )
                ),
                ComponentProfile(
                    componentId = 0xfa0,
                    para = shortArrayOf(
                        0, 0, 0, 0, 0, 0
                    )
                ),
                ComponentProfile(
                    componentId = 0xfa2,
                    para = shortArrayOf(
                        0, 0, 0, 0, 0, 0
                    )
                ),
                ComponentProfile(
                    componentId = 0xfa3,
                    para = shortArrayOf(
                        0, 0, 0, 0, 0, 0
                    )
                ),
            )
        )
    )
    return profile
}

fun formatValue(value: String) {
    val length = value.length
    var index = 0
    while (index < length - 5) {
        val temp = value.substring(index, index + 5)
        println(temp)
        index += 6
    }
}

private fun calculateCRC(buffer: ByteBuffer): Short {
    val hexString = buffer.toHexString()
    val data = hexString.windowed(2, 3).map { it.trim().lowercase().toInt(16) }
    var crc = 0xFFFF
    for (item in data) {
        val index = (crc xor (item and 0xFF)) and 0xFF
        crc = (crc ushr 8) xor fcstab[index]
//        println(
//            "data: ${String.format("%02X ", item)},fcs: ${String.format("%02X ", crc)}" +
//                    "fcsIndex: ${String.format("%02X ", index)}," +
//                    "tab: ${String.format("%02X ", fcstab[index])}"
//        )
    }
    return (crc and 0xFFFF).toShort()
}

private fun escapeData(data: ByteArray): ByteArray {
    val buffer = mutableListOf<Byte>()
    for (b in data) {
        when (b) {
            0x7E.toByte() -> {
                buffer.add(0x7D)
                buffer.add(0x5E)
            }

            0x7D.toByte() -> {
                buffer.add(0x7D)
                buffer.add(0x5D)
            }

            else -> buffer.add(b)
        }
    }
    return buffer.toByteArray()
}

private fun toHexString(data: ByteArray): String {
    val sb = StringBuilder()
    for (b in data) {
        sb.append(String.format("%02X ", b))
    }
    return sb.toString().trim()
}