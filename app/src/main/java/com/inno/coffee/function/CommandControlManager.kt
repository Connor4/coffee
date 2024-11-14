package com.inno.coffee.function

import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.utilities.COFFEE_INPUT_COMMAND_ID
import com.inno.serialport.utilities.COFFEE_OUTPUT_COMMAND_ID
import com.inno.serialport.utilities.MACHINE_PARAM_COMMAND_ID
import com.inno.serialport.utilities.STEAM_INPUT_COMMAND_ID
import com.inno.serialport.utilities.STEAM_OUTPUT_COMMAND_ID
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
            byteArray[index * 2] = (value shr 8).toByte()
            byteArray[index * 2 + 1] = (value and 0xFF).toByte()
        }
        return byteArray
    }

    fun sendMachineParams(
        leftBoilerTemp: Int, rightBoilerTemp: Int, coldRinse: Int, warmRinse: Int,
        groundsDrawerQuantity: Int, brewGroupLoadBalancing: Boolean, brewGroupPreHeating: Int,
        grinderPurgeFunction: Int, numberOfCyclesRinse: Int, steamBoilerPressure: Int,
        ntcCorrectionSteamLeft: Int, ntcCorrectionSteamRight: Int,
    ) {
        val reserve = 0
        val balance = if (brewGroupLoadBalancing) 1 else 0
        val commandInfo =
            intArrayOf(leftBoilerTemp, rightBoilerTemp, coldRinse, warmRinse, groundsDrawerQuantity,
                balance, brewGroupPreHeating, grinderPurgeFunction, reserve, numberOfCyclesRinse,
                steamBoilerPressure, ntcCorrectionSteamLeft, ntcCorrectionSteamRight, reserve,
                reserve, reserve)
        val byteArray = intArrayConvertByte(commandInfo)

        scope.launch {
            SerialPortDataManager.instance.sendCommand(MACHINE_PARAM_COMMAND_ID, 32, byteArray)
        }
    }

    fun getCoffeeInputs() {
        scope.launch {
            SerialPortDataManager.instance.sendCommand(COFFEE_INPUT_COMMAND_ID, 0, byteArrayOf())
        }
    }

    fun getSteamInputs() {
        scope.launch {
            SerialPortDataManager.instance.sendCommand(STEAM_INPUT_COMMAND_ID, 0, byteArrayOf())
        }
    }

    fun coffeeTestTurnOff() {
        scope.launch {
            val turnOff = byteArrayOf(0x2c, 0x01)
            SerialPortDataManager.instance.sendCommand(COFFEE_OUTPUT_COMMAND_ID, 2, turnOff)
        }
    }

    fun coffeeTest(vararg value: Int) {
        scope.launch {
            val byteArray = intArrayConvertByte(value)
            SerialPortDataManager.instance.sendCommand(COFFEE_OUTPUT_COMMAND_ID, 4, byteArray)
        }
    }

    fun steamTestTurnOff() {
        scope.launch {
            val turnOff = byteArrayOf(0x2C, 0x01)
            SerialPortDataManager.instance.sendCommand(STEAM_OUTPUT_COMMAND_ID, 2, turnOff)
        }
    }

}