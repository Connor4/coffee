package com.inno.serialport.function

import com.inno.serialport.bean.ParityType
import com.inno.serialport.bean.ProductInfo
import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.bean.StopBits
import com.inno.serialport.bean.fcstab
import com.inno.serialport.core.SerialPort
import com.inno.serialport.core.SerialPortManager
import kotlinx.serialization.json.Json
import java.nio.ByteBuffer
import java.nio.ByteOrder

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
        open()
    }

    override fun send(command: String) {
//        serialPort?.setRTS(true)
//        serialPort?.setRTS(false)
        val serializeProductInfo = serializeProductInfo(command)
        val crc = calculateCRC(serializeProductInfo)
        val buffer = ByteBuffer.allocate(serializeProductInfo.size + 2)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(serializeProductInfo)
        buffer.putShort(crc.toShort())

        val escapeData = escapeData(buffer.array())
        SerialPortManager.writeToSerialPort(mSerialPort, escapeData)

        val hexString = toHexString(escapeData)
        println("hexString: $hexString")
    }

    override fun receive(): PullBufInfo {
        var receivedData: PullBufInfo? = null
        SerialPortManager.readFromSerialPort(mSerialPort, onSuccess = { buffer, _ ->
            // already checked bytesRead
            receivedData = parsePullBuffInfo(buffer)
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

    private fun parsePullBuffInfo(data: ByteArray): PullBufInfo {
        // 解析ID（前两个字节）
        val id = ((data[0].toInt() and 0xFF) or ((data[1].toInt() and 0xFF) shl 8))
        // 解析PollBuf（后16个字节）
        val pullBuf = data.sliceArray(2 until 18)
        return PullBufInfo(id, pullBuf)
    }

    private fun serializeProductInfo(command: String): ByteArray {
        val productInfo = Json.decodeFromString<ProductInfo>(command)
        val buffer = ByteBuffer.allocate(1024)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        buffer.putShort(productInfo.productId)
        buffer.putShort(productInfo.componentList.componentNum)
        for (component in productInfo.componentList.singleComponent) {
            buffer.putShort(component.componentId)
            for (dosage in component.dosage) {
                buffer.putShort(dosage)
            }
        }

        buffer.putShort(productInfo.treeList.treeLen)
        for (tree in productInfo.treeList.singleTree) {
            buffer.putShort(tree.treeNr)
            buffer.putShort(tree.componentId)
            buffer.putShort(tree.sendComponentId)
            buffer.putShort(tree.receiveComponentId)
            buffer.putShort(tree.conflictComponentId)
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
        val buffer = ByteBuffer.allocate(data.size * 2)
        for (b in data) {
            when (b) {
                0x7E.toByte() -> {
                    buffer.put(0x7D.toByte())
                    buffer.put(0x5E.toByte())
                }

                0x7D.toByte() -> {
                    buffer.put(0x7D.toByte())
                    buffer.put(0x5D.toByte())
                }

                else -> buffer.put(b)
            }
        }
        buffer.flip()
        val result = ByteArray(buffer.limit())
        buffer.get(result)
        return result
    }

    private fun toHexString(data: ByteArray): String {
        val sb = StringBuilder()
        for (b in data) {
            sb.append(String.format("%02X ", b))
        }
        return sb.toString().trim()
    }

//    override fun parseFrame(frame: ByteArray): String {
//        if (frame[0] != 0x7E.toByte() || frame[frame.size - 1] != 0x7E.toByte()) {
//            Logger.e(TAG, "Invalid frame format")
//            return SerialErrorType.FRAME_FLAG_ILLEGAL.errorMsg
//        }
//        val address = frame[1].toInt() and 0xFF
//        val control = frame[2].toInt() and 0xFF
//
//        val length = ByteBuffer.wrap(frame, 3, 2).short.toInt() and 0xFFFF
//        val command = frame[5].toInt() and 0xFF
//        val payload = frame.sliceArray(6 until 6 + length - 1)
//
//        val receivedCRC = ByteBuffer.wrap(frame, 6 + length, 2).short.toInt() and 0xFFFF
//        val dataToCheckCRC = frame.sliceArray(0 until 6 + length)
//
//        val calculatedCRC = calculateCRC(dataToCheckCRC)
//        if (receivedCRC != calculatedCRC) {
//            Logger.e(TAG, "CRC check failed")
//            return SerialErrorType.CRC_CHECK_FAILED.errorMsg
//        }
//
//        Logger.d(
//            TAG, "Address: $address, Control: $control, Command: $command" +
//                    "Payload: ${payload.contentToString()}"
//        )
//        // 示例帧 (需要替换为实际读取的帧)
////        val frame = byteArrayOf(0x7E, 0x01, 0x01, 0x00, 0x03, 0x01, 0x02, 0x03, 0x00, 0x00, 0x7E)
////        parseFrame(frame)
//        return payload.contentToString()
//    }

}