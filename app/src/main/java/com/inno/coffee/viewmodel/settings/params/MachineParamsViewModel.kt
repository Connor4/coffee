package com.inno.coffee.viewmodel.settings.params

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.BREW_BALANCE
import com.inno.coffee.utilities.BREW_PRE_HEATING
import com.inno.coffee.utilities.COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.COLD_RINSE
import com.inno.coffee.utilities.GRINDER_PURGE_FUNCTION
import com.inno.coffee.utilities.GROUNDS_QUANTITY
import com.inno.coffee.utilities.MACHINE_PRIORITY
import com.inno.coffee.utilities.MILK_RINSE
import com.inno.coffee.utilities.NTC_LEFT
import com.inno.coffee.utilities.NTC_RIGHT
import com.inno.coffee.utilities.NUMBER_OF_CYCLES_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_BOILER_TEMP
import com.inno.coffee.utilities.PARAMS_KEY_BREW_BALANCE
import com.inno.coffee.utilities.PARAMS_KEY_BREW_PRE_HEATING
import com.inno.coffee.utilities.PARAMS_KEY_COLD_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_GRINDER_PURGE_FUNCTION
import com.inno.coffee.utilities.PARAMS_KEY_GROUNDS_QUANTITY
import com.inno.coffee.utilities.PARAMS_KEY_MACHINE_PRIORITY
import com.inno.coffee.utilities.PARAMS_KEY_MILK_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_NTC_LEFT
import com.inno.coffee.utilities.PARAMS_KEY_NTC_RIGHT
import com.inno.coffee.utilities.PARAMS_KEY_NUMBER_OF_CYCLES_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_SINK_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_STEAM_BOILER_PRESSURE
import com.inno.coffee.utilities.PARAMS_KEY_WARM_RINSE
import com.inno.coffee.utilities.PARAMS_VALUE_BOILER_TEMP
import com.inno.coffee.utilities.SINK_RINSE
import com.inno.coffee.utilities.STEAM_BOILER_PRESSURE
import com.inno.coffee.utilities.WARM_RINSE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.MACHINE_PARAM_COMMAND_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineParamsViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    companion object {
        private const val TAG = "MachineParamsViewModel"
        private const val TEMPERATURE_UNIT = "temperature_unit"
    }

    private var _temperatureUnit = MutableStateFlow(false)
    val temperatureUnit = _temperatureUnit
    private val _boilerTemp = MutableStateFlow(90f)
    val boilerTemp = _boilerTemp
    private val _coldRinseQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val coldRinseQuantity = _coldRinseQuantity
    private val _warmRinseQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val warmRinseQuantity = _warmRinseQuantity
    private val _groundsDrawerQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val groundsDrawerQuantity = _groundsDrawerQuantity
    private val _brewGroupLoadBalancing = MutableStateFlow(false)
    val brewGroupLoadBalancing = _brewGroupLoadBalancing
    private val _brewGroupPreHeating = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val brewGroupPreHeating = _brewGroupPreHeating
    private val _grinderPurgeFunction = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val grinderPurgeFunction = _grinderPurgeFunction
    private val _numberOfCyclesRinse = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val numberOfCyclesRinse = _numberOfCyclesRinse
    private val _steamBoilerPressure = MutableStateFlow(1f)
    val steamBoilerPressure = _steamBoilerPressure
    private val _ntcCorrectionSteamLeft = MutableStateFlow(0f)
    val ntcCorrectionSteamLeft = _ntcCorrectionSteamLeft
    private val _ntcCorrectionSteamRight = MutableStateFlow(0f)
    val ntcCorrectionSteamRight = _ntcCorrectionSteamRight

    private val _sinkRinse = MutableStateFlow(false)
    val sinkRinse = _sinkRinse
    private val _milkRinse = MutableStateFlow(false)
    val milkRinse = _milkRinse
    private val _machinePriority = MutableStateFlow(false)
    val machinePriority = _machinePriority

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _temperatureUnit.value = dataStore.getCoffeePreference(TEMPERATURE_UNIT, false)
            _boilerTemp.value = temperatureDisplay(
                dataStore.getCoffeePreference(COFFEE_BOILER_TEMP, 90f))
            _coldRinseQuantity.value = dataStore.getCoffeePreference(COLD_RINSE, 500)
            _warmRinseQuantity.value = dataStore.getCoffeePreference(WARM_RINSE, 100)
            _groundsDrawerQuantity.value = dataStore.getCoffeePreference(GROUNDS_QUANTITY, 0)
            _brewGroupLoadBalancing.value = dataStore.getCoffeePreference(BREW_BALANCE, false)
            _brewGroupPreHeating.value = dataStore.getCoffeePreference(BREW_PRE_HEATING, 0)
            _grinderPurgeFunction.value = dataStore.getCoffeePreference(GRINDER_PURGE_FUNCTION, 0)
            _numberOfCyclesRinse.value = dataStore.getCoffeePreference(NUMBER_OF_CYCLES_RINSE, 0)
            _steamBoilerPressure.value = dataStore.getCoffeePreference(STEAM_BOILER_PRESSURE, 1f)
            _ntcCorrectionSteamLeft.value =
                temperatureDisplay(dataStore.getCoffeePreference(NTC_LEFT, 0f))
            _ntcCorrectionSteamRight.value =
                temperatureDisplay(dataStore.getCoffeePreference(NTC_RIGHT, 0f))

            _sinkRinse.value = dataStore.getCoffeePreference(SINK_RINSE, false)
            _milkRinse.value = dataStore.getCoffeePreference(MILK_RINSE, false)
            _machinePriority.value = dataStore.getCoffeePreference(MACHINE_PRIORITY, false)
        }
    }

    fun saveMachineParamsValue(key: Int, value: Any) {
        Logger.d(TAG, "saveMachineParamsValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                PARAMS_KEY_BOILER_TEMP -> {
                    val realValue = temperatureRevert(value as Float)
                    dataStore.saveCoffeePreference(COFFEE_BOILER_TEMP, realValue)
                    _boilerTemp.value = value
                }
                PARAMS_KEY_COLD_RINSE -> {
                    val realValue = value as Float
                    dataStore.saveCoffeePreference(COLD_RINSE, realValue.toInt())
                    _coldRinseQuantity.value = realValue.toInt()
                }
                PARAMS_KEY_WARM_RINSE -> {
                    val realValue = value as Float
                    dataStore.saveCoffeePreference(WARM_RINSE, realValue.toInt())
                    _warmRinseQuantity.value = realValue.toInt()
                }
                PARAMS_KEY_GROUNDS_QUANTITY -> {
                    dataStore.saveCoffeePreference(GROUNDS_QUANTITY, value as Int)
                    _groundsDrawerQuantity.value = value
                }
                PARAMS_KEY_BREW_BALANCE -> {
                    dataStore.saveCoffeePreference(BREW_BALANCE, value as Boolean)
                    _brewGroupLoadBalancing.value = value
                }
                PARAMS_KEY_BREW_PRE_HEATING -> {
                    dataStore.saveCoffeePreference(BREW_PRE_HEATING, value as Int)
                    _brewGroupPreHeating.value = value
                }
                PARAMS_KEY_GRINDER_PURGE_FUNCTION -> {
                    dataStore.saveCoffeePreference(GRINDER_PURGE_FUNCTION, value as Int)
                    _grinderPurgeFunction.value = value
                }
                PARAMS_KEY_NUMBER_OF_CYCLES_RINSE -> {
                    dataStore.saveCoffeePreference(NUMBER_OF_CYCLES_RINSE, value as Int)
                    _numberOfCyclesRinse.value = value
                }
                PARAMS_KEY_STEAM_BOILER_PRESSURE -> {
                    dataStore.saveCoffeePreference(STEAM_BOILER_PRESSURE, value as Float)
                    _steamBoilerPressure.value = value
                }
                PARAMS_KEY_NTC_LEFT -> {
                    val realValue = temperatureRevert(value as Float)
                    dataStore.saveCoffeePreference(NTC_LEFT, realValue)
                    _ntcCorrectionSteamLeft.value = value
                }
                PARAMS_KEY_NTC_RIGHT -> {
                    val realValue = temperatureRevert(value as Float)
                    dataStore.saveCoffeePreference(NTC_RIGHT, realValue)
                    _ntcCorrectionSteamRight.value = value
                }
                PARAMS_KEY_SINK_RINSE -> {
                    dataStore.saveCoffeePreference(SINK_RINSE, value as Boolean)
                    _sinkRinse.value = value
                }
                PARAMS_KEY_MILK_RINSE -> {
                    dataStore.saveCoffeePreference(MILK_RINSE, value as Boolean)
                    _milkRinse.value = value
                }
                PARAMS_KEY_MACHINE_PRIORITY -> {
                    dataStore.saveCoffeePreference(MACHINE_PRIORITY, value as Boolean)
                    _machinePriority.value = value
                }
                else -> {}
            }

            val balance = if (_brewGroupLoadBalancing.value) 1 else 0
            val sinkRinse = if (_sinkRinse.value) 1 else 0
            val milkRinse = if (_milkRinse.value) 1 else 0
            val priority = if (_machinePriority.value) 1 else 0
            CommandControlManager.sendTestCommand(MACHINE_PARAM_COMMAND_ID,
                _boilerTemp.value.toInt(), _boilerTemp.value.toInt(),
                _coldRinseQuantity.value, _warmRinseQuantity.value, _groundsDrawerQuantity.value,
                balance, _brewGroupPreHeating.value, _grinderPurgeFunction.value, 0,
                _numberOfCyclesRinse.value, (_steamBoilerPressure.value * 10).toInt(),
                _ntcCorrectionSteamLeft.value.toInt(), _ntcCorrectionSteamRight.value.toInt(),
                sinkRinse, milkRinse, priority)

        }
    }

    fun temperatureDisplay(value: Float): Float {
        val result = if (_temperatureUnit.value) {
            Math.round(value * 9 / 5 + 32).toFloat()
        } else {
            Math.round(value).toFloat()
        }
        Logger.d(TAG, "temperatureDisplay() called with: value = $value result = $result")
        return result
    }

    private fun temperatureRevert(value: Float): Float {
        val result = if (_temperatureUnit.value) {
            Math.round((value - 32) * 5 / 9).toFloat()
        } else {
            Math.round(value).toFloat()
        }
        Logger.d(TAG, "temperatureRevert() called with: value = $value result = $result")
        return result
    }

}