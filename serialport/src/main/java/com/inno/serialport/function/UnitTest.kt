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

//private const val MAX_BYTEARRAY_SIZE = 265 // 256 + 9
private const val MAX_COMPONENT = 4
private const val MAX_TREE = 4

// ComponentList = 2 + SingleComponent: (2 + 2 * 4)*MaxComponent
// TreeList = 2 + SingleTree: (2 * 5)*MaxTree
// CAPACITY = productId + ComponentList + TreeList
private const val COMMAND_BUFFER_CAPACITY = 6 + 10 * MAX_COMPONENT + 10 * MAX_TREE
private const val FRAME_FLAG = 0x7E.toByte()
private const val FRAME_ADDRESS = 0x2.toByte()
private const val FRAME_CONTROL = 0X1.toByte()

fun main() {
    val info = createInfo()
    println("info: $info")
    val command = Json.encodeToString(info)
    println("string: $command")

    val serializeProductInfo = serializeProductInfo(command)
    println("serialize: " + toHexString(serializeProductInfo))
    val totalBufferSize = (COMMAND_BUFFER_CAPACITY + 8)
    val buffer = ByteBuffer.allocate(totalBufferSize)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.put(FRAME_ADDRESS)
    buffer.put(FRAME_CONTROL)
    // length
    buffer.putShort((COMMAND_BUFFER_CAPACITY + 2).toShort())
    println("size: ${COMMAND_BUFFER_CAPACITY + 2}")
    // cmd
    buffer.putShort(0x64.toShort())
    buffer.put(serializeProductInfo)
    buffer.putShort(calculateCRC1(buffer.array()))
    println("pack:      ${buffer.toHexString()}")

    val packBuffer = ByteBuffer.allocate(totalBufferSize + 16)
    packBuffer.order(ByteOrder.LITTLE_ENDIAN)
    packBuffer.put(FRAME_FLAG)
    val escapeData = escapeData(buffer.array())
    packBuffer.put(escapeData)
    packBuffer.put(FRAME_FLAG)
    packBuffer.flip()
    val packFrame = ByteArray(packBuffer.limit())
    packBuffer.get(packFrame)

    println("final:     " + packBuffer.toHexString())
    val value =
        "02 01 58 00 64 00 01 00 04 00 B8 0B BB 34 56 78 AB CD EF EE E8 03 BB 34 56 78 AB CD EF EE D0 07 BB 34 56 78 AB CD EF EE A0 0F BB 34 56 78 AB CD EF EE 04 00 00 00 8B 0B 00 00 E8 03 00 00 01 00 E8 03 8B 0B 00 00 00 00 02 00 A0 0F 00 00 00 00 00 00 03 00 D0 07 00 00 E8 03 00 00 4F AD"
    formatValue(value)
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
                        0x34bb.toShort(),
                        0x7856.toShort(),
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

private fun serializeProductInfo(command: String): ByteArray {
    val productInfo = Json.decodeFromString<ProductInfo>(command)
    val buffer = ByteBuffer.allocate(COMMAND_BUFFER_CAPACITY)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putShort(productInfo.productId)
    buffer.putShort(productInfo.componentList.componentNum)

    val componentSize = productInfo.componentList.singleComponent.size
    for (i in 0 until MAX_COMPONENT) {
        if (i < componentSize) {
            val singleComponent = productInfo.componentList.singleComponent[i]
            buffer.putShort(singleComponent.componentId)
            for (dosage in singleComponent.dosage) {
                buffer.putShort(dosage)
            }
        } else {
            buffer.putShort(0)
        }
    }

    buffer.putShort(productInfo.treeList.treeLen)
    val treeSize = productInfo.treeList.singleTree.size
    for (i in 0 until MAX_TREE) {
        if (i < treeSize) {
            val tree = productInfo.treeList.singleTree[i]
            buffer.putShort(tree.treeNr)
            buffer.putShort(tree.componentId)
            buffer.putShort(tree.sendComponentId)
            buffer.putShort(tree.receiveComponentId)
            buffer.putShort(tree.conflictComponentId)
        } else {
            buffer.putShort(0)
        }
    }

    buffer.flip()
    val result = ByteArray(buffer.limit())
    buffer.get(result)
    return result
}

private fun calculateCRC1(data: ByteArray): Short {
    var fcs = 0xFFFF
    var index = 0
    var remaining = data.size
    while (remaining > 0) {
        fcs = (fcs ushr 8) xor fcstab[(fcs xor data[index].toInt()) and 0xFF]
        index++
        remaining--
    }
    return fcs.toShort()
}

private fun calculateCRC(data: ByteArray): Short {
    var crc = 0xFFFF
    for (b in data) {
        crc = (crc ushr 8) xor fcstab[(crc xor (b.toInt() and 0xFF)) and 0xFF]
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