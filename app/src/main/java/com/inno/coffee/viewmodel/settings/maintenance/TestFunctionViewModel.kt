package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.ONE_IN_BYTE
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.MAINTENANCE_FLOW_RATE_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_GRINDER_SENSOR_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_MILK_SENSOR_LEFT_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_MILK_SENSOR_RIGHT_TEST_ID
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

    private val _status = MutableStateFlow(false)
    val status = _status
    private val _min = MutableStateFlow(0)
    val min = _min
    private val _max = MutableStateFlow(0)
    val max = _max
    private val _average = MutableStateFlow(0)
    val average = _average
    private val _offset = MutableStateFlow(0)
    val offset = _offset

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
                MAINTENANCE_MILK_SENSOR_LEFT_TEST_ID -> {
                    val params = data.params
                    _status.value = data.params[1] == ONE_IN_BYTE
                    _min.value =
                        ((params[3].toInt() and 0xFF) shl 8) or (params[2].toInt() and 0xFF)
                    _max.value =
                        ((params[5].toInt() and 0xFF) shl 8) or (params[4].toInt() and 0xFF)
                    _average.value =
                        ((params[7].toInt() and 0xFF) shl 8) or (params[6].toInt() and 0xFF)
                    _offset.value =
                        ((params[9].toInt() and 0xFF) shl 8) or (params[8].toInt() and 0xFF)
                }
                MAINTENANCE_MILK_SENSOR_RIGHT_TEST_ID -> {
                    val params = data.params
                    _status.value = data.params[1] == ONE_IN_BYTE
                    _min.value =
                        ((params[3].toInt() and 0xFF) shl 8) or (params[2].toInt() and 0xFF)
                    _max.value =
                        ((params[5].toInt() and 0xFF) shl 8) or (params[4].toInt() and 0xFF)
                    _average.value =
                        ((params[7].toInt() and 0xFF) shl 8) or (params[6].toInt() and 0xFF)
                    _offset.value =
                        ((params[9].toInt() and 0xFF) shl 8) or (params[8].toInt() and 0xFF)
                }
            }
        }
    }

}