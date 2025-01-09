package com.inno.coffee.function

import com.inno.serialport.function.SerialPortDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * 1. send optional param from index 0
 * TODO
 * 1. need to wait for response
 * 2. combine all commands into here
 */
object CommandControlManager {
    private const val TAG = "CommandControlManager"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun intArrayConvertByte(commandInfo: IntArray): ByteArray {
        val byteArray = ByteArray(commandInfo.size * 2)

        commandInfo.forEachIndexed { index, value ->
            byteArray[index * 2 + 1] = (value shr 8).toByte()
            byteArray[index * 2] = (value and 0xFF).toByte()
        }
        return byteArray
    }

    private fun shortArrayConvertByte(commandInfo: ShortArray): ByteArray {
        val byteArray = ByteArray(commandInfo.size)
        commandInfo.forEachIndexed { index, value ->
            byteArray[index] = value.toByte()
        }
        return byteArray
    }

    fun sendTestCommand(commandId: Short, vararg value: Int) {
        scope.launch {
            val byteArray = intArrayConvertByte(value)
            SerialPortDataManager.instance.sendCommand(commandId, byteArray.size, byteArray)
        }
    }

    fun sendFrontColorCommand(commandId: Short, address: Byte, vararg value: Short) {
        scope.launch {
            val byteArray = shortArrayConvertByte(value)
            SerialPortDataManager.instance.sendCommandSpecifyAddress(commandId, byteArray.size,
                address, byteArray)
        }
    }

}