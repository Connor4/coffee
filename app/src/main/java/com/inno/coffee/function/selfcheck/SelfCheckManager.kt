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
    private val _heating = MutableStateFlow(false)
    var heating = _heating.asStateFlow()
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
        waitBoilerHeating()
    }

    suspend fun waitBoilerHeating() {
        _heating.value = true
        step = 3
        delay(3000)
        _heating.value = false
    }

}