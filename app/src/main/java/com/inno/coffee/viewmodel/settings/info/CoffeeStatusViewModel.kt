package com.inno.coffee.viewmodel.settings.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.ONE_IN_BYTE
import com.inno.common.utils.CoffeeDataStore
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.INFO_COFFEE_STATUS_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeeStatusViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    private val _processCoffeeLeft = MutableStateFlow(0)
    val processCoffeeLeft = _processCoffeeLeft
    private val _actionCoffeeLeft = MutableStateFlow(0)
    val actionCoffeeLeft = _actionCoffeeLeft
    private val _processCoffeeRight = MutableStateFlow(0)
    val processCoffeeRight = _processCoffeeRight
    private val _actionCoffeeRight = MutableStateFlow(0)
    val actionCoffeeRight = _actionCoffeeRight
    private val _prodIdLeft = MutableStateFlow(0)
    val prodIdLeft = _prodIdLeft
    private val _prodIdRight = MutableStateFlow(0)
    val prodIdRight = _prodIdRight
    private val _numWarning = MutableStateFlow(0)
    val numWarning = _numWarning
    private val _numStop = MutableStateFlow(0)
    val numStop = _numStop
    private val _numError = MutableStateFlow(0)
    val numError = _numError

    private val _systemFlowLeft = MutableStateFlow(0)
    val systemFlowLeft = _systemFlowLeft
    private val _nozzleFlowLeft = MutableStateFlow(0)
    val nozzleFlowLeft = _nozzleFlowLeft
    private val _systemFlowRight = MutableStateFlow(0)
    val systemFlowRight = _systemFlowRight
    private val _nozzleFlowRight = MutableStateFlow(0)
    val nozzleFlowRight = _nozzleFlowRight

    private val _valveBrewLeft = MutableStateFlow(false)
    val valveBrewLeft = _valveBrewLeft
    private val _valveBrewRight = MutableStateFlow(false)
    val valveBrewRight = _valveBrewRight
    private val _valveBypassLeft = MutableStateFlow(false)
    val valveBypassLeft = _valveBypassLeft
    private val _valveBypassRight = MutableStateFlow(false)
    val valveBypassRight = _valveBypassRight
    private val _fanFront = MutableStateFlow(false)
    val fanFront = _fanFront
    private val _valveOutLeft = MutableStateFlow(false)
    val valveOutLeft = _valveOutLeft
    private val _valveOutRight = MutableStateFlow(false)
    val valveOutRight = _valveOutRight
    private val _valveWaterInlet = MutableStateFlow(false)
    val valveWaterInlet = _valveWaterInlet
    private val _valveCleanTab = MutableStateFlow(false)
    val valveCleanTab = _valveCleanTab
    private val _grinderLeft = MutableStateFlow(false)
    val grinderLeft = _grinderLeft
    private val _grinderRight = MutableStateFlow(false)
    val grinderRight = _grinderRight
    private val _boilerLeft = MutableStateFlow(false)
    val boilerLeft = _boilerLeft
    private val _boilerRight = MutableStateFlow(false)
    val boilerRight = _boilerRight
    private val _waterPump = MutableStateFlow(false)
    val waterPump = _waterPump
    private val _relayStandby = MutableStateFlow(false)
    val relayStandby = _relayStandby
    private val _fanLeft = MutableStateFlow(false)
    val fanLeft = _fanLeft
    private val _fanRight = MutableStateFlow(false)
    val fanRight = _fanRight

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.COMMON_REPLY, subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        DataCenter.unsubscribe(ReceivedDataType.COMMON_REPLY, subscriber)
    }

    fun getCoffeeStatus() {
        viewModelScope.launch {
            while (true) {
                CommandControlManager.sendTestCommand(INFO_COFFEE_STATUS_ID)
                delay(1000)
            }
        }
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.CommonReply) {
            if (data.commandId == INFO_COFFEE_STATUS_ID) {
                _valveBypassLeft.value = data.params[0] == ONE_IN_BYTE
                _valveBrewLeft.value = data.params[1] == ONE_IN_BYTE
                _valveOutLeft.value = data.params[2] == ONE_IN_BYTE
                _valveBypassRight.value = data.params[3] == ONE_IN_BYTE
                _valveBrewRight.value = data.params[4] == ONE_IN_BYTE
                _valveOutRight.value = data.params[5] == ONE_IN_BYTE
                _valveWaterInlet.value = data.params[6] == ONE_IN_BYTE
                _valveCleanTab.value = data.params[7] == ONE_IN_BYTE
                _waterPump.value = data.params[8] == ONE_IN_BYTE
                _grinderRight.value = data.params[9] == ONE_IN_BYTE
                _grinderLeft.value = data.params[10] == ONE_IN_BYTE
                _boilerLeft.value = data.params[11] == ONE_IN_BYTE
                _boilerRight.value = data.params[12] == ONE_IN_BYTE
                _relayStandby.value = data.params[13] == ONE_IN_BYTE
                _fanFront.value = data.params[14] == ONE_IN_BYTE
                _fanLeft.value = data.params[15] == ONE_IN_BYTE
                _fanRight.value = data.params[16] == ONE_IN_BYTE
            }
        }
    }

}