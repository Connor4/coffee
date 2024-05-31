package com.inno.serialport.driver

import com.inno.common.utils.Logger
import com.inno.serialport.bean.ParityType
import com.inno.serialport.bean.SerialErrorType
import com.inno.serialport.bean.StopBits
import com.inno.serialport.core.SerialPort
import com.inno.serialport.core.SerialPortManager
import java.nio.ByteBuffer

class RS485Driver : IDriver {

    companion object {
        private const val TAG = "RS485Driver"
        private const val DEVICE_PATH = "/dev/ttyS0"
        private const val BAUD_RATE = 115200
        private const val DATA_BITES = 8
        private val STOP_BITS = StopBits.SINGLE.value
        private val PARITY = ParityType.NONE_PARITY.value
        private const val FLAGS = 0x0002 or 0x0100 or 0x0800 // O_RDWR | O_NOCTTY | O_NONBLOC
        private const val MAX_BYTEARRAY_SIZE = 265 // 256 + 9
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

    init {
        SerialPortManager.open(mSerialPort)
    }

    override fun send(frame: ByteArray) {
//        serialPort?.setRTS(true)
        SerialPortManager.writeData(mSerialPort, frame)
//        serialPort?.setRTS(false)
    }

    override fun receive(): String? {
        var receivedData: String? = null
        SerialPortManager.readData(mSerialPort, onSuccess = { buffer, _ ->
            // already checked bytesRead
            receivedData = parseFrame(buffer)
        }, onFailure = {
            receivedData = it.errorMsg
        })
        return receivedData
    }

    override fun parseFrame(frame: ByteArray): String {
        if (frame[0] != 0x7E.toByte() || frame[frame.size - 1] != 0x7E.toByte()) {
            Logger.e(TAG, "Invalid frame format")
            return SerialErrorType.FRAME_FLAG_ILLEGAL.errorMsg
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
            return SerialErrorType.CRC_CHECK_FAILED.errorMsg
        }

        Logger.d(
            TAG, "Address: $address, Control: $control, Command: $command" +
                    "Payload: ${payload.contentToString()}"
        )
        // 示例帧 (需要替换为实际读取的帧)
//        val frame = byteArrayOf(0x7E, 0x01, 0x01, 0x00, 0x03, 0x01, 0x02, 0x03, 0x00, 0x00, 0x7E)
//        parseFrame(frame)
        return payload.contentToString()
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