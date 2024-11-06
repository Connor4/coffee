package com.inno.coffee.viewmodel.settings.params

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.utilities.PARAMS_VALUE_BOILER_TEMP
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineParamsViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    private val _boilerTemp = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val boilerTemp: StateFlow<Int> = _boilerTemp
    private val _coldRinseQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val coldRinseQuantity: StateFlow<Int> = _coldRinseQuantity
    private val _warmRinseQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val warmRinseQuantity: StateFlow<Int> = _warmRinseQuantity
    private val _groundsDrawerQuantity = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val groundsDrawerQuantity: StateFlow<Int> = _groundsDrawerQuantity
    private val _brewGroupLoadBalancing = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val brewGroupLoadBalancing: StateFlow<Int> = _brewGroupLoadBalancing
    private val _brewGroupPreHeating = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val brewGroupPreHeating: StateFlow<Int> = _brewGroupPreHeating
    private val _grinderPurgeFunction = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val grinderPurgeFunction: StateFlow<Int> = _grinderPurgeFunction
    private val _numberOfCyclesRinse = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val numberOfCyclesRinse: StateFlow<Int> = _numberOfCyclesRinse
    private val _steamBoilerPressure = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val steamBoilerPressure: StateFlow<Int> = _steamBoilerPressure
    private val _ntcCorrectionSteamLeft = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val ntcCorrectionSteamLeft: StateFlow<Int> = _ntcCorrectionSteamLeft
    private val _ntcCorrectionSteamRight = MutableStateFlow(PARAMS_VALUE_BOILER_TEMP)
    val ntcCorrectionSteamRight: StateFlow<Int> = _ntcCorrectionSteamRight

    fun init() {
        viewModelScope.launch {
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

}