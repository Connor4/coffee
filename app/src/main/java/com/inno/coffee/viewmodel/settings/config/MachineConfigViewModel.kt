package com.inno.coffee.viewmodel.settings.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.ADJUSTMENT_NONE
import com.inno.coffee.utilities.CONFIG_OPERATION_MODE_NORMAL
import com.inno.coffee.utilities.HOW_WATER_MID
import com.inno.coffee.utilities.SMART_MODE_OFF
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineConfigViewModel @Inject constructor(
    private val coffeeDataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val TAG = "MachineConfigViewModel"

    private val _lowPower = MutableStateFlow(false)
    val lowPower: StateFlow<Boolean> = _lowPower
    private val _machineType = MutableStateFlow("")
    val machineType: StateFlow<String> = _machineType
    private val _temperatureUnit = MutableStateFlow(false)
    val temperatureUnit: StateFlow<Boolean> = _temperatureUnit
    private val _operationMode = MutableStateFlow(CONFIG_OPERATION_MODE_NORMAL)
    val operationMode: StateFlow<Int> = _operationMode
    private val _smartMode = MutableStateFlow(SMART_MODE_OFF)
    val smartMode: StateFlow<Int> = _smartMode
    private val _waterTankSurveillance = MutableStateFlow(false)
    val waterTankSurveillance: StateFlow<Boolean> = _waterTankSurveillance
    private val _differentBoiler = MutableStateFlow(false)
    val differentBoiler: StateFlow<Boolean> = _differentBoiler
    private val _americanoTempAdjust = MutableStateFlow(ADJUSTMENT_NONE)
    val americanoTempAdjust: StateFlow<Int> = _americanoTempAdjust
    private val _hotWaterOutput = MutableStateFlow(HOW_WATER_MID)
    val hotWaterOutput: StateFlow<Int> = _hotWaterOutput
    private val _steamWandPosition = MutableStateFlow(HOW_WATER_MID)
    val steamWandPosition: StateFlow<Int> = _steamWandPosition

    fun init() {
        viewModelScope.launch {
            _lowPower.value = coffeeDataStore.getLowPowerMode()
            _machineType.value = coffeeDataStore.getMachineType()
            _temperatureUnit.value = coffeeDataStore.getTemperatureUnit()
            _operationMode.value = coffeeDataStore.getOperationMode()
            _smartMode.value = coffeeDataStore.getSmartMode()
            _waterTankSurveillance.value = coffeeDataStore.getWaterTankSurveillance()
            _differentBoiler.value = coffeeDataStore.getDifferentBoiler()
            _americanoTempAdjust.value = coffeeDataStore.getAmericanoTempAdjust()
            _hotWaterOutput.value = coffeeDataStore.getHotWaterOutput()
            _steamWandPosition.value = coffeeDataStore.getSteamWandPosition()
        }
    }

}