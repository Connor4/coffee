package com.inno.coffee.viewmodel.settings.machinetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.ONE_IN_BYTE
import com.inno.common.utils.CoffeeDataStore
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.COFFEE_INPUT_COMMAND_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.STEAM_INPUT_COMMAND_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineTestInputViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    companion object {
        private const val TAG = "MachineTestViewModel"
        private const val TEMPERATURE_UNIT = "temperature_unit"
    }

    private val _tempUnit = MutableStateFlow(false)
    private val _microSwitchLeft = MutableStateFlow(true)
    val microSwitchLeft = _microSwitchLeft
    private val _microSwitchRight = MutableStateFlow(true)
    val microSwitchRight = _microSwitchRight
    private val _rear = MutableStateFlow(true)
    val rear = _rear
    private val _front = MutableStateFlow(true)
    val front = _front
    private val _drawer = MutableStateFlow(true)
    val drawer = _drawer
    private val _switch = MutableStateFlow(true)
    val switch = _switch
    private val _pressure = MutableStateFlow(0f)
    val pressure = _pressure
    private val _leftTemp = MutableStateFlow(0f)
    val leftTemp = temperatureDisplayFlow(_leftTemp)
    private val _rightTemp = MutableStateFlow(0f)
    val rightTemp = temperatureDisplayFlow(_rightTemp)
    private val _leftFlow = MutableStateFlow(0f)
    val leftFlow = _leftFlow
    private val _rightFlow = MutableStateFlow(0f)
    val rightFlow = _rightFlow

    private val _steamPressure = MutableStateFlow(0f)
    val steamPressure = _steamPressure
    private val _securityLevel = MutableStateFlow(true)
    val securityLevel = _securityLevel
    private val _workLevel = MutableStateFlow(true)
    val workLevel = _workLevel
    private val _leftWandTemp = MutableStateFlow(0f)
    val leftWandTemp = temperatureDisplayFlow(_leftWandTemp)
    private val _rightWandTemp = MutableStateFlow(0f)
    val rightWandTemp = temperatureDisplayFlow(_rightWandTemp)


    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        viewModelScope.launch {
            _tempUnit.value = dataStore.getCoffeePreference(TEMPERATURE_UNIT, false)
        }
        DataCenter.subscribe(ReceivedDataType.COMMON_REPLY, subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        DataCenter.unsubscribe(ReceivedDataType.COMMON_REPLY, subscriber)
    }

    fun getCoffeeInputs() {
        viewModelScope.launch {
            while (true) {
                CommandControlManager.sendTestCommand(COFFEE_INPUT_COMMAND_ID)
                delay(1000)
            }
        }
    }

    fun getSteamInputs() {
        viewModelScope.launch {
            while (true) {
                CommandControlManager.sendTestCommand(STEAM_INPUT_COMMAND_ID)
                delay(1000)
            }
        }
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.CommonReply) {
            val params = data.params
            val commandId = data.commandId
            if (commandId == COFFEE_INPUT_COMMAND_ID) {
                _microSwitchLeft.value = params[1] == ONE_IN_BYTE
                _microSwitchRight.value = params[3] == ONE_IN_BYTE
                _rear.value = params[5] == ONE_IN_BYTE
                _front.value = params[7] == ONE_IN_BYTE
                _drawer.value = params[9] == ONE_IN_BYTE
                _switch.value = params[11] == ONE_IN_BYTE
                val pressure =
                    ((params[13].toInt() and 0xFF) shl 8) or (params[12].toInt() and 0xFF)
                _pressure.value = pressure / 10f
                val leftTemp =
                    ((params[15].toInt() and 0xFF) shl 8) or (params[14].toInt() and 0xFF)
                _leftTemp.value = leftTemp / 10f
                val rightTemp =
                    ((params[17].toInt() and 0xFF) shl 8) or (params[16].toInt() and 0xFF)
                _rightTemp.value = rightTemp / 10f
                val leftFlow =
                    ((params[19].toInt() and 0xFF) shl 8) or (params[18].toInt() and 0xFF)
                _leftFlow.value = leftFlow / 10f
                val rightFlow =
                    ((params[21].toInt() and 0xFF) shl 8) or (params[20].toInt() and 0xFF)
                _rightFlow.value = rightFlow / 10f
            } else if (commandId == STEAM_INPUT_COMMAND_ID) {
                val steamPressure =
                    ((params[1].toInt() and 0xFF) shl 8) or (params[0].toInt() and 0xFF)
                _steamPressure.value = steamPressure / 10f
                _securityLevel.value = params[3] == ONE_IN_BYTE
                _workLevel.value = params[5] == ONE_IN_BYTE
                val leftWandTemp =
                    ((params[7].toInt() and 0xFF) shl 8) or (params[6].toInt() and 0xFF)
                _leftWandTemp.value = leftWandTemp / 10f
                val rightWandTemp =
                    ((params[9].toInt() and 0xFF) shl 8) or (params[8].toInt() and 0xFF)
                _rightWandTemp.value = rightWandTemp / 10f
            }
        }
    }

    private fun temperatureDisplayFlow(temperatureFlow: StateFlow<Float>): StateFlow<String> {
        return _tempUnit.combine(temperatureFlow) { isFahrenheit, tempCelsius ->
            if (isFahrenheit) {
                val fahrenheit = tempCelsius * 1.8 + 32
                "$fahrenheit °F"
            } else {
                "$tempCelsius °C"
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, "")

    }

}