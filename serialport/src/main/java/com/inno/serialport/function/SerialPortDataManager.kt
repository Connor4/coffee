package com.inno.serialport.function

import com.inno.serialport.bean.PullBufInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * 1. string to json
 * 2. select chain to process, generate data bean
 * 3. as a repository to hold generate data
 * 4. need two function 1) repeat send get data 2) send command, when 2, 1 need stop
 *
 */
class SerialPortDataManager private constructor() {

    companion object {
        val instance: SerialPortDataManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SerialPortDataManager()
        }

        private const val TAG = "SerialPortDataManager"
        private const val PULL_INTERVAL_MILLIS = 500L
    }

    private val driver = RS485Driver()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _receivedDataFlow = MutableSharedFlow<PullBufInfo?>(
        replay = 0,
        extraBufferCapacity = 12,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedDataFlow: SharedFlow<PullBufInfo?> = _receivedDataFlow

    fun init() {
        scope.launch {
            withContext(Dispatchers.IO) {
                driver.open()
            }
        }
        scope.launch {
            receiveData()
        }
    }

    suspend fun sendCommand(command: String) {
        withContext(Dispatchers.IO) {
//            driver.send(command)

            val createInfo = createInfo()
            val infoString = Json.encodeToString(createInfo)
            driver.send(infoString)
        }
    }

    private suspend fun receiveData() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                delay(PULL_INTERVAL_MILLIS)
                val receive = driver.receive()
                receive?.let {
                    _receivedDataFlow.emit(it)
                }
            }
        }
    }

}