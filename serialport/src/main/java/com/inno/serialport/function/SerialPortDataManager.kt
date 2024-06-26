package com.inno.serialport.function

import androidx.annotation.WorkerThread
import com.inno.common.utils.Logger
import com.inno.serialport.bean.HandleResult
import com.inno.serialport.bean.SerialErrorType
import com.inno.serialport.function.chain.RealChainHandler
import com.inno.serialport.function.driver.RS485Driver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

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
        private const val MAX_HEARTBEAT_MISS_COUNT = 3
    }

    @Volatile
    private var isRunning = false
    private val _receivedDataFlow = MutableSharedFlow<HandleResult?>(
        replay = 0,
        extraBufferCapacity = 12,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedDataFlow: SharedFlow<HandleResult?> = _receivedDataFlow
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val driver = RS485Driver()
    private var heartBeatMode = false
    private var heartBeatMiss = AtomicInteger(0)
    private var pendingCommandData = AtomicInteger(0)
    private var retryCount = AtomicInteger(0)
    private val chain = RealChainHandler()

    suspend fun open() {
        Logger.d(TAG, "open driver")
        driver.open()
        isRunning = true
        heartBeatMode = true
        scope.launch {
            startHeartBeat()
        }
        scope.launch {
            receiveData()
        }
    }

    fun close() {
        Logger.d(TAG, "close driver")
        isRunning = false
        heartBeatMode = false
        pendingCommandData.set(0)
        heartBeatMiss.set(0)
        scope.coroutineContext.cancelChildren()
        driver.close()
    }

    fun sendCommand(command: String) {
        Logger.d(TAG, "sendCommand $command")
        heartBeatMode = false
        pendingCommandData.incrementAndGet()
        driver.send(command)
    }

    private suspend fun receiveData() {
        Logger.d(TAG, "receiveData $isRunning")
        while (isRunning) {
            delay(PULL_INTERVAL_MILLIS)
            val pullBufInfo = driver.receive()
            val result = chain.proceed(pullBufInfo)
            result?.let {
                Logger.d(TAG, "handle result: $it")
                processHeartBeat(it)
                _receivedDataFlow.emit(it)
            }
        }
    }

    private suspend fun startHeartBeat() {
        while (isRunning && heartBeatMode) {
            delay(PULL_INTERVAL_MILLIS)
            Logger.d(TAG, "startHeartBeat")
            driver.sendHeartBeat()
        }
    }

    private suspend fun processHeartBeat(result: HandleResult) {
        if (heartBeatMode && pendingCommandData.get() == 0) {
            if (result.heartbeatStatus) {
                heartBeatMiss.set(0)
            } else {
                val miss = heartBeatMiss.incrementAndGet()
                Logger.d(TAG, "heart beat miss time: $miss")
                if (miss >= MAX_HEARTBEAT_MISS_COUNT) {
                    val retry = retryCount.incrementAndGet()
                    if (retry > MAX_HEARTBEAT_MISS_COUNT) {
                        result.result = SerialErrorType.HEART_BEAT_MISS.errorMsg
                        result.heartbeatStatus = false
                        close()
                    } else {
                        close()
                        open()
                    }
                }
            }
        } else {
            if (pendingCommandData.decrementAndGet() == 0) {
                heartBeatMode = true
            }
        }
    }

}