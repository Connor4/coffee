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
private const val MAX_COMPONENT = 2
private const val MAX_TREE = 2

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
    val infoString = Json.encodeToString(info)
    println("string: $infoString")

    val totalBufferSize = (COMMAND_BUFFER_CAPACITY + 8)
    val buffer = ByteBuffer.allocate(totalBufferSize)
    val serializeProductInfo = serializeProductInfo(infoString)
    println("serialize: " + toHexString(serializeProductInfo))
    val crc = calculateCRC(serializeProductInfo)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.put(FRAME_ADDRESS)
    buffer.put(FRAME_CONTROL)
    buffer.putShort((COMMAND_BUFFER_CAPACITY).toShort())
    println("size: ${COMMAND_BUFFER_CAPACITY}")
    buffer.putShort(0x64.toShort())
    buffer.put(serializeProductInfo)
    buffer.putShort(crc.toShort())
    println("after crc: " + buffer.toHexString())
    val escapeData = escapeData(buffer.array())
    println("escape:    " + toHexString(escapeData))

    val finalBuffer = ByteBuffer.allocate(totalBufferSize + 2)
    finalBuffer.order(ByteOrder.LITTLE_ENDIAN)
    finalBuffer.put(FRAME_FLAG)
    finalBuffer.put(escapeData)
    finalBuffer.put(FRAME_FLAG)
    finalBuffer.flip()
    val final = ByteArray(finalBuffer.limit())
    finalBuffer.get(final)

    println("final:     " + finalBuffer.toHexString())
}

fun createInfo(): ProductInfo {
    val productInfo = ProductInfo(
        productId = 0x01,
        componentList = ComponentList(
            componentNum = 2,
            singleComponent = listOf(
                SingleComponent(
                    componentId = 0x01,
                    dosage = shortArrayOf(0x34, 0x56, 0xAB, 0xEF)
                ),
                SingleComponent(
                    componentId = 0x02,
                    dosage = shortArrayOf(0x12, 0x78, 0xCD, 0x02)
                )
            )
        ),
        treeList = TreeList(
            treeLen = 2,
            singleTree = listOf(
                SingleTree(
                    treeNr = 0x01,
                    componentId = 0x12,
                    sendComponentId = 0x56,
                    receiveComponentId = 0xAB,
                    conflictComponentId = 0xEF
                ),
                SingleTree(
                    treeNr = 0x02,
                    componentId = 0x12,
                    sendComponentId = 0x56,
                    receiveComponentId = 0xAB,
                    conflictComponentId = 0xEF
                )
            )
        )
    )
    return productInfo
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

private fun calculateCRC(data: ByteArray): Int {
    var crc = 0xFFFF
    for (b in data) {
        crc = (crc shr 8) xor fcstab[(crc xor (b.toInt() and 0xFF)) and 0xFF]
    }
    return crc and 0xFFFF
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