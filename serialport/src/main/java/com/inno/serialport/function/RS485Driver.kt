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
        private const val MAX_COMPONENT = 64
        private const val MAX_TREE = 64

        // ComponentList = 2 + SingleComponent: (2 + 2 * 4)*MaxComponent
        // TreeList = 2 + SingleTree: (2 * 5)*MaxTree
        // CAPACITY = productId + ComponentList + TreeList
        private const val COMMAND_BUFFER_CAPACITY = 6 + 10 * MAX_COMPONENT + 10 * MAX_TREE
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

    init {
        open()
    }

    override fun send(command: String) {
//        serialPort?.setRTS(true)
//        serialPort?.setRTS(false)
        val totalBufferSize = (COMMAND_BUFFER_CAPACITY + 8)
        val buffer = ByteBuffer.allocate(totalBufferSize)
        val serializeProductInfo = serializeProductInfo(command)
        val crc = calculateCRC(serializeProductInfo)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(FRAME_ADDRESS)
        buffer.put(FRAME_CONTROL)
        // length
        buffer.putShort((COMMAND_BUFFER_CAPACITY * 8).toShort())
        // cmd
        buffer.putShort(0x64.toShort())
        buffer.put(serializeProductInfo)
        buffer.putShort(crc.toShort())
        val escapeData = escapeData(buffer.array())

        val packBuffer = ByteBuffer.allocate(totalBufferSize + 16)
        packBuffer.order(ByteOrder.LITTLE_ENDIAN)
        packBuffer.put(FRAME_FLAG)
        packBuffer.put(escapeData)
        packBuffer.put(FRAME_FLAG)
        packBuffer.flip()
        val packFrame = ByteArray(packBuffer.limit())
        packBuffer.get(packFrame)

        SerialPortManager.writeToSerialPort(mSerialPort, packFrame)
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