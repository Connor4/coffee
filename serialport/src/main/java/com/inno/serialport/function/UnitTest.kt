package com.inno.serialport.function

import com.inno.common.utils.toHexString
import com.inno.serialport.utilities.ComponentProfile
import com.inno.serialport.utilities.ComponentProfileList
import com.inno.serialport.utilities.ProductProfile
import com.inno.serialport.utilities.fcstab
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
private val serializeBuffer = ByteBuffer.allocate(COMMAND_BUFFER_CAPACITY)
private val contentBuffer = ByteBuffer.allocate(CONTENT_BUFFER_CAPACITY)
private val commandBuffer = ByteBuffer.allocate(TOTAL_BUFFER_SIZE)
private val packBuffer = ByteBuffer.allocate(PACK_BUFFER_SIZE)

fun main() {
    testProductCommand()
}

fun testProductCommand() {
    val info = createProductProfile()
    println("info: $info")
    val command = Json.encodeToString(info)
    println("string: $command")
    contentBuffer.order(ByteOrder.LITTLE_ENDIAN)
    commandBuffer.order(ByteOrder.LITTLE_ENDIAN)
    packBuffer.order(ByteOrder.LITTLE_ENDIAN)
    serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)

    lock.withLock {
        val serializeProductInfo = serializeProductInfo(command)
        println("serialize: " + toHexString(serializeProductInfo))
        contentBuffer.clear()
        contentBuffer.put(FRAME_ADDRESS)
        contentBuffer.put(FRAME_CONTROL)
        // length
        contentBuffer.putShort((COMMAND_BUFFER_CAPACITY + 2).toShort())
        println("size: ${COMMAND_BUFFER_CAPACITY + 2}")
        // cmd
        contentBuffer.putShort(0x64.toShort())
        contentBuffer.put(serializeProductInfo)
        // crc
        val crc = calculateCRC(contentBuffer)
        println("crc ${Integer.toHexString(crc.toInt())}")
        commandBuffer.clear()
        commandBuffer.put(contentBuffer.array())
        commandBuffer.putShort(crc)
        println("command:   ${commandBuffer.toHexString()}")

        packBuffer.clear()
        packBuffer.put(FRAME_FLAG)
        val escapeData = escapeData(commandBuffer.array())
        packBuffer.put(escapeData)
        packBuffer.put(FRAME_FLAG)
        packBuffer.flip()
        val packFrame = ByteArray(packBuffer.limit())
        packBuffer.get(packFrame)
        println("final:     " + packBuffer.toHexString())

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

private fun serializeProductInfo(command: String): ByteArray {
    val productInfo = Json.decodeFromString<ProductProfile>(command)
    serializeBuffer.clear()
    serializeBuffer.putShort(productInfo.productId)
    serializeBuffer.putShort(productInfo.preFlush)
    serializeBuffer.putShort(productInfo.postFlush)
    serializeBuffer.putShort(productInfo.componentProfileList.componentNum)

    val componentSize = productInfo.componentProfileList.componentList.size
    for (i in 0 until MAX_COMPONENT) {
        if (i < componentSize) {
            val componentProfile = productInfo.componentProfileList.componentList[i]
            serializeBuffer.putShort(componentProfile.componentId)
            for (para in componentProfile.para) {
                serializeBuffer.putShort(para)
            }
        } else {
            serializeBuffer.putShort(0)
        }
    }
    serializeBuffer.flip()
    val result = ByteArray(serializeBuffer.limit())
    serializeBuffer.get(result)
    return result
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