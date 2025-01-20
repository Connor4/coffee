package com.inno.coffee.function.makedrinks

import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.utilities.MAKE_DRINK_REPLY_VALUE
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import com.inno.common.utils.Logger
import com.inno.serialport.function.CommunicationController
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

object MakeRightDrinksHandler {
    private const val TAG = "MakeRightDrinksHandler"
    private const val REPLY_WAIT_TIME = 2000L
    private const val RIGHT_OFFSET = 100
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()
    private var productReplyConfirmJob: Job? = null
    private var operationReplyConfirmJob: Job? = null
    private val _size = MutableStateFlow(0)
    val size: StateFlow<Int> = _size.asStateFlow()
    private val _status = MutableStateFlow(MakeDrinkStatusEnum.LEFT_BREWING)
    val status: StateFlow<MakeDrinkStatusEnum> = _status.asStateFlow()
    private val _productQueue = MutableStateFlow<List<Formula>>(emptyList())
    val productQueue = _productQueue.asStateFlow()
    private val _operationQueue = MutableStateFlow<List<Formula>>(emptyList())
    val operationQueue = _operationQueue.asStateFlow()
    private val _extractionTime = MutableStateFlow(0f)
    val extractionTime = _extractionTime
    private var countdownJob: Job? = null
    private val commandCallbackMap = mutableMapOf<Int, (Boolean) -> Unit>()

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
        DataCenter.subscribe(ReceivedDataType.MAKE_DRINK_REPLY, subscriber)
        scope.launch {
            _productQueue.collect { queue ->
                Logger.d(TAG, "null() called with: queue = ${queue.isEmpty()}")
                if (queue.isNotEmpty()) {
                    queue.firstOrNull()?.also { formula ->
                        val coffee = ProductType.isCoffeeType(formula.productType?.type)
                        if (coffee) {
                            startCountdownExtractTime()
                        }
                    }
                } else {
                    stopCountdownExtractTime()
                }
            }
        }
    }

    fun executeNow(formula: Formula, callback: (Boolean) -> Unit = {}) {
        scope.launch {
            mutex.withLock {
                _operationQueue.value += formula
                Logger.d(TAG, "executeNow() called operationId: ${formula.productId}")

                val byteInfo = ProductProfileManager.convertProductProfile(formula, false)
                CommunicationController.instance.sendCommand(MAKE_DRINKS_COMMAND_ID,
                    byteInfo.size, byteInfo)
                waitForOperationReplyConfirm(formula.productId, callback)
            }
        }
    }

    fun enqueueMessage(formula: Formula, callback: (Boolean) -> Unit = {}) {
        scope.launch {
            mutex.withLock {
                _productQueue.value += formula
                Logger.d(TAG, "handleMessage() called processingProductId: ${formula.productId}")

                val byteInfo = ProductProfileManager.convertProductProfile(formula, true)
                CommunicationController.instance.sendCommand(MAKE_DRINKS_COMMAND_ID,
                    byteInfo.size, byteInfo)
                waitForProductReplyConfirm(formula.productId, callback)
            }
        }
    }

    private fun waitForProductReplyConfirm(productId: Int, callback: (Boolean) -> Unit) {
        productReplyConfirmJob?.cancel()
        productReplyConfirmJob = scope.launch {
            commandCallbackMap[productId] = callback
            val result = withTimeoutOrNull(REPLY_WAIT_TIME) {
                delay(REPLY_WAIT_TIME + 1)
            }
            if (result == null) {
                Logger.d(TAG, "waitForProductReplyConfirm() called")
                callback(false)
                clearProductQueue()
            }
        }
    }

    private fun waitForOperationReplyConfirm(productId: Int, callback: (Boolean) -> Unit) {
        operationReplyConfirmJob?.cancel()
        operationReplyConfirmJob = scope.launch {
            commandCallbackMap[productId] = callback
            val result = withTimeoutOrNull(REPLY_WAIT_TIME) {
                delay(REPLY_WAIT_TIME + 1)
            }
            if (result == null) {
                callback(false)
                _operationQueue.update { list ->
                    list.filterNot { ProductType.isOperationType(it.productType?.type) }
                }
                Logger.d(TAG, "waitForOperationReplyConfirm() called ${_productQueue.value}")
            }
        }
    }

    fun finishAndClear(productId: Int) {
        scope.launch {
            mutex.withLock {
                _productQueue.update { list ->
                    list.filterNot { it.productId == productId }
                }
            }
        }
    }

    fun clearProductQueue() {
        scope.launch {
            mutex.withLock {
                _productQueue.update { emptyList() }
            }
        }
    }

    private fun startCountdownExtractTime() {
        _extractionTime.value = 0f
        if (countdownJob == null || countdownJob?.isCancelled == true) {
//            if (ProductType.isCoffeeType(formula?.productType?.type)) {
            countdownJob = scope.launch {
                while (isActive) {
                    delay(100)
                    _extractionTime.value += 0.1f
                }
            }
//            }
            Logger.d(TAG, "startCountdownExtractTime() called $countdownJob")
        }
    }

    private fun stopCountdownExtractTime() {
        Logger.d(TAG, "stopCountdownExtractTime() called $countdownJob")
        countdownJob?.cancel()
        countdownJob = null
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.MakeDrinkReply) {
            if (data.value == MAKE_DRINK_REPLY_VALUE) {
                _productQueue.value.forEach {
                    val processingProductId = it.productId
                    if (data.id == processingProductId) {
                        productReplyConfirmJob?.cancel()
                        commandCallbackMap[processingProductId]?.invoke(true)
                        commandCallbackMap.remove(processingProductId)
                    }
                }
                _operationQueue.value.forEach {
                    val processingProductId = it.productId
                    if (data.id == processingProductId) {
                        operationReplyConfirmJob?.cancel()
                        commandCallbackMap[processingProductId]?.invoke(true)
                        commandCallbackMap.remove(processingProductId)
                    }
                }
            }
        } else if (data is ReceivedData.HeartBeat) {
            data.makeDrinkStatus?.let { reply ->
                val status = reply.status
                val productId = reply.value
                val params = reply.params
                when (status) {
                    MakeDrinkStatusEnum.LEFT_BREWING -> {

                    }
                    MakeDrinkStatusEnum.LEFT_BREW_COMPLETED -> {

                    }
                    MakeDrinkStatusEnum.LEFT_FINISHED -> {
                        finishAndClear(productId)
                        processBrewParams(params)
                    }
                    else -> {}
                }
                _status.value = status
            }
        }
    }

    private fun processBrewParams(params: ByteArray) {

    }

}