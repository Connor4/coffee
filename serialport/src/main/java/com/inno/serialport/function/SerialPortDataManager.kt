package com.inno.serialport.function

import com.inno.serialport.bean.PullBufInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    @Volatile
    private var isRunning = false
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _receivedDataFlow = MutableSharedFlow<PullBufInfo?>(
        replay = 0,
        extraBufferCapacity = 12,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedDataFlow: SharedFlow<PullBufInfo?> = _receivedDataFlow

    fun open() {
        scope.launch {
            withContext(Dispatchers.IO) {
                driver.open()
                isRunning = true
                receiveData()
            }
        }
    }

    fun close() {
        scope.launch {
            withContext(Dispatchers.IO) {
                isRunning = false
                driver.close()
            }
        }
    }

    suspend fun sendCommand(command: String) {
        withContext(Dispatchers.IO) {
            driver.send(command)
        }
    }

    private suspend fun receiveData() {
        while (isRunning) {
            delay(PULL_INTERVAL_MILLIS)
            _receivedDataFlow.emit(driver.receive())
        }
    }

}