package com.inno.serialport.function

import androidx.annotation.WorkerThread
import com.inno.serialport.bean.HandleResult
import com.inno.serialport.function.chain.RealHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * 1. string to json
 * 2. select chain to process, generate data bean
 * 3. as a repository to hold generate data
 * 4. need two function 1) repeat send get data 2) send command, when 2, 1 need stop
 *
 */
@WorkerThread
class SerialPortDataManager private constructor() {

    companion object {
        val instance: SerialPortDataManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SerialPortDataManager()
        }

        private const val TAG = "SerialPortDataManager"
        private const val PULL_INTERVAL_MILLIS = 500L
    }

    @Volatile
    private var isRunning = false
    private val _receivedDataFlow = MutableSharedFlow<HandleResult?>(
        replay = 0,
        extraBufferCapacity = 12,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedDataFlow: SharedFlow<HandleResult?> = _receivedDataFlow
    private val driver = RS485Driver()
    private var heartBeatMode = false

    @Volatile
    private var pendingCommandData = 0
    private val chain = RealHandler()

    suspend fun open() {
        driver.open()
        isRunning = true
        heartBeatMode = true
        receiveData()
    }

    fun close() {
        isRunning = false
        heartBeatMode = false
        pendingCommandData = 0
        driver.close()
    }

    fun sendCommand(command: String) {
        heartBeatMode = false
        driver.send(command)
        pendingCommandData++
    }

    private suspend fun receiveData() {
        while (isRunning) {
            delay(PULL_INTERVAL_MILLIS)
            val pullBufInfo = driver.receive()
            val result = chain.proceed(pullBufInfo)
            if (heartBeatMode && pendingCommandData == 0) {
                checkHeartBeat(result)
            } else {
                pendingCommandData--
                _receivedDataFlow.emit(result)
                if (pendingCommandData == 0) {
                    heartBeatMode = true
                    startHeartBeat()
                }
            }
        }
    }

    private suspend fun startHeartBeat() {
        while (isRunning && heartBeatMode) {
            delay(PULL_INTERVAL_MILLIS)
            driver.sendHeartBeat()
        }
    }

    private fun checkHeartBeat(result: HandleResult) {

    }

}