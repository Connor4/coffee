package com.inno.serialport.function

import android.util.Log
import com.inno.common.utils.Logger
import com.inno.serialport.bean.ParityType
import com.inno.serialport.bean.ProductInfo
import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.bean.SerialErrorType
import com.inno.serialport.bean.StopBits
import com.inno.serialport.bean.fcstab
import com.inno.serialport.core.SerialPort
import com.inno.serialport.core.SerialPortManager
import kotlinx.serialization.json.Json
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class RS485Driver : IDriver {

    companion object {
        private const val TAG = "RS485Driver"
        private const val DEVICE_PATH = "/dev/ttyS9"
        private const val BAUD_RATE = 115200
        private const val DATA_BITES = 8
        private val STOP_BITS = StopBits.SINGLE.value
        private val PARITY = ParityType.NONE_PARITY.value
        private const val FLAGS = 0x0002 or 0x0100 or 0x0800 // O_RDWR | O_NOCTTY | O_NONBLOC
        private val HEART_BEAT_COMMAND = byteArrayOf(
            0x7e.toByte(), 0x02.toByte(), 0x01.toByte(), 0x02.toByte(),
            0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0xcc.toByte(),
            0x2b.toByte(), 0x7e.toByte())

        private const val MAX_BYTEARRAY_SIZE = 265 // 256 + 9
        private const val MAX_COMPONENT = 64
        private const val MAX_TREE = 64

        // ComponentList = 2 + SingleComponent: (2 + 2 * 4)*MaxComponent
        // TreeList = 2 + SingleTree: (2 * 5)*MaxTree
        // CAPACITY = productId + ComponentList + TreeList
        private const val COMMAND_BUFFER_CAPACITY = 6 + 10 * MAX_COMPONENT + 10 * MAX_TREE
        private const val TOTAL_BUFFER_SIZE = COMMAND_BUFFER_CAPACITY + 8
        private const val PACK_BUFFER_SIZE = TOTAL_BUFFER_SIZE + 16
        private const val FRAME_FLAG = 0x7E.toByte()
        private const val FRAME_ADDRESS = 0x2.toByte()
        private const val FRAME_CONTROL = 0X1.toByte()
    }

    private val mSerialPort: SerialPort = SerialPort.Builder()
        .portName(DEVICE_PATH)
        .baudRate(BAUD_RATE)
        .dataBits(DATA_BITES)
        .stopBits(STOP_BITS)
        .parity(PARITY)
        .flag(FLAGS)
        .portFrameSize(MAX_BYTEARRAY_SIZE)
        .build()
    private val lock = ReentrantLock()
    private val commandBuffer = ByteBuffer.allocate(TOTAL_BUFFER_SIZE)
    private val packBuffer = ByteBuffer.allocate(PACK_BUFFER_SIZE)
    private val serializeBuffer = ByteBuffer.allocate(COMMAND_BUFFER_CAPACITY)

    init {
        commandBuffer.order(ByteOrder.LITTLE_ENDIAN)
        packBuffer.order(ByteOrder.LITTLE_ENDIAN)
        serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)
    }

    override fun send(command: String) {
        lock.withLock {
            val serializeProductInfo = serializeProductInfo(command)
            commandBuffer.clear()
            commandBuffer.put(FRAME_ADDRESS)
            commandBuffer.put(FRAME_CONTROL)
            // length
            commandBuffer.putShort((COMMAND_BUFFER_CAPACITY + 2).toShort())
            // cmd
            commandBuffer.putShort(0x64.toShort())
            commandBuffer.put(serializeProductInfo)
            // crc
            val crc = calculateCRC(commandBuffer)
            commandBuffer.putShort(crc)
            // generate frame
            val escapeData = escapeData(commandBuffer.array())
            packBuffer.clear()
            packBuffer.put(FRAME_FLAG)
            packBuffer.put(escapeData)
            packBuffer.put(FRAME_FLAG)
            packBuffer.flip()
            val packFrame = ByteArray(packBuffer.limit())
            packBuffer.get(packFrame)

            SerialPortManager.writeToSerialPort(mSerialPort, packFrame)
        }
    }

    fun sendHeartBeat() {
        SerialPortManager.writeToSerialPort(mSerialPort, HEART_BEAT_COMMAND)
    }

    override suspend fun receive(): PullBufInfo {
        var receivedData: PullBufInfo? = null
        SerialPortManager.readFromSerialPort(mSerialPort, onSuccess = { buffer, _ ->
            // already checked bytesRead
            receivedData = checkPullInfo(buffer) ?: parsePullBuffInfo(buffer)
        }, onFailure = {
            receivedData = PullBufInfo(it.value, it.errorMsg.toByteArray())
        })
        return receivedData!!
    }

    override fun open() {
        SerialPortManager.open(mSerialPort)
    }

    override fun close() {
        SerialPortManager.close(mSerialPort)
    }

    private fun parsePullBuffInfo(buffer: ByteArray): PullBufInfo {
        // 解析ID（前两个字节）
        val id = ((buffer[0].toInt() and 0xFF) or ((buffer[1].toInt() and 0xFF) shl 8))
        // 解析PollBuf（后16个字节）
        val pullBuf = buffer.sliceArray(2 until 18)
        return PullBufInfo(id, pullBuf)
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
        val data = hexString.windowed(2, 3).map { it.trim().lowercase().toInt(16) }
        var crc = 0xFFFF
        for (item in data) {
            val index = (crc xor (item and 0xFF)) and 0xFF
            crc = (crc ushr 8) xor fcstab[index]
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

    private fun checkPullInfo(buffer: ByteArray): PullBufInfo? {
        val size = buffer.size
        if (size < 12 || buffer[0] != FRAME_FLAG || buffer[size - 1] != FRAME_FLAG) {
            Log.e(TAG, "Invalid packet header or footer")
            return PullBufInfo(SerialErrorType.FRAME_FORMAT_ILLEGAL.value, SerialErrorType
                .FRAME_FORMAT_ILLEGAL.errorMsg.toByteArray())
        }

        val receivedCRC = ((buffer[size - 2].toInt() and 0xFF) shl 8 or (buffer[size - 1].toInt()
                and 0xFF)).toShort()
        // exclude frame flag and crc
        val payload = buffer.sliceArray(1 until size - 3)
        val payloadBuffer = ByteBuffer.allocate(payload.size)
        payloadBuffer.put(payload)
        payloadBuffer.flip()
        val calculatedCRC = calculateCRC(payloadBuffer)
        if (receivedCRC != calculatedCRC) {
            Logger.e(TAG,
                "CRC check failed: received ${receivedCRC.toUShort()}, calculated ${calculatedCRC.toUShort()}")
            return PullBufInfo(SerialErrorType.CRC_CHECK_FAILED.value,
                SerialErrorType.CRC_CHECK_FAILED.errorMsg.toByteArray())
        }
        return null
    }

}