package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import com.inno.coffee.function.CommandControlManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.MAINTENANCE_FLOW_RATE_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_GRINDER_SENSOR_TEST_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TestFunctionViewModel @Inject constructor(

) : ViewModel() {

    private val _grinderLeftResult = MutableStateFlow(0)
    val grinderLeftResult = _grinderLeftResult
    private val _grinderRightResult = MutableStateFlow(0)
    val grinderRightResult = _grinderRightResult
    private val _flowRateLeftResult = MutableStateFlow(0)
    val flowRateLeftResult = _flowRateLeftResult
    private val _flowRateRightResult = MutableStateFlow(0)
    val flowRateRightResult = _flowRateRightResult

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

    fun sendTestCommand(id: Short) {
        CommandControlManager.sendTestCommand(id)
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.CommonReply) {
            when (data.commandId) {
                MAINTENANCE_GRINDER_SENSOR_TEST_ID -> {
                    val params = data.params
                    _grinderLeftResult.value =
                        ((params[3].toInt() and 0xFF) shl 8) or (params[2].toInt() and 0xFF)
                    _grinderRightResult.value =
                        ((params[5].toInt() and 0xFF) shl 8) or (params[4].toInt() and 0xFF)
                }
                MAINTENANCE_FLOW_RATE_TEST_ID -> {
                    val params = data.params
                    _flowRateLeftResult.value =
                        ((params[3].toInt() and 0xFF) shl 8) or (params[2].toInt() and 0xFF)
                    _flowRateRightResult.value =
                        ((params[5].toInt() and 0xFF) shl 8) or (params[4].toInt() and 0xFF)
                }
            }
        }
    }

}