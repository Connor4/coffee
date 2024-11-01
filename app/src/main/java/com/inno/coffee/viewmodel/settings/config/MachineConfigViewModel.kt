package com.inno.coffee.viewmodel.settings.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.ADJUSTMENT_NONE
import com.inno.coffee.utilities.CONFIG_OPERATION_MODE_NORMAL
import com.inno.coffee.utilities.HOW_WATER_MID
import com.inno.coffee.utilities.INDEX_AMERICANO_TEMP_ADJUST
import com.inno.coffee.utilities.INDEX_DIFFERENT_BOILER
import com.inno.coffee.utilities.INDEX_HOT_WATER_OUTPUT
import com.inno.coffee.utilities.INDEX_LOW_POWER
import com.inno.coffee.utilities.INDEX_MACHINE_TYPE
import com.inno.coffee.utilities.INDEX_OPERATION_MODE
import com.inno.coffee.utilities.INDEX_POWER_CONFIGURATION
import com.inno.coffee.utilities.INDEX_SMART_MODE
import com.inno.coffee.utilities.INDEX_STEAM_WAND_POSITION
import com.inno.coffee.utilities.INDEX_TEMPERATURE_UNIT
import com.inno.coffee.utilities.INDEX_WATER_TANK_SURVEILLANCE
import com.inno.coffee.utilities.SMART_MODE_OFF
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
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

    fun saveMachineConfigValue(key: Int, value: Any) {
        Logger.d(TAG, "saveMachineConfigValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                INDEX_POWER_CONFIGURATION -> {
                    coffeeDataStore.saveLowPowerMode(value as Boolean)
                    _lowPower.value = value
                }
                INDEX_LOW_POWER -> {
                    coffeeDataStore.saveLowPowerMode(value as Boolean)
                    _lowPower.value = value
                }
                INDEX_MACHINE_TYPE -> {
                    coffeeDataStore.saveMachineType(value as String)
                    _machineType.value = value
                }
                INDEX_TEMPERATURE_UNIT -> {
                    coffeeDataStore.saveTemperatureUnit(value as Boolean)
                    _temperatureUnit.value = value
                }
                INDEX_OPERATION_MODE -> {
                    coffeeDataStore.saveOperationMode(value as Int)
                    _operationMode.value = value
                }
                INDEX_SMART_MODE -> {
                    coffeeDataStore.saveSmartMode(value as Int)
                    _smartMode.value = value
                }
                INDEX_WATER_TANK_SURVEILLANCE -> {
                    coffeeDataStore.saveWaterTankSurveillance(value as Boolean)
                    _waterTankSurveillance.value = value
                }
                INDEX_DIFFERENT_BOILER -> {
                    coffeeDataStore.saveDifferentBoiler(value as Boolean)
                    _differentBoiler.value = value
                }
                INDEX_AMERICANO_TEMP_ADJUST -> {
                    coffeeDataStore.saveAmericanoTempAdjust(value as Int)
                    _americanoTempAdjust.value = value
                }
                INDEX_HOT_WATER_OUTPUT -> {
                    coffeeDataStore.saveHotWaterOutput(value as Int)
                    _hotWaterOutput.value = value
                }
                INDEX_STEAM_WAND_POSITION -> {
                    coffeeDataStore.saveSteamWandPosition(value as Int)
                    _steamWandPosition.value = value
                }
            }
        }
    }

}