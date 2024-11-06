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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineParamsViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val TAG = "MachineParamsViewModel"

    private val _boilerTemp = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val boilerTemp: StateFlow<Int> = _boilerTemp
    private val _coldRinseQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val coldRinseQuantity: StateFlow<Int> = _coldRinseQuantity
    private val _warmRinseQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val warmRinseQuantity: StateFlow<Int> = _warmRinseQuantity
    private val _groundsDrawerQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val groundsDrawerQuantity: StateFlow<Int> = _groundsDrawerQuantity
    private val _brewGroupLoadBalancing = MutableStateFlow(false)
    val brewGroupLoadBalancing: StateFlow<Boolean> = _brewGroupLoadBalancing
    private val _brewGroupPreHeating = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val brewGroupPreHeating: StateFlow<Int> = _brewGroupPreHeating
    private val _grinderPurgeFunction = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val grinderPurgeFunction: StateFlow<Int> = _grinderPurgeFunction
    private val _numberOfCyclesRinse = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val numberOfCyclesRinse: StateFlow<Int> = _numberOfCyclesRinse
    private val _steamBoilerPressure = MutableStateFlow(PARAMS_VALUE_STEAM_BOILER_PRESSURE)
    val steamBoilerPressure: StateFlow<Int> = _steamBoilerPressure
    private val _ntcCorrectionSteamLeft = MutableStateFlow(0)
    val ntcCorrectionSteamLeft: StateFlow<Int> = _ntcCorrectionSteamLeft
    private val _ntcCorrectionSteamRight = MutableStateFlow(0)
    val ntcCorrectionSteamRight: StateFlow<Int> = _ntcCorrectionSteamRight

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _boilerTemp.value = dataStore.getCoffeeBoilerTemp()
            _coldRinseQuantity.value = dataStore.getColdRinseQuantity()
            _warmRinseQuantity.value = dataStore.getWarmRinseQuantity()
            _groundsDrawerQuantity.value = dataStore.getGroundsDrawerQuantity()
            _brewGroupLoadBalancing.value = dataStore.getBrewGroupLoadBalancing()
            _brewGroupPreHeating.value = dataStore.getBrewGroupPreHeating()
            _grinderPurgeFunction.value = dataStore.getGrinderPurgeFunction()
            _numberOfCyclesRinse.value = dataStore.getNumberOfCyclesRinse()
            _steamBoilerPressure.value = dataStore.getSteamBoilerPressure()
            _ntcCorrectionSteamLeft.value = dataStore.getNtcCorrectionSteamLeft()
            _ntcCorrectionSteamRight.value = dataStore.getNtcCorrectionSteamRight()
        }
    }

    fun saveMachineParamsValue(key: Int, value: Any) {
        Logger.d(TAG, "saveMachineParamsValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                PARAMS_KEY_BOILER_TEMP -> {
                    dataStore.saveCoffeeBoilerTemp(value as Int)
                    _boilerTemp.value = value
                }
                PARAMS_KEY_COLD_RINSE -> {
                    dataStore.saveColdRinseQuantity(value as Int)
                    _coldRinseQuantity.value = value
                }
                PARAMS_KEY_WARM_RINSE -> {
                    dataStore.saveWarmRinseQuantity(value as Int)
                    _warmRinseQuantity.value = value
                }
                PARAMS_KEY_GROUNDS_QUANTITY -> {
                    dataStore.saveGroundsDrawerQuantity(value as Int)
                    _groundsDrawerQuantity.value = value
                }
                PARAMS_KEY_BREW_BALANCE -> {
                    dataStore.saveBrewGroupLoadBalancing(value as Boolean)
                    _brewGroupLoadBalancing.value = value
                }
                PARAMS_KEY_BREW_PRE_HEATING -> {
                    dataStore.saveBrewGroupPreHeating(value as Int)
                    _brewGroupPreHeating.value = value
                }
                PARAMS_KEY_GRINDER_PURGE_FUNCTION -> {
                    dataStore.saveGrinderPurgeFunction(value as Int)
                    _grinderPurgeFunction.value = value
                }
                PARAMS_KEY_NUMBER_OF_CYCLES_RINSE -> {
                    dataStore.saveNumberOfCyclesRinse(value as Int)
                    _numberOfCyclesRinse.value = value
                }
                PARAMS_KEY_STEAM_BOILER_PRESSURE -> {
                    dataStore.saveSteamBoilerPressure(value as Int)
                    _steamBoilerPressure.value = value
                }
                PARAMS_KEY_NTC_LEFT -> {
                    dataStore.saveNtcCorrectionSteamLeft(value as Int)
                    _ntcCorrectionSteamLeft.value = value
                }
                PARAMS_KEY_NTC_RIGHT -> {
                    dataStore.saveNtcCorrectionSteamRight(value as Int)
                    _ntcCorrectionSteamRight.value = value
                }
                else -> {}
            }
        }
    }

}