package com.inno.serialport.function.driver

import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
import com.inno.serialport.annotation.DATA_BITS_8
import com.inno.serialport.annotation.PARITY_NONE
import com.inno.serialport.annotation.STOP_BITS_1
import com.inno.serialport.core.SerialPort
import com.inno.serialport.utilities.FRAME_CMD_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_CMD_INDEX_LOW
import com.inno.serialport.utilities.FRAME_CONTENT_START_INDEX
import com.inno.serialport.utilities.FRAME_DATA_START_INDEX
import com.inno.serialport.utilities.FRAME_FLAG_INDEX
import com.inno.serialport.utilities.FRAME_LENGTH_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_LENGTH_INDEX_LOW
import com.inno.serialport.utilities.HEART_BEAT_COMMAND
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.fcstab
import com.inno.serialport.utilities.statusenum.SerialErrorTypeEnum
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RS485Driver(
    devicePath: String = "/dev/ttyS4",
    baudRate: Int = 115200,
    dataBits: Int = DATA_BITS_8,
    stopBits: Int = STOP_BITS_1,
    parity: Int = PARITY_NONE,
    flags: Int = 0x0002 or 0x0100 or 0x0800, // O_RDWR | O_NOCTTY | O_NONBLOC
) : IDriver {

    companion object {
        private const val TAG = "RS485Driver"
        private const val MINIMUM_FRAME_PACK_SIZE = 12
        private const val FRAME_FLAG = 0x7E.toByte()
        private const val FRAME_CONTROL = 0X1.toByte()
        private const val FD = 0x5D.toByte()
        private const val FE = 0x5E.toByte()
        private const val SE = 0X7E.toByte()
        private const val SD = 0x7D.toByte()
    }

    private val serialPort: SerialPort = SerialPort.Builder()
        .portName(devicePath)
        .baudRate(baudRate)
        .dataBits(dataBits)
        .stopBits(stopBits)
        .parity(parity)
        .flag(flags)
        .build()

    override fun openSerialPort() {
        serialPort.openSerialPort()
    }

    override fun closeSerialPort() {
        serialPort.closeSerialPort()
    }

    override fun readFromSerialPort(
        onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: (type: SerialErrorTypeEnum) -> Unit,
    ) {
        val buffer = ByteArray(serialPort.portFrameSize)
        try {
            val bytesRead = serialPort.mFileInputStream?.read(buffer)
            when {
                bytesRead != null && bytesRead > 0 -> {
                    onSuccess(buffer, bytesRead)
                }
                else -> {
                    onFailure(SerialErrorTypeEnum.READ_FAIL)
                }
            }
        } catch (e: IOException) {
            Logger.e(TAG, "readFromSerialPort Exception $e")
            onFailure(SerialErrorTypeEnum.IO_EXCEPTION)
            closeSerialPort()
            openSerialPort()
        }
    }

    override fun writeToSerialPort(frame: ByteArray) {
        try {
            serialPort.mFileOutputStream?.write(frame)
        } catch (e: IOException) {
            Logger.e(TAG, "writeToSerialPort Exception $e")
        }
    }

    override fun send(command: Short, infoSize: Int, address: Byte, commandInfo: ByteArray) {
        // 6 = addr1 + control1 + len2 + cmd2
        val contentSize = infoSize + 6
        val contentBuffer = ByteBuffer.allocate(contentSize)
        contentBuffer.order(ByteOrder.LITTLE_ENDIAN)
        contentBuffer.put(address)
        contentBuffer.put(FRAME_CONTROL)
        // length = infoSize + 2length
        contentBuffer.putShort((infoSize + 2).toShort())
        // cmd
        contentBuffer.putShort(command)
        contentBuffer.put(commandInfo)
        // crc
        val crc = calculateCRC(contentBuffer)

        // 2 = crc2
        val commandSize = contentSize + 2
        val commandBuffer = ByteBuffer.allocate(commandSize)
        commandBuffer.order(ByteOrder.LITTLE_ENDIAN)
        commandBuffer.clear()
        commandBuffer.put(contentBuffer.array())
        commandBuffer.putShort(crc)
        val escapeData = escapeData(commandBuffer.array())
        val escapeSize = escapeData.size

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
        Logger.lengthy(TAG, "packFrame: ${packFrame.toHexString()}")

        writeToSerialPort(packFrame)
    }

    override fun receive(): List<PullBufInfo> {
        val receivedData = mutableListOf<PullBufInfo>()
        readFromSerialPort(
            onSuccess = { buffer, _ ->
                val multiInfo = slicePullInfo(buffer)
                if (multiInfo.isEmpty()) {
                    receivedData.add(PullBufInfo(command = SerialErrorTypeEnum.FRAME_FORMAT_ILLEGAL
                        .value))
                } else {
                    for (info in multiInfo) {
                        val bufInfo = validPullInfo(info) ?: parsePullBuffInfo(info)
                        receivedData.add(bufInfo)
                    }
                }
            },
            onFailure = {
                receivedData.add(PullBufInfo(command = it.value))
            },
        )
        return receivedData
    }

    override fun heartbeat() {
        writeToSerialPort(HEART_BEAT_COMMAND)
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
                SE -> {
                    buffer.add(SD)
                    buffer.add(FE)
                }

                SD -> {
                    buffer.add(SD)
                    buffer.add(FD)
                }

                else -> buffer.add(b)
            }
        }
        return buffer.toByteArray()
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

    private fun parsePullBuffInfo(buffer: ByteArray): PullBufInfo {
        val length = ((buffer[FRAME_LENGTH_INDEX_HIGH].toInt() and 0xFF) shl 8) or
                (buffer[FRAME_LENGTH_INDEX_LOW].toInt() and 0xFF)
        val command = ((buffer[FRAME_CMD_INDEX_HIGH].toInt() and 0xFF) shl 8) or
                (buffer[FRAME_CMD_INDEX_LOW].toInt() and 0xFF)
        val content = buffer.sliceArray(FRAME_CONTENT_START_INDEX until buffer.size - 3)
        return PullBufInfo(length, command.toShort(), content)
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
                    val escapeReceivedData = escapeReceivedData(info)
                    multiPullInfo.add(escapeReceivedData)
                    Logger.lengthy(TAG, "slicePullInfo: ${escapeReceivedData.toHexString()}")
                    start = -1
                }
            }
        }
        return multiPullInfo
    }

}