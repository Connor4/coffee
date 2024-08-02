package com.inno.serialport.function

import androidx.annotation.WorkerThread
import com.inno.common.utils.Logger
import com.inno.serialport.function.chain.RealChainHandler
import com.inno.serialport.function.driver.RS485Driver
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.SerialErrorTypeEnum
import com.inno.serialport.utilities.profile.ProductProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@WorkerThread
class SerialPortDataManager private constructor() {

    companion object {
        val instance: SerialPortDataManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SerialPortDataManager()
        }

        private const val TAG = "SerialPortDataManager"
        private const val PULL_INTERVAL_MILLIS = 500L
        private const val RECEIVE_INTERVAL_MILLIS = 100L
        private const val MAX_HEARTBEAT_MISS_COUNT = 3
        private const val MAX_RETRY_COUNT = 3
    }

    private val _receivedDataFlow = MutableSharedFlow<ReceivedData?>(
        replay = 0,
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedDataFlow: SharedFlow<ReceivedData?> = _receivedDataFlow
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val driver = RS485Driver()
    private val mutex = Mutex()
    private var heartBeatJob: Job? = null
    private var heartBeatMiss = 0
    private var retryCount = 0
    private val chain = RealChainHandler()

    suspend fun open() {
        Logger.d(TAG, "open driver")
        driver.open()
        scope.launch {
            startHeartBeat()
        }
    }

    fun close() {
        Logger.d(TAG, "close driver")
        heartBeatMiss = 0
        scope.coroutineContext.cancelChildren()
        driver.close()
    }

    suspend fun sendCommand(command: Short, productProfile: ProductProfile?) {
        Logger.d(TAG, "sendCommand command: $command, productProfile: $productProfile")
        productProfile?.let {
            mutex.withLock {
                heartBeatJob?.cancel()
                driver.send(command, productProfile)
                // let heart beat response
                startHeartBeat()
            }
        }
    }

    private suspend fun receiveData() {
        delay(RECEIVE_INTERVAL_MILLIS)
        Logger.d(TAG, "receiveData() called $retryCount")
        val pullBufInfo = driver.receive()
        val result = chain.proceed(pullBufInfo)
        result?.let {
            processRetry(it)
            Logger.d(TAG, "receiveData() data $it")
            _receivedDataFlow.emit(it)
        }
    }

    private suspend fun startHeartBeat() {
        heartBeatJob?.cancel()
        heartBeatJob = scope.launch {
            Logger.d(TAG, "startHeartBeat() called")
            while (isActive) {
                // delay must stay here, or reopen will recall this and
                // interrupt delay.
                delay(PULL_INTERVAL_MILLIS)
                mutex.withLock {
                    driver.sendHeartBeat()
                    receiveData()
                }
            }
        }
    }

    private suspend fun processRetry(receivedData: ReceivedData) {
        if (receivedData is ReceivedData.HeartBeat) {
            if (receivedData.heartbeatStatus) {
                heartBeatMiss = 0
                retryCount = 0
            } else {
                if (++heartBeatMiss >= MAX_HEARTBEAT_MISS_COUNT) {
                    if (++retryCount > MAX_RETRY_COUNT) {
                        receivedData.reboot = true
                        receivedData.info = SerialErrorTypeEnum.HEART_BEAT_MISS.errorMsg
                        close()
                    } else {
                        close()
                        open()
                    }
                }
            }
        } else if (receivedData is ReceivedData.ErrorData) {
            if (++retryCount > MAX_RETRY_COUNT) {
                receivedData.reboot = true
                receivedData.info = SerialErrorTypeEnum.READ_NO_DATA.errorMsg
                close()
            } else {
                close()
                open()
            }
        }
    }

}