package com.inno.coffee.viewmodel.settings.machinetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.common.utils.CoffeeDataStore
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineTestViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    private val TAG = "MachineTestViewModel"
    private val TEMPERATURE_UNIT = "temperature_unit"

    private val _tempUnit = MutableStateFlow(false)
    private val _microSwitchLeft = MutableStateFlow(false)
    val microSwitchLeft = _microSwitchLeft
    private val _microSwitchRight = MutableStateFlow(false)
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
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        DataCenter.unsubscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    private fun parseReceivedData(data: Any) {
        val boiler = data as ReceivedData.HeartBeat
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