package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.ONE_IN_BYTE
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.STEAM_INPUT_COMMAND_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceFunctionViewModel @Inject constructor(

) : ViewModel() {

    private val _steamPressure = MutableStateFlow(0f)
    val steamPressure = _steamPressure
    private val _securityLevel = MutableStateFlow(true)
    val securityLevel = _securityLevel
    private val _workLevel = MutableStateFlow(true)
    val workLevel = _workLevel

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

    fun getSteamInputs() {
        viewModelScope.launch {
            while (true) {
                CommandControlManager.sendTestCommand(STEAM_INPUT_COMMAND_ID)
                delay(1000)
            }
        }
    }

    fun sendTestCommand(id: Short) {
        CommandControlManager.sendTestCommand(id)
    }

    fun sendTestCommand(id: Short, param: Int) {
        CommandControlManager.sendTestCommand(id, param)
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.CommonReply) {
            if (data.commandId == STEAM_INPUT_COMMAND_ID) {
                val params = data.params
                val steamPressure =
                    ((params[1].toInt() and 0xFF) shl 8) or (params[0].toInt() and 0xFF)
                _steamPressure.value = steamPressure / 10f
                _securityLevel.value = params[3] == ONE_IN_BYTE
                _workLevel.value = params[5] == ONE_IN_BYTE
            }
        }
    }

}