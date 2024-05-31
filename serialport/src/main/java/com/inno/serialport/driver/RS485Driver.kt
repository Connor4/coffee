package com.inno.serialport.driver

import com.inno.common.utils.Logger
import com.inno.serialport.core.SerialPort
import com.inno.serialport.core.SerialPortManager
import java.nio.ByteBuffer

class RS485Driver(
    devicePath: String,
    baudRate: Int,
    dataBits: Int,
    stopBits: Int,
    parity: Int,
    flag: Int
) : IDriver {

    companion object {
        private const val TAG = "RS485Driver"
    }

    private val mSerialPort: SerialPort = SerialPort.Builder()
        .portName(devicePath)
        .baudRate(baudRate)
        .dataBits(dataBits)
        .stopBits(stopBits)
        .parity(parity)
        .flag(flag)
        .build()

    init {
        SerialPortManager.open(mSerialPort)
    }

    override fun send(frame: ByteArray) {
//        serialPort?.setRTS(true)
        SerialPortManager.writeData(mSerialPort, frame)
//        serialPort?.setRTS(false)
    }

    override fun receive(): ByteArray? {
        var receivedData: ByteArray? = null
        SerialPortManager.readData(mSerialPort, onSuccess = { buffer, size ->
            if (size > 0) {
                receivedData = buffer.copyOf(size)
            }
        }, onFailure = {
            Logger.e(TAG, "receive failed")
        })
//        parseFrame(receivedData)
        return receivedData
    }

    override fun parseFrame(frame: ByteArray) {
        if (frame[0] != 0x7E.toByte() || frame[frame.size - 1] != 0x7E.toByte()) {
            Logger.e(TAG, "Invalid frame format")
            // TODO 处理异常逻辑
            throw IllegalArgumentException("Invalid frame format")
        }
        val address = frame[1].toInt() and 0xFF
        val control = frame[2].toInt() and 0xFF

        val length = ByteBuffer.wrap(frame, 3, 2).short.toInt() and 0xFFFF
        val command = frame[5].toInt() and 0xFF
        val payload = frame.sliceArray(6 until 6 + length - 1)

        val receivedCRC = ByteBuffer.wrap(frame, 6 + length, 2).short.toInt() and 0xFFFF
        val dataToCheckCRC = frame.sliceArray(0 until 6 + length)

        val calculatedCRC = calculateCRC(dataToCheckCRC)
        if (receivedCRC != calculatedCRC) {
            Logger.e(TAG, "CRC check failed")
            // TODO 处理异常逻辑
            throw IllegalArgumentException("CRC check failed.")
        }

        Logger.d(
            TAG, "Address: $address, Control: $control, Command: $command" +
                    "Payload: ${payload.contentToString()}"
        )
        // 示例帧 (需要替换为实际读取的帧)
//        val frame = byteArrayOf(0x7E, 0x01, 0x01, 0x00, 0x03, 0x01, 0x02, 0x03, 0x00, 0x00, 0x7E)
//        parseFrame(frame)

    }

    override fun close() {
        SerialPortManager.close(mSerialPort)
    }

    /**
     * CRC校验函数
     */
    private fun calculateCRC(data: ByteArray): Int {
        var crc = 0xFFFF
        for (b in data) {
            crc = crc xor (b.toInt() and 0xFF)
            for (i in 0..7) {
                crc = if (crc and 1 != 0) {
                    (crc shr 1) xor 0xA001
                } else {
                    crc shr 1
                }
            }
        }
        return crc and 0xFFFF
    }

}