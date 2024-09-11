package com.inno.coffee.function.selfcheck

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. IO自动检测状态，通过pull返回结果，同时根据pull返回IO自检阶段。
// 2. 自检完成由应用触发冲水等流程。
// 3. 制作异常，需要取消并执行清除操作。清除操作可细分各个阶段，磨粉阶段丢弃粉即可，萃取阶段需要完成萃取。
object SelfCheckManager {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _operateRinse = MutableStateFlow(false)
    var operateRinse = _operateRinse.asStateFlow()
    private val _coffeeHeating = MutableStateFlow(false)
    var coffeeHeating = _coffeeHeating.asStateFlow()
    private val _steamHeating = MutableStateFlow(false)
    var steamHeating = _steamHeating.asStateFlow()
    private val _releaseSteam = MutableStateFlow(0)
    var releaseSteam = _releaseSteam.asStateFlow()
    private val _checking = MutableStateFlow(true)
    val checking = _checking.asStateFlow()
    private var step = 0

    suspend fun ioStatusCheck(): Boolean {
        return scope.async {
            // TODO 使用pullinfo检查是否有异常
            delay(3000)
            step = 1
            true
        }.await()
    }

    suspend fun operateRinse() {
        // TODO 1. 操作出水
        //  2. 并且需要获取出水结果是否正常，正常则进入锅炉加热阶段
        _operateRinse.value = true
        step = 2
        waitCoffeeBoilerHeating()
    }

    suspend fun waitCoffeeBoilerHeating() {
        _coffeeHeating.value = true
        // TODO 1. 下发开始锅炉加热命令
        //  2. 抓取pullinfo锅炉温度
        //  3. 下发停止锅炉加热命令
        delay(3000)
        step = 3
        _coffeeHeating.value = false
        waitSteamBoilerHeating()
    }

    suspend fun waitSteamBoilerHeating() {
        _steamHeating.value = true
        // TODO 1. 下发开始锅炉加热命令
        //  2. 抓取pullinfo锅炉温度
        //  3. 下发停止锅炉加热命令
        delay(3000)
        step = 4
        _steamHeating.value = false
        _releaseSteam.value = 1
    }

    suspend fun releaseSteamNotice() {
        _releaseSteam.value = 1
    }

    suspend fun updateReleaseSteam() {
        _releaseSteam.value = 2
        // TODO 1. 下发释放蒸汽命令
        //  2.抓取释放结果
        delay(3000)
        step = 4
        _releaseSteam.value = 3
        _checking.value = false
    }

}