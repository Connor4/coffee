package com.inno.coffee.function.selfcheck

import com.inno.coffee.function.CommandControlManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.CLEAN_MACHINE_ID
import com.inno.serialport.utilities.CONTINUE_CLEAN_MACHINE_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.START_HEAT_COFFEE_BOILER_ID
import com.inno.serialport.utilities.START_HEAT_STEAM_BOILER_ID
import com.inno.serialport.utilities.STOP_HEAT_COFFEE_BOILER_ID
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
import java.util.concurrent.atomic.AtomicInteger

// 1. IO自动检测状态，通过pull返回结果，同时根据pull返回IO自检阶段。
// 2. 自检完成由应用触发冲水等流程。
// 3. 制作异常，需要取消并执行清除操作。清除操作可细分各个阶段，磨粉阶段丢弃粉即可，萃取阶段需要完成萃取。
// TODO 4. 下发磨刀模块数据
object SelfCheckManager {
    const val RELEASE_STEAM_READY = 1
    const val RELEASE_STEAM_START = 2
    const val RELEASE_STEAM_FINISHED = 3
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
    private val _ioCheck = MutableStateFlow(false)
    var ioCheck = _ioCheck.asStateFlow()
    private val _operateRinse = MutableStateFlow(false)
    var operateRinse = _operateRinse.asStateFlow()
    private val _waitRinse = MutableStateFlow(false)
    var waitRinse = _waitRinse.asStateFlow()
    private val _coffeeHeating = MutableStateFlow(false)
    var coffeeHeating = _coffeeHeating.asStateFlow()
    private val _steamHeating = MutableStateFlow(false)
    var steamHeating = _steamHeating.asStateFlow()
    private val _washMachine = MutableStateFlow(false)
    var washMachine = _washMachine.asStateFlow()
    private val _leftLackPill = MutableStateFlow(false)
    var leftLackPill = _leftLackPill.asStateFlow()
    private val _rightLackPill = MutableStateFlow(false)
    var rightLackPill = _rightLackPill.asStateFlow()
    private val _releaseSteam = MutableStateFlow(0)
    var releaseSteam = _releaseSteam.asStateFlow()
    private val _checking = MutableStateFlow(true)
    val checking = _checking.asStateFlow()
    private val _step = MutableStateFlow(0)
    val step = _step.asStateFlow()
    private val operateCount = AtomicInteger(0)
    private var rightRinseFlag = false
    private var leftRinseFlag = false
    private var cleanCoffeeFlag = false
    private var cleanFoamFlag = false

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    private fun selfCheckFinished() {
        DataCenter.unsubscribe(ReceivedDataType.HEARTBEAT, subscriber)
        _step.value = STEP_CHECK_FINISH
    }

    suspend fun ioStatusCheck() {
        // TODO 使用pullinfo检查是否有异常
        _step.value = STEP_IO_CHECK_START
        delay(3000)
        _step.value = STEP_IO_CHECK_END
        _ioCheck.value = true
    }

    fun startRinse() {
        _step.value = STEP_RINSE_START
        _operateRinse.value = true
        _waitRinse.value = true
    }

    fun wakeupRinseSuccess() {
        // 1. 操作左右屏冲洗 2. 并且需要获取出水结果是否正常，正常则进入锅炉加热阶段
        if (operateCount.incrementAndGet() == 2) {
            _step.value = STEP_RINSE_END
            _waitRinse.value = false
            scope.launch {
                waitCoffeeBoilerHeating()
            }
        }
    }

    fun wakeupRinseFail() {
        scope.launch {
            delay(1000)
            wakeupRinseSuccess()
        }
    }

    suspend fun waitCoffeeBoilerHeating() {
        _step.value = STEP_BOILER_HEATING_START
        _coffeeHeating.value = true
        // TODO 1. 下发开始锅炉加热命令
        CommandControlManager.sendTestCommand(START_HEAT_COFFEE_BOILER_ID)
        //  2. 抓取pullinfo锅炉温度
        //  3. 下发停止锅炉加热命令
        delay(2000)
        CommandControlManager.sendTestCommand(STOP_HEAT_COFFEE_BOILER_ID)
        _step.value = STEP_BOILER_HEATING_END
        _coffeeHeating.value = false
        waitSteamBoilerHeating()
    }

    suspend fun waitSteamBoilerHeating() {
        delay(2000)
        _step.value = STEP_STEAM_HEATING_START
        _steamHeating.value = true
        // TODO 1. 下发开始锅炉加热命令
        CommandControlManager.sendTestCommand(START_HEAT_STEAM_BOILER_ID)
        //  2. 抓取pullinfo锅炉温度
        //  3. 下发停止锅炉加热命令
        delay(2000)
        CommandControlManager.sendTestCommand(STOP_HEAT_STEAM_BOILER_ID)
        _step.value = STEP_STEAM_HEATING_END
        _steamHeating.value = false
        _washMachine.value = true
    }

    suspend fun waitWashMachine() {
        _step.value = STEP_WASH_MACHINE_START
        CommandControlManager.sendTestCommand(CLEAN_MACHINE_ID)
        delay(5000)
        _step.value = STEP_WASH_MACHINE_END
        _washMachine.value = false
        _step.value = STEP_RELEASE_STEAM_READY
        _releaseSteam.value = RELEASE_STEAM_READY
    }

    fun putWashPill() {
        CommandControlManager.sendTestCommand(CONTINUE_CLEAN_MACHINE_ID)
        _step.value = STEP_WASH_MACHINE_START
    }

    suspend fun updateReleaseSteam() {
        _step.value = STEP_RELEASE_STEAM_START
        _releaseSteam.value = RELEASE_STEAM_START
        // TODO 1. 下发释放蒸汽命令
        //  2.抓取释放结果
        delay(2000)
        _step.value = STEP_RELEASE_STEAM_END
        _releaseSteam.value = RELEASE_STEAM_FINISHED
        _checking.value = false
        selfCheckFinished()
    }

    private fun parseReceivedData(data: Any) {
        // TODO 全部请求三次心跳，保证不存在错过或者获取旧心跳
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
                            _waitRinse.value = false
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
                                _coffeeHeating.value = false
                                CommandControlManager.sendTestCommand(STOP_HEAT_COFFEE_BOILER_ID)
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
                                _steamHeating.value = false
                                CommandControlManager.sendTestCommand(STOP_HEAT_STEAM_BOILER_ID)
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
                        _step.value = STEP_LACK_PILL_START
                        _leftLackPill.value = true
                    } else if (!_rightLackPill.value && reply.status == ErrorStatusEnum.NO_PILL_RIGHT) {
                        _step.value = STEP_LACK_PILL_START
                        _rightLackPill.value = true
                    }
                }
                washStatus.cleanMachine?.let { reply ->
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
                        _washMachine.value = false
                    }
                }
            }
            STEP_RELEASE_STEAM_START -> {
                val heartBeat = data as ReceivedData.HeartBeat
                if (heartBeat.error != null) {
                    // TODO 1 释放蒸汽异常，已进入主页，有异常弹窗，不需要额外操作
                } else {
                    // TODO 也是按照产品处理?
                    _step.value = STEP_RELEASE_STEAM_END
                    _releaseSteam.value = RELEASE_STEAM_FINISHED
                    _checking.value = false
                    selfCheckFinished()
                }
            }
        }

    }

}