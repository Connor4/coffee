package com.inno.serialport.function.driver

import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
import com.inno.serialport.annotation.DATA_BITS_8
import com.inno.serialport.annotation.PARITY_NONE
import com.inno.serialport.annotation.STOP_BITS_1
import com.inno.serialport.core.SerialPort
import com.inno.serialport.core.SerialPortManager
import com.inno.serialport.utilities.FRAME_CMD_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_CMD_INDEX_LOW
import com.inno.serialport.utilities.FRAME_CONTENT_START_INDEX
import com.inno.serialport.utilities.FRAME_DATA_START_INDEX
import com.inno.serialport.utilities.FRAME_FLAG_INDEX
import com.inno.serialport.utilities.FRAME_LENGTH_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_LENGTH_INDEX_LOW
import com.inno.serialport.utilities.HEART_BEAT_COMMAND
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.SerialErrorTypeEnum
import com.inno.serialport.utilities.fcstab
import com.inno.serialport.utilities.profile.ProductProfile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class RS485Driver : IDriver {

    companion object {
        private const val TAG = "RS485Driver"
        private const val DEVICE_PATH = "/dev/ttyS9"
        private const val BAUD_RATE = 115200
        private const val DATA_BITES = DATA_BITS_8
        private const val STOP_BITS = STOP_BITS_1
        private const val PARITY = PARITY_NONE
        private const val FLAGS = 0x0002 or 0x0100 or 0x0800 // O_RDWR | O_NOCTTY | O_NONBLOC
        // pull info12(flag1 + addr1 + control1 + len2 + cmd2 + data2 + crc2 + flag1)
        private const val MINIMUM_FRAME_PACK_SIZE = 12
        private const val MAX_COMPONENT: Short = 3
        // ProductProfile = id2 + preFlush2 + postFlush2 + ComponentProfileList: num2 + MAX_COMPONENT * (comid2 + 2*para6)
        private const val COMMAND_BUFFER_CAPACITY = 8 + 14 * MAX_COMPONENT
        // 6 = addr1 + control1 + len2 + cmd2
        private const val CONTENT_BUFFER_CAPACITY = COMMAND_BUFFER_CAPACITY + 6
        // 8 = addr1 + control1 + len2 + cmd2 + crc2
        private const val TOTAL_BUFFER_SIZE = COMMAND_BUFFER_CAPACITY + 8
        // why there is 16?
        private const val PACK_BUFFER_SIZE = TOTAL_BUFFER_SIZE + 16
        private const val FRAME_FLAG = 0x7E.toByte()
        private const val FRAME_ADDRESS = 0x2.toByte()
        private const val FRAME_CONTROL = 0X1.toByte()
    }

    private val serialPort: SerialPort = SerialPort.Builder()
        .portName(DEVICE_PATH)
        .baudRate(BAUD_RATE)
        .dataBits(DATA_BITES)
        .stopBits(STOP_BITS)
        .parity(PARITY)
        .flag(FLAGS)
        .build()
    private val lock = ReentrantLock()
    private val serializeBuffer = ByteBuffer.allocate(COMMAND_BUFFER_CAPACITY)
    private val contentBuffer = ByteBuffer.allocate(CONTENT_BUFFER_CAPACITY)
    private val commandBuffer = ByteBuffer.allocate(TOTAL_BUFFER_SIZE)
    private val packBuffer = ByteBuffer.allocate(PACK_BUFFER_SIZE)

    init {
        contentBuffer.order(ByteOrder.LITTLE_ENDIAN)
        commandBuffer.order(ByteOrder.LITTLE_ENDIAN)
        packBuffer.order(ByteOrder.LITTLE_ENDIAN)
        serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)
    }

    override fun send(command: Short, productProfile: ProductProfile) {
        lock.withLock {
            val serializeProductInfo = serializeProductInfo(productProfile)
            contentBuffer.clear()
            contentBuffer.put(FRAME_ADDRESS)
            contentBuffer.put(FRAME_CONTROL)
            // length
            contentBuffer.putShort((COMMAND_BUFFER_CAPACITY + 2).toShort())
            // cmd
            contentBuffer.putShort(command)
            contentBuffer.put(serializeProductInfo)
            // crc
            val crc = calculateCRC(contentBuffer)
            commandBuffer.clear()
            commandBuffer.put(contentBuffer.array())
            commandBuffer.putShort(crc)

            packBuffer.clear()
            packBuffer.put(FRAME_FLAG)
            val escapeData = escapeData(commandBuffer.array())
            packBuffer.put(escapeData)
            packBuffer.put(FRAME_FLAG)
            packBuffer.flip()
            val packFrame = ByteArray(packBuffer.limit())
            SerialPortManager.writeToSerialPort(serialPort, packFrame)
        }
    }

    fun sendHeartBeat() {
        SerialPortManager.writeToSerialPort(serialPort, HEART_BEAT_COMMAND)
    }

    override suspend fun receive(): PullBufInfo {
        var receivedData: PullBufInfo? = null
        SerialPortManager.readFromSerialPort(serialPort, onSuccess = { buffer, _ ->
            val multiInfo = slicePullInfo(buffer)
            for (info in multiInfo) {
                receivedData = validPullInfo(info) ?: parsePullBuffInfo(info)
            }
        }, onFailure = {
            receivedData = PullBufInfo(command = it.value)
        })
        return receivedData!!
    }

    override fun open() {
        SerialPortManager.open(serialPort)
    }

    override fun close() {
        SerialPortManager.close(serialPort)
    }

    private fun serializeProductInfo(productProfile: ProductProfile): ByteArray {
        serializeBuffer.clear()
        serializeBuffer.putShort(productProfile.productId)
        serializeBuffer.putShort(productProfile.preFlush)
        serializeBuffer.putShort(productProfile.postFlush)
        serializeBuffer.putShort(productProfile.componentProfileList.componentNum)

        val componentSize = productProfile.componentProfileList.componentList.size
        for (i in 0 until MAX_COMPONENT) {
            if (i < componentSize) {
                val componentProfile = productProfile.componentProfileList.componentList[i]
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

    private fun parsePullBuffInfo(buffer: ByteArray): PullBufInfo {
        val length = ((buffer[FRAME_LENGTH_INDEX_LOW].toInt() and 0xFF) shl 8) or
                (buffer[FRAME_LENGTH_INDEX_HIGH].toInt() and 0xFF)
        val command = ((buffer[FRAME_CMD_INDEX_LOW].toInt() and 0xFF) shl 8) or
                (buffer[FRAME_CMD_INDEX_HIGH].toInt() and 0xFF)
        val content = buffer.sliceArray(FRAME_CONTENT_START_INDEX until buffer.size - 2)
        return PullBufInfo(length, command, content)
    }

    private fun validPullInfo(buffer: ByteArray): PullBufInfo? {
        val size = buffer.size
        if (size < MINIMUM_FRAME_PACK_SIZE) {
            Logger.e(TAG, "frame size error")
            return PullBufInfo(command = SerialErrorTypeEnum.FRAME_SIZE_ERROR.value)
        }
        if (buffer[FRAME_FLAG_INDEX] != FRAME_FLAG || buffer[size - 1] != FRAME_FLAG) {
            Logger.e(TAG, "Invalid packet header or footer")
            return PullBufInfo(command = SerialErrorTypeEnum.FRAME_FORMAT_ILLEGAL.value)
        }

        val receivedCRC = ((buffer[size - 2].toUByte().toInt() shl 8) or (buffer[size - 3].toUByte()
            .toInt())).toShort()
        Logger.lengthy(TAG,
            "buffer: ${buffer.toHexString()}, Received CRC: ${receivedCRC.toHexString()}")

        // exclude frame flag and crc
        val payload = buffer.sliceArray(FRAME_DATA_START_INDEX until size - 3)
        val payloadBuffer = ByteBuffer.allocate(payload.size)
        payloadBuffer.put(payload)
        payloadBuffer.flip()
        Logger.lengthy(TAG,
            "payload: ${payload.toHexString()}， payloadBuffer: ${payloadBuffer.toHexString()}")

        val calculatedCRC = calculateCRC(payloadBuffer)
        if (receivedCRC != calculatedCRC) {
            Logger.e(TAG, "CRC check failed: received ${receivedCRC.toHexString()}," +
                    " calculated ${calculatedCRC.toHexString()}")
            return PullBufInfo(command = SerialErrorTypeEnum.CRC_CHECK_FAILED.value)
        }
        return null
    }

    private fun slicePullInfo(buffer: ByteArray): List<ByteArray> {
        Logger.lengthy(TAG, "slicePullInfo() called with: buffer = ${buffer.toHexString()}")
        val multiPullInfo = mutableListOf<ByteArray>()
        var start = -1
        for (i in buffer.indices) {
            if (buffer[i] == FRAME_FLAG) {
                if (start == -1) {
                    start = i
                } else {
                    val info = buffer.sliceArray(start until i + 1)
                    multiPullInfo.add(info)
                    Logger.lengthy(TAG, "slicePullInfo: ${info.toHexString()}")
                    start = -1
                }
            }
        }
        return multiPullInfo
    }

}