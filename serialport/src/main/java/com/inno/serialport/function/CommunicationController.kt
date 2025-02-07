package com.inno.serialport.function

import androidx.annotation.WorkerThread
import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
import com.inno.serialport.function.chain.RealChainHandler
import com.inno.serialport.function.driver.Command
import com.inno.serialport.function.driver.IDriver
import com.inno.serialport.function.driver.RS485Driver
import com.inno.serialport.utilities.FRAME_ADDRESS_2
import com.inno.serialport.utilities.HEARTBEAT_COMMAND_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.LinkedBlockingQueue

@WorkerThread
class CommunicationController private constructor() {

    companion object {
        val instance: CommunicationController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CommunicationController()
        }
        private const val TAG = "CommunicationController"
        private const val PULL_INTERVAL_MILLIS = 500L
        private const val RECEIVE_INTERVAL_MILLIS = 100L
    }

    private val _receivedDataFlow = MutableSharedFlow<ReceivedData?>(
        replay = 0,
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val receivedDataFlow: SharedFlow<ReceivedData?> = _receivedDataFlow
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var heartbeatJob: Job? = null
    private val heartbeatArray = byteArrayOf()
    private val commandDriver = RS485Driver()
    private val frontColorDriver = RS485Driver(devicePath = "/dev/ttyS8")
    private val chain = RealChainHandler()
    private val commandQueueA = LinkedBlockingQueue<Command>()
    private val commandQueueB = LinkedBlockingQueue<Command>()
    private val mutexA = Mutex()
    private val mutexB = Mutex()
    private val receivedDataChannel = Channel<List<PullBufInfo>>(Channel.UNLIMITED)

    init {
        processDriverQueue(commandQueueA, commandDriver, mutexA, ::startHeartBeat)
        processDriverQueue(commandQueueB, frontColorDriver, mutexB, null)
        processReceivedInfo()
        startHeartBeat()
    }

    fun openDriver() {
        Logger.d(TAG, "openDriver() called")
        commandDriver.openSerialPort()
        frontColorDriver.openSerialPort()
    }

    fun closeDriver() {
        Logger.d(TAG, "closeDriver() called")
        commandDriver.closeSerialPort()
        frontColorDriver.closeSerialPort()
        heartbeatJob?.cancel()
    }

    fun sendCommand(commandId: Short, infoSize: Int, commandInfo: ByteArray) {
        Logger.d(TAG, "sendCommand() called with: commandId = $commandId, infoSize = $infoSize," +
                " commandInfo = ${commandInfo.toHexString()}")
        commandQueueA.offer(Command(commandId, infoSize, FRAME_ADDRESS_2, commandInfo))
    }

    fun sendFrontColorCommand(
        commandId: Short, infoSize: Int, address: Byte, commandInfo: ByteArray,
    ) {
        commandQueueB.offer(Command(commandId, infoSize, address, commandInfo))
    }

    private fun startHeartBeat() {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch {
            while (isActive) {
                delay(PULL_INTERVAL_MILLIS)
                sendCommand(HEARTBEAT_COMMAND_ID, 0, heartbeatArray)
            }
        }
    }

    private fun processDriverQueue(
        commandQueue: LinkedBlockingQueue<Command>, driver: IDriver, mutex: Mutex,
        handleHeartbeat: (() -> Unit)?,
    ) {
        scope.launch {
            while (isActive) {
                val command = commandQueue.take()
                mutex.withLock {
                    driver.send(command.id, command.size, command.address, command.data)
                    val response = withTimeoutOrNull(RECEIVE_INTERVAL_MILLIS) {
                        driver.receive()
                    }
                    response?.let {
                        receivedDataChannel.send(it)
                    }
                    handleHeartbeat?.invoke()
                }
            }
        }
    }

    private fun processReceivedInfo() {
        scope.launch {
            for (pullInfoList in receivedDataChannel) {
                pullInfoList.forEach { pullInfo ->
                    chain.proceed(pullInfo)?.let {
                        _receivedDataFlow.emit(it)
                    }
                }
            }
        }
    }


}