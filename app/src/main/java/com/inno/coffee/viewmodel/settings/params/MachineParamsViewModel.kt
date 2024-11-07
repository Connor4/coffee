package com.inno.coffee.viewmodel.settings.params

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.PARAMS_KEY_BOILER_TEMP
import com.inno.coffee.utilities.PARAMS_KEY_BREW_BALANCE
import com.inno.coffee.utilities.PARAMS_KEY_BREW_PRE_HEATING
import com.inno.coffee.utilities.PARAMS_KEY_COLD_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_GRINDER_PURGE_FUNCTION
import com.inno.coffee.utilities.PARAMS_KEY_GROUNDS_QUANTITY
import com.inno.coffee.utilities.PARAMS_KEY_NTC_LEFT
import com.inno.coffee.utilities.PARAMS_KEY_NTC_RIGHT
import com.inno.coffee.utilities.PARAMS_KEY_NUMBER_OF_CYCLES_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_STEAM_BOILER_PRESSURE
import com.inno.coffee.utilities.PARAMS_KEY_WARM_RINSE
import com.inno.coffee.utilities.PARAMS_VALUE_BOILER_TEMP
import com.inno.coffee.utilities.PARAMS_VALUE_STEAM_BOILER_PRESSURE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
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
    private val TAG = "MachineParamsViewModel"
    private val COFFEE_BOILER_TEMP = "coffee_boiler_temp"
    private val COLD_RINSE = "code_rinse"
    private val WARM_RINSE = "warm_rinse"
    private val GROUNDS_QUANTITY = "grounds_quantity"
    private val BREW_BALANCE = "brew_balance"
    private val BREW_PRE_HEATING = "brew_pre_heating"
    private val GRINDER_PURGE_FUNCTION = "grinder_purge_function"
    private val NUMBER_OF_CYCLES_RINSE = "number_of_cycles_rinse"
    private val STEAM_BOILER_PRESSURE = "steam_boiler_pressure"
    private val NTC_LEFT = "ntc_left"
    private val NTC_RIGHT = "ntc_right"
    private val TEMPERATURE_UNIT = "temperature_unit"

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
    private val _steamBoilerPressure = MutableStateFlow(PARAMS_VALUE_STEAM_BOILER_PRESSURE)
    val steamBoilerPressure = _steamBoilerPressure
    private val _ntcCorrectionSteamLeft = MutableStateFlow(0f)
    val ntcCorrectionSteamLeft = _ntcCorrectionSteamLeft
    private val _ntcCorrectionSteamRight = MutableStateFlow(0f)
    val ntcCorrectionSteamRight = _ntcCorrectionSteamRight

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
            _steamBoilerPressure.value = dataStore.getCoffeePreference(STEAM_BOILER_PRESSURE, 1)
            _ntcCorrectionSteamLeft.value =
                temperatureDisplay(dataStore.getCoffeePreference(NTC_LEFT, 0f))
            _ntcCorrectionSteamRight.value =
                temperatureDisplay(dataStore.getCoffeePreference(NTC_RIGHT, 0f))
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
                    val realValue = value as Float
                    dataStore.saveCoffeePreference(STEAM_BOILER_PRESSURE, realValue.toInt())
                    _steamBoilerPressure.value = realValue.toInt()
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
                else -> {}
            }
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