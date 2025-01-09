package com.inno.coffee.function

import com.inno.coffee.utilities.DISPLAY_COLOR_BLUE
import com.inno.coffee.utilities.DISPLAY_COLOR_GREEN
import com.inno.coffee.utilities.DISPLAY_COLOR_MIX
import com.inno.coffee.utilities.DISPLAY_COLOR_OFF
import com.inno.coffee.utilities.DISPLAY_COLOR_ORANGE
import com.inno.coffee.utilities.DISPLAY_COLOR_PURPLE
import com.inno.coffee.utilities.DISPLAY_COLOR_RED
import com.inno.coffee.utilities.DISPLAY_COLOR_WHITE
import com.inno.coffee.utilities.DISPLAY_COLOR_YELLOW
import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.utilities.FRAME_ADDRESS_3
import com.inno.serialport.utilities.FRAME_ADDRESS_4
import com.inno.serialport.utilities.FRONT_GRADIENT_COLOR_ID
import com.inno.serialport.utilities.FRONT_SINGLE_COLOR_ID
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

    fun sendFrontColor(colorName: Int) {
        when (colorName) {
            DISPLAY_COLOR_OFF -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0, 0, 0)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0, 0, 0)
            }
            DISPLAY_COLOR_WHITE -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0xFF, 0xFF, 0xFF)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0xFF, 0xFF, 0xFF)
            }
            DISPLAY_COLOR_PURPLE -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0, 0x80, 0x80)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0, 0x80, 0x80)
            }
            DISPLAY_COLOR_YELLOW -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0xFF, 0xFF, 0)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0xFF, 0xFF, 0)
            }
            DISPLAY_COLOR_ORANGE -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0xA5, 0XFF, 0)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0xA5, 0XFF, 0)
            }
            DISPLAY_COLOR_RED -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0, 0xFF, 0)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0, 0xFF, 0)
            }
            DISPLAY_COLOR_GREEN -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0xFF, 0, 0)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0xFF, 0, 0)
            }
            DISPLAY_COLOR_BLUE -> {
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3, 0, 0, 0xFF)
                sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4, 0, 0, 0xFF)
            }
            DISPLAY_COLOR_MIX -> {
                sendFrontColorCommand(FRONT_GRADIENT_COLOR_ID, FRAME_ADDRESS_3, 0x32, 0, 0xFF, 0x00,
                    0x00, 0x00, 0xFF, 0x00)
                sendFrontColorCommand(FRONT_GRADIENT_COLOR_ID, FRAME_ADDRESS_4, 0x32, 0, 0xFF, 0x00,
                    0x00, 0x00, 0xFF, 0x00)
            }
        }
    }

    private fun sendFrontColorCommand(commandId: Short, address: Byte, vararg value: Short) {
        scope.launch {
            val byteArray = shortArrayConvertByte(value)
            SerialPortDataManager.instance.sendFrontColorCommand(commandId, byteArray.size,
                address, byteArray)
        }
    }

}