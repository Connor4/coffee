package com.inno.coffee.viewmodel.settings.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.ONE_IN_BYTE
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.INFO_STEAM_STATUS_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SteamStatusViewModel @Inject constructor(

) : ViewModel() {

    private val _processSteamLeft = MutableStateFlow(0)
    val processSteamLeft = _processSteamLeft
    private val _processHotWater = MutableStateFlow(0)
    val processHotWater = _processHotWater
    private val _processSteamRight = MutableStateFlow(0)
    val processSteamRight = _processSteamRight
    private val _actionSteamLeft = MutableStateFlow(0)
    val actionSteamLeft = _actionSteamLeft
    private val _actionHotWater = MutableStateFlow(0)
    val actionHotWater = _actionHotWater
    private val _actionSteamRight = MutableStateFlow(0)
    val actionSteamRight = _actionSteamRight
    private val _prodidSteamLeft = MutableStateFlow(0)
    val prodidSteamLeft = _prodidSteamLeft
    private val _prodidHotWater = MutableStateFlow(0)
    val prodidHotWater = _prodidHotWater
    private val _prodidSteamRight = MutableStateFlow(0)
    val prodidSteamRight = _prodidSteamRight
    private val _modidSteamLeft = MutableStateFlow(0)
    val modidSteamLeft = _modidSteamLeft
    private val _modidHotWater = MutableStateFlow(0)
    val modidHotWater = _modidHotWater
    private val _modidSteamRight = MutableStateFlow(0)
    val modidSteamRight = _modidSteamRight
    private val _numError = MutableStateFlow(0)
    val numError = _numError
    private val _numStop = MutableStateFlow(0)
    val numStop = _numStop
    private val _numWarning = MutableStateFlow(0)
    val numWarning = _numWarning

    private val _valveWaterInBoiler = MutableStateFlow(false)
    val valveWaterInBoiler = _valveWaterInBoiler
    private val _valveHotWater = MutableStateFlow(false)
    val valveHotWater = _valveHotWater
    private val _valveMixHotWater = MutableStateFlow(false)
    val valveMixHotWater = _valveMixHotWater
    private val _valveSteam1 = MutableStateFlow(false)
    val valveSteam1 = _valveSteam1
    private val _valveSteam2 = MutableStateFlow(false)
    val valveSteam2 = _valveSteam2
    private val _valveFoam1 = MutableStateFlow(false)
    val valveFoam1 = _valveFoam1
    private val _valveFoam2 = MutableStateFlow(false)
    val valveFoam2 = _valveFoam2
    private val _valvePurgeMix = MutableStateFlow(false)
    val valvePurgeMix = _valvePurgeMix
    private val _valvePurge = MutableStateFlow(false)
    val valvePurge = _valvePurge
    private val _valveWaterInlet = MutableStateFlow(false)
    val valveWaterInlet = _valveWaterInlet
    private val _waterPump = MutableStateFlow(false)
    val waterPump = _waterPump
    private val _steamHeating1 = MutableStateFlow(false)
    val steamHeating1 = _steamHeating1
    private val _steamHeating2 = MutableStateFlow(false)
    val steamHeating2 = _steamHeating2
    private val _steamAirPump = MutableStateFlow(false)
    val steamAirPump = _steamAirPump

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

    fun getSteamStatus() {
        viewModelScope.launch {
            while (true) {
                CommandControlManager.sendTestCommand(INFO_STEAM_STATUS_ID)
                delay(1000)
            }
        }
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.CommonReply) {
            if (data.commandId == INFO_STEAM_STATUS_ID) {
                _valveWaterInBoiler.value = data.params[1] == ONE_IN_BYTE
                _valveHotWater.value = data.params[3] == ONE_IN_BYTE
                _valveMixHotWater.value = data.params[5] == ONE_IN_BYTE
                _valveSteam1.value = data.params[7] == ONE_IN_BYTE
                _valveSteam2.value = data.params[9] == ONE_IN_BYTE
                _valveFoam1.value = data.params[11] == ONE_IN_BYTE
                _valveFoam2.value = data.params[13] == ONE_IN_BYTE
                _valvePurgeMix.value = data.params[15] == ONE_IN_BYTE
                _valvePurge.value = data.params[17] == ONE_IN_BYTE
                _valveWaterInlet.value = data.params[19] == ONE_IN_BYTE
                _waterPump.value = data.params[21] == ONE_IN_BYTE
                _steamHeating1.value = data.params[23] == ONE_IN_BYTE
                _steamHeating2.value = data.params[25] == ONE_IN_BYTE
                _steamAirPump.value = data.params[27] == ONE_IN_BYTE
            }
        }
    }

}