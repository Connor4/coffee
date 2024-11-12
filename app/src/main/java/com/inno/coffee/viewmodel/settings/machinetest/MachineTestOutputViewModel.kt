package com.inno.coffee.viewmodel.settings.machinetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_CURRENT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_SPEED
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_STEP
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineTestOutputViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    private val TAG = "MachineTestOutputViewModel"
    private val MACHINE_TEST_STEP = "machine_test_step"
    private val MACHINE_TEST_SPEED = "machine_test_speed"
    private val MACHINE_TEST_CURRENT = "machine_test_current"

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

    fun sendCoffeeOutputCommand() {

    }

    fun sendSteamOutputCommand() {

    }

    fun saveMotorTestValue(key: Int, value: Any) {
        Logger.d(TAG, "saveMotorTestValue() called with: key = $key, value = $value")
        viewModelScope.launch {
            when (key) {
                MACHINE_TEST_MOTOR_STEP -> {
                    dataStore.saveCoffeePreference(MACHINE_TEST_STEP, value as Int)
                    _step.value = value
                }
                MACHINE_TEST_MOTOR_SPEED -> {
                    dataStore.saveCoffeePreference(MACHINE_TEST_SPEED, value as Int)
                    _speed.value = value
                }
                MACHINE_TEST_MOTOR_CURRENT -> {
                    dataStore.saveCoffeePreference(MACHINE_TEST_CURRENT, value as Int)
                    _current.value = value
                }
            }
        }
    }

}