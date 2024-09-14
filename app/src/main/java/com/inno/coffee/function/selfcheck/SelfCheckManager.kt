package com.inno.coffee.function.selfcheck

import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. IO自动检测状态，通过pull返回结果，同时根据pull返回IO自检阶段。
// 2. 自检完成由应用触发冲水等流程。
// 3. 制作异常，需要取消并执行清除操作。清除操作可细分各个阶段，磨粉阶段丢弃粉即可，萃取阶段需要完成萃取。
object SelfCheckManager {
    private const val STEP_IO_CHECK = 1
    private const val STEP_RINSE = 2
    private const val STEP_BOILER_HEATING = 3
    private const val STEP_STEAM_HEATING = 4
    private const val STEP_RELEASE_STEAM = 5
    private const val TRY_MAX_TIME = 3
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var tryTimes = 0
    private val _ioCheck = MutableStateFlow(true)
    var ioCheck = _ioCheck.asStateFlow()
    private val _operateRinse = MutableStateFlow(true)
    var operateRinse = _operateRinse.asStateFlow()
    private val _coffeeHeating = MutableStateFlow(false)
    var coffeeHeating = _coffeeHeating.asStateFlow()
    private val _steamHeating = MutableStateFlow(false)
    var steamHeating = _steamHeating.asStateFlow()
    private val _releaseSteam = MutableStateFlow(0)
    var releaseSteam = _releaseSteam.asStateFlow()
    private val _checking = MutableStateFlow(false)
    val checking = _checking.asStateFlow()
    private var step = 0
    private val _errorStep = MutableStateFlow(step)
    val errorStep = _errorStep.asStateFlow()

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    suspend fun ioStatusCheck() {
        // TODO 使用pullinfo检查是否有异常
        delay(3000)
        _ioCheck.value = true
        step = STEP_IO_CHECK
    }

    suspend fun operateRinse() {
        // TODO 1. 操作出水
        //  2. 并且需要获取出水结果是否正常，正常则进入锅炉加热阶段
        _operateRinse.value = true
        step = STEP_RINSE
        waitCoffeeBoilerHeating()
    }

    suspend fun waitCoffeeBoilerHeating() {
        _coffeeHeating.value = true
        // TODO 1. 下发开始锅炉加热命令
        //  2. 抓取pullinfo锅炉温度
        //  3. 下发停止锅炉加热命令
        delay(3000)
        step = STEP_BOILER_HEATING
        _coffeeHeating.value = false
        waitSteamBoilerHeating()
    }

    suspend fun waitSteamBoilerHeating() {
        _steamHeating.value = true
        // TODO 1. 下发开始锅炉加热命令
        //  2. 抓取pullinfo锅炉温度
        //  3. 下发停止锅炉加热命令
        delay(3000)
        step = STEP_STEAM_HEATING
        _steamHeating.value = false
        _releaseSteam.value = 1
    }

    suspend fun updateReleaseSteam() {
        _releaseSteam.value = 2
        // TODO 1. 下发释放蒸汽命令
        //  2.抓取释放结果
        delay(3000)
        step = STEP_RELEASE_STEAM
        _releaseSteam.value = 3
        _checking.value = false
    }

    private fun parseReceivedData(data: Any) {
        // TODO 全部请求三次心跳，保证不存在错过或者获取旧心跳
        when (step) {
            STEP_IO_CHECK -> {
                if (tryTimes < TRY_MAX_TIME) {
                    tryTimes++
                    val heartBeat = data as ReceivedData.HeartBeat
                    if (heartBeat.error != null) {
                        // TODO
                        //  1 存在异常，正常弹窗提示异常即可，无需干预。
                        //  2 人工恢复后，需要手动触发再次io自检。所以需要有一个异常的提示，以及触发按钮
                        _errorStep.value = STEP_IO_CHECK
                    } else {
                        step = STEP_RINSE
                        _ioCheck.value = true
                        tryTimes = 0
                    }
                }
            }
            STEP_RINSE -> {
                if (tryTimes < TRY_MAX_TIME) {
                    tryTimes++
                    val heartBeat = data as ReceivedData.HeartBeat
                    if (heartBeat.error != null) {
                        // TODO 1 冲水异常，已进入主页，有异常弹窗，不需要额外操作
                        _errorStep.value = STEP_RINSE
                    } else {
                        step = STEP_BOILER_HEATING
                        _operateRinse.value = true
                        tryTimes = 0
                        scope.launch {
                            waitCoffeeBoilerHeating()
                        }
                    }
                }
            }
            STEP_BOILER_HEATING -> {
                // 干等，或者设置超时报异常
                val boiler = data as ReceivedData.HeartBeat
                boiler.temperature?.let { reply ->
                    when (reply.status) {
                        BoilerStatusEnum.BOILER_TEMPERATURE -> {
                            val leftBoiler = ((reply.value[0].toInt() and 0xFF) shl 8) or
                                    (reply.value[1].toInt() and 0xFF)
                            val rightBoiler = ((reply.value[2].toInt() and 0xFF) shl 8) or
                                    (reply.value[3].toInt() and 0xFF)
                            if (leftBoiler >= 92 && rightBoiler >= 92) {
                                _coffeeHeating.value = false
                                scope.launch {
                                    waitSteamBoilerHeating()
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            STEP_STEAM_HEATING -> {
                // TODO 待定
            }
            STEP_RELEASE_STEAM -> {
                if (tryTimes < TRY_MAX_TIME) {
                    val heartBeat = data as ReceivedData.HeartBeat
                    if (heartBeat.error != null) {
                        // TODO 1 释放蒸汽异常，已进入主页，有异常弹窗，不需要额外操作
                        _errorStep.value = STEP_RELEASE_STEAM
                    } else {
                        _releaseSteam.value = 3
                    }
                }
            }
        }

    }

}