package com.inno.serialport.function

import com.inno.serialport.bean.ComponentList
import com.inno.serialport.bean.ProductInfo
import com.inno.serialport.bean.SingleComponent
import com.inno.serialport.bean.SingleTree
import com.inno.serialport.bean.TreeList
import com.inno.serialport.bean.fcstab
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

//private const val MAX_BYTEARRAY_SIZE = 265 // 256 + 9
private const val MAX_COMPONENT = 4
private const val MAX_TREE = 4

// ComponentList = 2 + SingleComponent: (2 + 2 * 4)*MaxComponent
// TreeList = 2 + SingleTree: (2 * 5)*MaxTree
// CAPACITY = productId + ComponentList + TreeList
private const val COMMAND_BUFFER_CAPACITY =
    6 + 10 * MAX_COMPONENT + 10 * MAX_TREE
private const val TOTAL_BUFFER_SIZE = COMMAND_BUFFER_CAPACITY + 8
private const val PACK_BUFFER_SIZE = TOTAL_BUFFER_SIZE + 16
private const val FRAME_FLAG = 0x7E.toByte()
private const val FRAME_ADDRESS = 0x2.toByte()
private const val FRAME_CONTROL = 0X1.toByte()
private val lock = ReentrantLock()
private val commandBuffer = ByteBuffer.allocate(TOTAL_BUFFER_SIZE)
private val packBuffer = ByteBuffer.allocate(PACK_BUFFER_SIZE)
private val serializeBuffer = ByteBuffer.allocate(COMMAND_BUFFER_CAPACITY)

fun main() {
    val info = createInfo()
    println("info: $info")
    val command = Json.encodeToString(info)
    println("string: $command")
    commandBuffer.order(ByteOrder.LITTLE_ENDIAN)
    packBuffer.order(ByteOrder.LITTLE_ENDIAN)
    serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)

    lock.withLock {
        val serializeProductInfo = serializeProductInfo(command)
        println("serialize: " + toHexString(serializeProductInfo))
        commandBuffer.clear()
        commandBuffer.put(FRAME_ADDRESS)
        commandBuffer.put(FRAME_CONTROL)
        // length
        commandBuffer.putShort((COMMAND_BUFFER_CAPACITY + 2).toShort())
        println("size: ${COMMAND_BUFFER_CAPACITY + 2}")
        // cmd
        commandBuffer.putShort(0x64.toShort())
        commandBuffer.put(serializeProductInfo)
        // crc
        val crc = calculateCRC(commandBuffer)
        commandBuffer.putShort(crc)
        println("pack:      ${commandBuffer.toHexString()}")

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

fun createInfo(): ProductInfo {
    val productInfo = ProductInfo(
        productId = 0x01,
        componentList = ComponentList(
            componentNum = 4,
            singleComponent = listOf(
                SingleComponent(
                    componentId = 0x0bb8,
                    dosage = shortArrayOf(
                        0x347e.toShort(),
                        0x787d.toShort(),
                        0xCDAB.toShort(),
                        0xEEEF.toShort()
                    )
                ),
                SingleComponent(
                    componentId = 0x3e8,
                    dosage = shortArrayOf(
                        0x34bb.toShort(),
                        0x7856.toShort(),
                        0xCDAB.toShort(),
                        0xEEEF.toShort()
                    )
                ),
                SingleComponent(
                    componentId = 0x7d0,
                    dosage = shortArrayOf(
                        0x34bb.toShort(),
                        0x7856.toShort(),
                        0xCDAB.toShort(),
                        0xEEEF.toShort()
                    )
                ),
                SingleComponent(
                    componentId = 0xfa0,
                    dosage = shortArrayOf(
                        0x34bb.toShort(),
                        0x7856.toShort(),
                        0xCDAB.toShort(),
                        0xEEEF.toShort()
                    )
                )
            )
        ),
        treeList = TreeList(
            treeLen = 4,
            singleTree = listOf(
                SingleTree(
                    treeNr = 0x0,
                    componentId = 0xb8b,
                    sendComponentId = 0x0,
                    receiveComponentId = 0x3e8,
                    conflictComponentId = 0x0
                ),
                SingleTree(
                    treeNr = 0x01,
                    componentId = 0x3e8,
                    sendComponentId = 0xb8b,
                    receiveComponentId = 0x0,
                    conflictComponentId = 0x0
                ),
                SingleTree(
                    treeNr = 0x02,
                    componentId = 0xfa0,
                    sendComponentId = 0x0,
                    receiveComponentId = 0x0,
                    conflictComponentId = 0x0
                ),
                SingleTree(
                    treeNr = 0x03,
                    componentId = 0x7d0,
                    sendComponentId = 0x0,
                    receiveComponentId = 0x3e8,
                    conflictComponentId = 0x0
                )
            )
        )
    )
    return productInfo
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

private fun calculateCRC1(data: ByteArray): Short {
    var fcs = 0xFFFF
    var index = 0
    var remaining = data.size
    while (remaining > 0) {
        val fcstabIndex = (fcs xor data[index].toInt()) and 0xFF
        fcs = (fcs shr 8) xor fcstab[fcstabIndex]
        println("data: ${data[index].toInt()},fcsIndex: $fcstabIndex, tab: ${fcstab[fcstabIndex]}, fcs: $fcs")
        index++
        remaining--
    }
    return fcs.toShort()
}

private fun serializeProductInfo(command: String): ByteArray {
    val productInfo = Json.decodeFromString<ProductInfo>(command)
    serializeBuffer.clear()
    serializeBuffer.putShort(productInfo.productId)
    serializeBuffer.putShort(productInfo.componentList.componentNum)

    val componentSize = productInfo.componentList.singleComponent.size
    for (i in 0 until MAX_COMPONENT) {
        if (i < componentSize) {
            val singleComponent = productInfo.componentList.singleComponent[i]
            serializeBuffer.putShort(singleComponent.componentId)
            for (dosage in singleComponent.dosage) {
                serializeBuffer.putShort(dosage)
            }
        } else {
            serializeBuffer.putShort(0)
        }
    }

    serializeBuffer.putShort(productInfo.treeList.treeLen)
    val treeSize = productInfo.treeList.singleTree.size
    for (i in 0 until MAX_TREE) {
        if (i < treeSize) {
            val tree = productInfo.treeList.singleTree[i]
            serializeBuffer.putShort(tree.treeNr)
            serializeBuffer.putShort(tree.componentId)
            serializeBuffer.putShort(tree.sendComponentId)
            serializeBuffer.putShort(tree.receiveComponentId)
            serializeBuffer.putShort(tree.conflictComponentId)
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
//    val hexString =
//        "02 01 58 00 64 00 01 00 04 00 b8 0b bb 34 56 78 ab cd ef ee e8 03 bb 34 56 78 ab cd ef ee d0 07 bb 34 56 78 ab cd ef ee a0 0f bb 34 56 78 ab cd ef ee 04 00 00 00 b8 0b 00 00 e8 03 00 00 01 00 e8 03 b8 0b 00 00 00 00 02 00 a0 0f 00 00 00 00 00 00 03 00 d0 07 00 00 e8 03 00 00"
    val data = hexString.windowed(2, 3).map { it.trim().lowercase().toInt(16) }
    var crc = 0xFFFF
    for (item in data) {
        val index = (crc xor (item and 0xFF)) and 0xFF
        crc = (crc ushr 8) xor fcstab[index]
        println(
            "data: ${String.format("%02X ", item)},fcs: ${String.format("%02X ", crc)}" +
                    "fcsIndex: ${String.format("%02X ", index)}," +
                    "tab: ${String.format("%02X ", fcstab[index])}"
        )
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