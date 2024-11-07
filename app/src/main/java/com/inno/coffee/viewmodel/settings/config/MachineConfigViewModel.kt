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
    private val LOW_POWER_MODE = "low_power_mode"
    private val MACHINE_TYPE = "machine_type"
    private val TEMPERATURE_UNIT = "temperature_unit"
    private val OPERATION_MODE = "operation_mode"
    private val SMART_MODE = "smart_mode"
    private val WATER_TANK_SURVEILLANCE = "water_tank_surveillance"
    private val DIFFERENT_BOILER = "different_boiler"
    private val AMERICANO_TEMP_ADJUST = "americano_temp_adjust"
    private val HOT_WATER_OUTPUT = "hot_water_output"
    private val STEAM_WAND_POSITION = "steam_wand_position"

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
            _lowPower.value = coffeeDataStore.getCoffeePreference(LOW_POWER_MODE, false)
            _machineType.value = coffeeDataStore.getCoffeePreference(MACHINE_TYPE, "CM01")
            _temperatureUnit.value = coffeeDataStore.getCoffeePreference(TEMPERATURE_UNIT, false)
            _operationMode.value = coffeeDataStore.getCoffeePreference(OPERATION_MODE, 0)
            _smartMode.value = coffeeDataStore.getCoffeePreference(SMART_MODE, 0)
            _waterTankSurveillance.value =
                coffeeDataStore.getCoffeePreference(WATER_TANK_SURVEILLANCE, false)
            _differentBoiler.value = coffeeDataStore.getCoffeePreference(DIFFERENT_BOILER, false)
            _americanoTempAdjust.value =
                coffeeDataStore.getCoffeePreference(AMERICANO_TEMP_ADJUST, 0)
            _hotWaterOutput.value = coffeeDataStore.getCoffeePreference(HOT_WATER_OUTPUT, 0)
            _steamWandPosition.value = coffeeDataStore.getCoffeePreference(STEAM_WAND_POSITION, 0)
        }
    }

    fun saveMachineConfigValue(key: Int, value: Any) {
        Logger.d(TAG, "saveMachineConfigValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                INDEX_POWER_CONFIGURATION -> {
                }
                INDEX_LOW_POWER -> {
                    coffeeDataStore.saveCoffeePreference(LOW_POWER_MODE, value as Boolean)
                    _lowPower.value = value
                }
                INDEX_MACHINE_TYPE -> {
                    coffeeDataStore.saveCoffeePreference(MACHINE_TYPE, value as String)
                    _machineType.value = value
                }
                INDEX_TEMPERATURE_UNIT -> {
                    coffeeDataStore.saveCoffeePreference(TEMPERATURE_UNIT, value as Boolean)
                    _temperatureUnit.value = value
                }
                INDEX_OPERATION_MODE -> {
                    coffeeDataStore.saveCoffeePreference(OPERATION_MODE, value as Int)
                    _operationMode.value = value
                }
                INDEX_SMART_MODE -> {
                    coffeeDataStore.saveCoffeePreference(SMART_MODE, value as Int)
                    _smartMode.value = value
                }
                INDEX_WATER_TANK_SURVEILLANCE -> {
                    coffeeDataStore.saveCoffeePreference(WATER_TANK_SURVEILLANCE, value as Boolean)
                    _waterTankSurveillance.value = value
                }
                INDEX_DIFFERENT_BOILER -> {
                    coffeeDataStore.saveCoffeePreference(DIFFERENT_BOILER, value as Boolean)
                    _differentBoiler.value = value
                }
                INDEX_AMERICANO_TEMP_ADJUST -> {
                    coffeeDataStore.saveCoffeePreference(AMERICANO_TEMP_ADJUST, value as Int)
                    _americanoTempAdjust.value = value
                }
                INDEX_HOT_WATER_OUTPUT -> {
                    coffeeDataStore.saveCoffeePreference(HOT_WATER_OUTPUT, value as Int)
                    _hotWaterOutput.value = value
                }
                INDEX_STEAM_WAND_POSITION -> {
                    coffeeDataStore.saveCoffeePreference(STEAM_WAND_POSITION, value as Int)
                    _steamWandPosition.value = value
                }
            }
        }
    }

}