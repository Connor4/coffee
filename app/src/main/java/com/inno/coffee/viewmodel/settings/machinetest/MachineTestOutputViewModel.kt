package com.inno.coffee.viewmodel.settings.machinetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_CURRENT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_LEFT_TOP
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_RIGHT_TOP
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_SPEED
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_STEP
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.COFFEE_OUTPUT_COMMAND_ID
import com.inno.serialport.utilities.MACHINE_TEST_MOTOR_INIT_ID
import com.inno.serialport.utilities.MACHINE_TEST_MOTOR_TEST_ID
import com.inno.serialport.utilities.STEAM_OUTPUT_COMMAND_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineTestOutputViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    companion object {
        private const val TAG = "MachineTestOutputViewModel"
        private const val MACHINE_TEST_STEP = "machine_test_step"
        private const val MACHINE_TEST_SPEED = "machine_test_speed"
        private const val MACHINE_TEST_CURRENT = "machine_test_current"
    }

    private val _step = MutableStateFlow(0)
    val step = _step
    private val _speed = MutableStateFlow(0)
    val speed = _speed
    private val _current = MutableStateFlow(0)
    val current = _current

    init {
        viewModelScope.launch {
            _step.value = dataStore.getCoffeePreference(MACHINE_TEST_STEP, 1000)
            _speed.value = dataStore.getCoffeePreference(MACHINE_TEST_SPEED, 1000)
            _current.value = dataStore.getCoffeePreference(MACHINE_TEST_CURRENT, 10)
        }
    }

    fun coffeeTestTurnOff() {
        CommandControlManager.sendTestCommand(COFFEE_OUTPUT_COMMAND_ID)
    }

    fun steamTestTurnOff() {
        CommandControlManager.sendTestCommand(STEAM_OUTPUT_COMMAND_ID)
    }

    fun sendTestCommand(id: Short) {
        CommandControlManager.sendTestCommand(id)
    }

    fun sendTestCommand(id: Short, param: Int) {
        CommandControlManager.sendTestCommand(id, param)
    }

    fun motorInit() {
        CommandControlManager.sendTestCommand(MACHINE_TEST_MOTOR_INIT_ID)
    }

    fun sendMotorTest(index: Int, add: Boolean, step: Int, speed: Int, current: Int) {
        val direction = if (index == MACHINE_TEST_MOTOR_LEFT_TOP ||
                index == MACHINE_TEST_MOTOR_RIGHT_TOP) {
            if (add) 0 else 1
        } else {
            if (add) 1 else 0
        }
        CommandControlManager.sendTestCommand(MACHINE_TEST_MOTOR_TEST_ID, index, direction, step,
            speed, current, 0, 0, 0)
    }

    fun saveMotorTestValue(key: Int, value: Float) {
        Logger.d(TAG, "saveMotorTestValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                MACHINE_TEST_MOTOR_STEP -> {
                    dataStore.saveCoffeePreference(MACHINE_TEST_STEP, value.toInt())
                    _step.value = value.toInt()
                }
                MACHINE_TEST_MOTOR_SPEED -> {
                    dataStore.saveCoffeePreference(MACHINE_TEST_SPEED, value.toInt())
                    _speed.value = value.toInt()
                }
                MACHINE_TEST_MOTOR_CURRENT -> {
                    dataStore.saveCoffeePreference(MACHINE_TEST_CURRENT, value.toInt())
                    _current.value = value.toInt()
                }
            }
        }
    }

}