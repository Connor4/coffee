package com.inno.coffee.function

import com.inno.coffee.utilities.MACHINE_PARAMS_COMMAND_ID
import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
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

        val byteArray = ByteArray(commandInfo.size * 2)
        commandInfo.forEachIndexed { index, value ->
            byteArray[index * 2] = (value shr 8).toByte()
            byteArray[index * 2 + 1] = (value and 0xFF).toByte()
        }
        Logger.d(TAG, "sendMachineParams() called with: byteArray = ${byteArray.toHexString()}")

        scope.launch {
            SerialPortDataManager.instance.sendCommand(MACHINE_PARAMS_COMMAND_ID, 32, byteArray)
        }
    }

}