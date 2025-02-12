package com.inno.coffee.ui.notice

import android.app.Application
import com.inno.coffee.R
import com.inno.coffee.data.DialogData
import com.inno.coffee.function.statistic.StatisticManager
import com.inno.coffee.utilities.errorMap
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object ErrorDataManager {
    private val TAG = "ErrorDataManager"
    private val _dialogDataList = MutableStateFlow<List<DialogData>>(emptyList())
    val dialogDataList: StateFlow<List<DialogData>> get() = _dialogDataList
    private val _warningExist = MutableStateFlow(false)
    val warningExist = _warningExist
    private val _userDismissed = MutableStateFlow(false)
    val userDismissed: StateFlow<Boolean> get() = _userDismissed
    private val _closeEvent = MutableSharedFlow<Unit>(replay = 0)
    val closeEvent: SharedFlow<Unit> get() = _closeEvent

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var errorCheckJob: Job? = null
    private val mutex = Mutex()
    private var application: Application? = null

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    fun init(context: Application) {
        application = context
        GlobalDialogLeftManager.init(context)
        GlobalDialogRightManager.init(context)
        DataCenter.subscribe(ReceivedDataType.SERIAL_PORT_ERROR, subscriber)
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT_LIST, subscriber)
    }

    private fun parseReceivedData(data: Any) {
        when (val receivedData = data as ReceivedData) {
            is ReceivedData.SerialErrorData -> handleError(receivedData.code)
            is ReceivedData.HeatBeatList -> {
                receivedData.list.forEach { heartbeat ->
                    heartbeat.error?.let { error ->
                        handleError(error.status.value)
                    }
                }
            }
            else -> {}
        }
    }

    private fun handleError(code: Int) {
        scope.launch {
            errorCheckJob?.cancel()
            mutex.withLock {
                val existIndex = _dialogDataList.value.indexOfFirst { it.errorCode == code }
                if (existIndex == -1) {
                    val dialogData = DialogData().apply {
                        errorCode = code
                        val detail = application?.resources?.getString(
                            errorMap[errorCode] ?: R.string.error_no_resource) ?: ""
                        message = "E $errorCode: $detail"
                        StatisticManager.countErrorHistory("E $errorCode", detail)
                    }
                    _dialogDataList.value += dialogData
                    _warningExist.value = true
                } else {
                    errorCheckJob = scope.launch {
                        delay(2000)
                        _dialogDataList.value = emptyList()
                        _warningExist.value = false
                    }
                }
                _userDismissed.value = false
            }
        }
    }

    fun setUserDismissed(dismissed: Boolean) {
        _userDismissed.value = dismissed
    }

    fun notifyClose() {
        _closeEvent.tryEmit(Unit)
    }
}