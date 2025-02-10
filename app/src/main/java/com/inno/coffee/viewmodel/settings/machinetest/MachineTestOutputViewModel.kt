package com.inno.coffee.viewmodel.settings.machinetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_CURRENT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_LEFT_BOTTOM
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_LEFT_TOP
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_RIGHT_BOTTOM
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_RIGHT_TOP
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_SPEED
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_STEP
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.COFFEE_OUTPUT_COMMAND_ID
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_COMMAND_ID
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
        private const val MACHINE_TEST_STEP_LEFT_TOP = "machine_test_step_left_top"
        private const val MACHINE_TEST_STEP_RIGHT_TOP = "machine_test_step_right_top"
        private const val MACHINE_TEST_STEP_LEFT_BOTTOM = "machine_test_step_left_bottom"
        private const val MACHINE_TEST_STEP_RIGHT_BOTTOM = "machine_test_step_right_bottom"
        private const val MACHINE_TEST_SPEED_LEFT_TOP = "machine_test_speed_left_top"
        private const val MACHINE_TEST_SPEED_RIGHT_TOP = "machine_test_speed_right_top"
        private const val MACHINE_TEST_SPEED_LEFT_BOTTOM = "machine_test_speed_left_bottom"
        private const val MACHINE_TEST_SPEED_RIGHT_BOTTOM = "machine_test_speed_right_bottom"
        private const val MACHINE_TEST_CURRENT_LEFT_TOP = "machine_test_current_left_top"
        private const val MACHINE_TEST_CURRENT_RIGHT_TOP = "machine_test_current_right_top"
        private const val MACHINE_TEST_CURRENT_LEFT_BOTTOM = "machine_test_current_left_bottom"
        private const val MACHINE_TEST_CURRENT_RIGHT_BOTTOM = "machine_test_current_right_bottom"
        private const val MACHINE_DEFAULT_STEP = 1000
        private const val MACHINE_DEFAULT_CURRENT = 10
        private const val MACHINE_DEFAULT_TOP_SPEED = 1000
        private const val MACHINE_DEFAULT_BOTTOM_SPEED = 800
    }

    private val _step = MutableStateFlow(0)
    val step = _step
    private val _speed = MutableStateFlow(0)
    val speed = _speed
    private val _current = MutableStateFlow(0)
    val current = _current

    init {
        viewModelScope.launch {
            _step.value =
                dataStore.getCoffeePreference(MACHINE_TEST_STEP_LEFT_TOP, MACHINE_DEFAULT_STEP)
            _speed.value =
                dataStore.getCoffeePreference(MACHINE_TEST_SPEED_LEFT_TOP,
                    MACHINE_DEFAULT_TOP_SPEED)
            _current.value =
                dataStore.getCoffeePreference(MACHINE_TEST_CURRENT_LEFT_TOP,
                    MACHINE_DEFAULT_CURRENT)
        }
    }

    fun coffeeTestTurnOff() {
        CommandControlManager.sendTestCommand(COFFEE_OUTPUT_COMMAND_ID)
    }

    fun steamTestTurnOff() {
        CommandControlManager.sendTestCommand(STEAM_OUTPUT_COMMAND_ID)
    }

    fun milkTestTurnOff() {
        CommandControlManager.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_COMMAND_ID)
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

    fun sendMotorTest(index: Int, add: Boolean) {
        val direction = if (index == MACHINE_TEST_MOTOR_LEFT_TOP ||
                index == MACHINE_TEST_MOTOR_RIGHT_TOP) {
            if (add) 0 else 1
        } else {
            if (add) 1 else 0
        }
        CommandControlManager.sendTestCommand(MACHINE_TEST_MOTOR_TEST_ID, index, direction,
            step.value, speed.value, current.value, 0, 0, 0)
    }

    fun saveMotorTestValue(index: Int, key: Int, value: Float) {
        Logger.d(TAG, "saveMotorTestValue() called with: index = $index, key = $key, value " +
                "=$value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                MACHINE_TEST_MOTOR_STEP -> {
                    val stepKey = when (index) {
                        MACHINE_TEST_MOTOR_LEFT_TOP -> {
                            MACHINE_TEST_STEP_LEFT_TOP
                        }
                        MACHINE_TEST_MOTOR_RIGHT_TOP -> {
                            MACHINE_TEST_STEP_RIGHT_TOP
                        }
                        MACHINE_TEST_MOTOR_LEFT_BOTTOM -> {
                            MACHINE_TEST_STEP_LEFT_BOTTOM
                        }
                        MACHINE_TEST_MOTOR_RIGHT_BOTTOM -> {
                            MACHINE_TEST_STEP_RIGHT_BOTTOM
                        }
                        else -> {
                            MACHINE_TEST_STEP_LEFT_TOP
                        }
                    }
                    dataStore.saveCoffeePreference(stepKey, value.toInt())
                    _step.value = value.toInt()
                }
                MACHINE_TEST_MOTOR_SPEED -> {
                    val speedKey = when (index) {
                        MACHINE_TEST_MOTOR_LEFT_TOP -> {
                            MACHINE_TEST_SPEED_LEFT_TOP
                        }
                        MACHINE_TEST_MOTOR_RIGHT_TOP -> {
                            MACHINE_TEST_SPEED_RIGHT_TOP
                        }
                        MACHINE_TEST_MOTOR_LEFT_BOTTOM -> {
                            MACHINE_TEST_SPEED_LEFT_BOTTOM
                        }
                        MACHINE_TEST_MOTOR_RIGHT_BOTTOM -> {
                            MACHINE_TEST_SPEED_RIGHT_BOTTOM
                        }
                        else -> {
                            MACHINE_TEST_SPEED_LEFT_TOP
                        }
                    }
                    dataStore.saveCoffeePreference(speedKey, value.toInt())
                    _speed.value = value.toInt()
                }
                MACHINE_TEST_MOTOR_CURRENT -> {
                    val currentKey = when (index) {
                        MACHINE_TEST_MOTOR_LEFT_TOP -> {
                            MACHINE_TEST_CURRENT_LEFT_TOP
                        }
                        MACHINE_TEST_MOTOR_RIGHT_TOP -> {
                            MACHINE_TEST_CURRENT_RIGHT_TOP
                        }
                        MACHINE_TEST_MOTOR_LEFT_BOTTOM -> {
                            MACHINE_TEST_CURRENT_LEFT_BOTTOM
                        }
                        MACHINE_TEST_MOTOR_RIGHT_BOTTOM -> {
                            MACHINE_TEST_CURRENT_RIGHT_BOTTOM
                        }
                        else -> {
                            MACHINE_TEST_CURRENT_LEFT_TOP
                        }
                    }
                    dataStore.saveCoffeePreference(currentKey, value.toInt())
                    _current.value = value.toInt()
                }
            }
        }
    }

    fun selectMotor(index: Int) {
        viewModelScope.launch {
            when (index) {
                MACHINE_TEST_MOTOR_LEFT_TOP -> {
                    _step.value = dataStore.getCoffeePreference(MACHINE_TEST_STEP_LEFT_TOP,
                        MACHINE_DEFAULT_STEP)
                    _speed.value = dataStore.getCoffeePreference(MACHINE_TEST_SPEED_LEFT_TOP,
                        MACHINE_DEFAULT_TOP_SPEED)
                    _current.value = dataStore.getCoffeePreference(MACHINE_TEST_CURRENT_LEFT_TOP,
                        MACHINE_DEFAULT_CURRENT)
                }
                MACHINE_TEST_MOTOR_RIGHT_TOP -> {
                    _step.value = dataStore.getCoffeePreference(MACHINE_TEST_STEP_RIGHT_TOP,
                        MACHINE_DEFAULT_STEP)
                    _speed.value = dataStore.getCoffeePreference(MACHINE_TEST_SPEED_RIGHT_TOP,
                        MACHINE_DEFAULT_TOP_SPEED)
                    _current.value = dataStore.getCoffeePreference(MACHINE_TEST_CURRENT_RIGHT_TOP,
                        MACHINE_DEFAULT_CURRENT)
                }
                MACHINE_TEST_MOTOR_LEFT_BOTTOM -> {
                    _step.value = dataStore.getCoffeePreference(MACHINE_TEST_STEP_LEFT_BOTTOM,
                        MACHINE_DEFAULT_STEP)
                    _speed.value = dataStore.getCoffeePreference(MACHINE_TEST_SPEED_LEFT_BOTTOM,
                        MACHINE_DEFAULT_BOTTOM_SPEED)
                    _current.value = dataStore.getCoffeePreference(MACHINE_TEST_CURRENT_LEFT_BOTTOM,
                        MACHINE_DEFAULT_CURRENT)
                }
                MACHINE_TEST_MOTOR_RIGHT_BOTTOM -> {
                    _step.value = dataStore.getCoffeePreference(MACHINE_TEST_STEP_RIGHT_BOTTOM,
                        MACHINE_DEFAULT_STEP)
                    _speed.value = dataStore.getCoffeePreference(MACHINE_TEST_SPEED_RIGHT_BOTTOM,
                        MACHINE_DEFAULT_BOTTOM_SPEED)
                    _current.value =
                        dataStore.getCoffeePreference(MACHINE_TEST_CURRENT_RIGHT_BOTTOM,
                            MACHINE_DEFAULT_CURRENT)
                }

            }
        }
    }

}