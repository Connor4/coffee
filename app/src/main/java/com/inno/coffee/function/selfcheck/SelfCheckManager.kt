package com.inno.coffee.function.selfcheck

import android.util.Log
import com.inno.coffee.function.CommandControlManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.CLEAN_MACHINE_ID
import com.inno.serialport.utilities.CONTINUE_CLEAN_MACHINE_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.START_HEAT_COFFEE_BOILER_ID
import com.inno.serialport.utilities.START_HEAT_STEAM_BOILER_ID
import com.inno.serialport.utilities.STOP_HEAT_STEAM_BOILER_ID
import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import com.inno.serialport.utilities.statusenum.CleanMachineEnum
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object SelfCheckManager {
    private const val TAG = "SelfCheckManager"
    const val STEP_IO_CHECK_START = 1
    const val STEP_IO_CHECK_END = 2
    const val STEP_RINSE_START = 3
    const val STEP_RINSE_END = 4
    const val STEP_BOILER_HEATING_START = 5
    const val STEP_BOILER_HEATING_END = 6
    const val STEP_STEAM_HEATING_START = 7
    const val STEP_STEAM_HEATING_END = 8
    const val STEP_WASH_MACHINE_START = 9
    const val STEP_LACK_PILL_START = 10
    const val STEP_WASH_MACHINE_END = 11
    const val STEP_RELEASE_STEAM_READY = 12
    const val STEP_RELEASE_STEAM_START = 13
    const val STEP_RELEASE_STEAM_END = 14
    const val STEP_CHECK_FINISH = 15
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _leftLackPill = MutableStateFlow(false)
    var leftLackPill = _leftLackPill.asStateFlow()
    private val _rightLackPill = MutableStateFlow(false)
    var rightLackPill = _rightLackPill.asStateFlow()
    private val _step = MutableStateFlow(STEP_IO_CHECK_START)
    val step = _step.asStateFlow()
    private var rightRinseFlag = false
    private var leftRinseFlag = false
    private var cleanCoffeeFlag = false
    private var cleanFoamFlag = false
    private var rightSteamFlag = false
    private var leftSteamFlag = false

    private val testEnvironment = true

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    private fun selfCheckFinished() {
        _step.value = STEP_CHECK_FINISH
        DataCenter.unsubscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    suspend fun ioStatusCheck() {
        _step.value = STEP_IO_CHECK_START

        delay(5000)
        _step.value = STEP_IO_CHECK_END
    }

    fun checkColdRinse() {
        _step.value = STEP_RINSE_START

        if (testEnvironment) {
            scope.launch {
                delay(2000)
                _step.value = STEP_RINSE_END
                waitCoffeeBoilerHeating()
            }
        }
    }

    private suspend fun waitCoffeeBoilerHeating() {
        _step.value = STEP_BOILER_HEATING_START
        CommandControlManager.sendTestCommand(START_HEAT_COFFEE_BOILER_ID)

        if (testEnvironment) {
            delay(2000)
            _step.value = STEP_BOILER_HEATING_END
            waitSteamBoilerHeating()
        }
    }

    private suspend fun waitSteamBoilerHeating() {
        delay(2000)
        _step.value = STEP_STEAM_HEATING_START
        CommandControlManager.sendTestCommand(START_HEAT_STEAM_BOILER_ID)

        if (testEnvironment) {
            delay(2000)
            CommandControlManager.sendTestCommand(STOP_HEAT_STEAM_BOILER_ID)
            _step.value = STEP_STEAM_HEATING_END
        }
    }

    suspend fun waitWashMachine() {
        _step.value = STEP_WASH_MACHINE_START
        CommandControlManager.sendCommandWithTimeout(CLEAN_MACHINE_ID, 900000)

        if (testEnvironment) {
            delay(5000)
            _step.value = STEP_WASH_MACHINE_END
            _step.value = STEP_RELEASE_STEAM_READY
        }
    }

    fun putWashPill() {
        CommandControlManager.sendCommandWithTimeout(CONTINUE_CLEAN_MACHINE_ID, 60000)
        _step.value = STEP_WASH_MACHINE_START
    }

    suspend fun updateReleaseSteam() {
        _step.value = STEP_RELEASE_STEAM_START

        if (testEnvironment) {
            delay(2000)
            _step.value = STEP_RELEASE_STEAM_END
            delay(1000)
            selfCheckFinished()
        }
    }

    private fun parseReceivedData(data: Any) {
        when (_step.value) {
            STEP_IO_CHECK_START -> {
                val heartBeat = data as ReceivedData.HeartBeat
                if (heartBeat.error != null) {
                    // TODO
                    //  1 存在异常，正常弹窗提示异常即可，无需干预。
                    //  2 人工恢复后，需要手动触发再次io自检。所以需要有一个异常的提示，以及触发按钮
                } else {
                }
            }
            STEP_RINSE_START -> {
                val heartBeat = data as ReceivedData.HeartBeat
                if (heartBeat.error != null) {
                    // TODO 1 冲水异常，已进入主页，有异常弹窗，不需要额外操作
                } else {
                    data.makeDrinkStatus?.let { reply ->
                        val status = reply.status
                        when (status) {
                            MakeDrinkStatusEnum.RIGHT_FINISHED -> {
                                rightRinseFlag = true
                            }
                            MakeDrinkStatusEnum.LEFT_FINISHED -> {
                                leftRinseFlag = true
                            }
                            else -> {}
                        }
                        if (leftRinseFlag && rightRinseFlag) {
                            _step.value = STEP_RINSE_END
                            scope.launch {
                                waitCoffeeBoilerHeating()
                            }
                        }
                    }
                }
            }
            STEP_BOILER_HEATING_START -> {
                // 干等，或者设置超时报异常
                val boiler = data as ReceivedData.HeartBeat
                boiler.temperature?.let { reply ->
                    when (reply.status) {
                        BoilerStatusEnum.BOILER_TEMPERATURE -> {
                            val leftBoiler = ((reply.value[0].toInt() and 0xFF) shl 8) or
                                    (reply.value[1].toInt() and 0xFF)
                            val rightBoiler = ((reply.value[2].toInt() and 0xFF) shl 8) or
                                    (reply.value[3].toInt() and 0xFF)
                            // TODO 存在咖啡锅炉温度设置，需要获取
                            if (leftBoiler >= 92 && rightBoiler >= 92) {
                                _step.value = STEP_BOILER_HEATING_END
                                scope.launch {
                                    waitSteamBoilerHeating()
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            STEP_STEAM_HEATING_START -> {
                val boiler = data as ReceivedData.HeartBeat
                boiler.temperature?.let { reply ->
                    when (reply.status) {
                        BoilerStatusEnum.BOILER_TEMPERATURE -> {
                            val steamBoiler = ((reply.value[4].toInt() and 0xFF) shl 8) or
                                    (reply.value[5].toInt() and 0xFF)
                            if (steamBoiler >= 92) {
                                _step.value = STEP_STEAM_HEATING_END
                            }
                        }
                        else -> {}
                    }
                }
            }
            STEP_LACK_PILL_START,
            STEP_WASH_MACHINE_START,
                -> {
                val washStatus = data as ReceivedData.HeartBeat
                washStatus.error?.let { reply ->
                    if (!_leftLackPill.value && reply.status == ErrorStatusEnum.NO_PILL_LEFT) {
                        _leftLackPill.value = true
                        _step.value = STEP_LACK_PILL_START
                    } else if (!_rightLackPill.value && reply.status == ErrorStatusEnum.NO_PILL_RIGHT) {
                        _rightLackPill.value = true
                        _step.value = STEP_LACK_PILL_START
                    }
                }
                washStatus.cleanMachine?.let { reply ->
                    Log.d(TAG, "parseReceivedData() called with: reply = $reply")
                    when (reply.status) {
                        CleanMachineEnum.CLEAN_COFFEE_FINISH -> {
                            cleanCoffeeFlag = true
                        }
                        CleanMachineEnum.CLEAN_FOAM_FINISH -> {
                            cleanFoamFlag = true
                        }
                        else -> {}
                    }
                    if (cleanCoffeeFlag && cleanFoamFlag) {
                        _step.value = STEP_WASH_MACHINE_END
                        _step.value = STEP_RELEASE_STEAM_READY
                    }
                }
            }
            STEP_RELEASE_STEAM_START -> {
                val heartBeat = data as ReceivedData.HeartBeat
                if (heartBeat.error != null) {
                    // TODO 1 释放蒸汽异常，已进入主页，有异常弹窗，不需要额外操作
                } else {
                    data.makeDrinkStatus?.let { reply ->
                        val status = reply.status
                        when (status) {
                            MakeDrinkStatusEnum.RIGHT_FINISHED -> {
                                rightSteamFlag = true
                            }
                            MakeDrinkStatusEnum.LEFT_FINISHED -> {
                                leftSteamFlag = true
                            }
                            else -> {}
                        }
                        if (leftSteamFlag && rightSteamFlag) {
                            _step.value = STEP_RELEASE_STEAM_END
                            selfCheckFinished()
                        }
                    }
                }
            }
        }

    }

}