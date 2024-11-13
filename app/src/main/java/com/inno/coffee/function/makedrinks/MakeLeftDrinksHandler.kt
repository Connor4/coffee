package com.inno.coffee.function.makedrinks

import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.utilities.HEAD_INDEX
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MAKE_DRINK_REPLY_VALUE
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import com.inno.common.utils.Logger
import com.inno.serialport.function.SerialPortDataManager
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

object MakeLeftDrinksHandler {
    private const val TAG = "MakeLeftDrinksHandler"
    private const val REPLY_WAIT_TIME = 2000L
    private var messageHead: DrinkMessage? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()
    private var productReplyConfirmJob: Job? = null
    private var operationReplyConfirmJob: Job? = null
    private var processingProductId = INVALID_INT
    private val _queue = MutableStateFlow<List<Formula>>(emptyList())
    val queue: StateFlow<List<Formula>> = _queue.asStateFlow()
    private val _size = MutableStateFlow(0)
    val size: StateFlow<Int> = _size.asStateFlow()
    private val _status = MutableStateFlow(MakeDrinkStatusEnum.LEFT_BREWING)
    val status: StateFlow<MakeDrinkStatusEnum> = _status.asStateFlow()
    private val _executingQueue = MutableStateFlow<List<Formula>>(emptyList())
    val executingQueue = _executingQueue.asStateFlow()

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
        DataCenter.subscribe(ReceivedDataType.MAKE_DRINK_REPLY, subscriber)
    }

    fun discardAndClear(index: Int, model: Formula) {
        scope.launch {
            mutex.withLock {
                Logger.d(TAG, "discard index $index, processingProductId: $processingProductId, " +
                        "model:" + "${model.productId}")
                if (processingProductId == model.productId) {
                    recycleMessage(index)
                    minusQueueSize(model)
                    processingProductId = INVALID_INT
                    handleMessage()
                } else {
                    recycleMessage(index)
                    minusQueueSize(model)
                }
            }
        }
    }

    fun executeNow(model: Formula) {
        scope.launch {
            mutex.withLock {
                _executingQueue.value += model
                val byteInfo =
                    ProductProfileManager.convertProductProfile(model.productId, true)
                SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, byteInfo.size,
                    byteInfo)
                waitForOperationReplyConfirm()
                Logger.d(TAG, "executeNow() called ${_executingQueue.value}")
            }
        }
    }

    fun enqueueMessage(model: Formula) {
        scope.launch {
            mutex.withLock {
                // build real drink queue
                val message = DrinkMessage.obtainMessage(model.productId)
                if (messageHead == null) {
                    messageHead = message
                } else {
                    var prev: DrinkMessage
                    var p = messageHead
                    while (true) {
                        prev = p!!
                        p = p.next
                        if (p == null) {
                            break
                        }
                    }
                    prev.next = message
                }
                addQueueSize(model)
                Logger.d(TAG, "message: ${messageHead.toString()}")
                handleMessage()
            }
        }
    }

    // every time enqueue, only run for once. until get response
    // id parse to command, send command
    private suspend fun handleMessage() {
        if (messageHead != null && processingProductId == INVALID_INT) {
            processingProductId = messageHead!!.productId
            _executingQueue.value += _queue.value[HEAD_INDEX]
            Logger.d(TAG, "handleMessage() called ${_executingQueue.value}")

            val byteInfo =
                ProductProfileManager.convertProductProfile(processingProductId, true)
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, byteInfo.size,
                byteInfo)
            waitForProductReplyConfirm()
        }
    }

    private fun recycleMessage(index: Int) {
        if (messageHead == null || index < 0) {
            return
        }

        if (index == 0) {
            messageHead = messageHead?.next
            return
        }

        var current = messageHead
        var currentIndex = 0
        while (current != null && currentIndex < index - 1) {
            current = current.next
            currentIndex++
        }
        if (current?.next != null) {
            val p = current.next
            current.next = current.next?.next
            DrinkMessage.recycleMessage(p!!)
        }
    }

    private fun addQueueSize(model: Formula) {
        _queue.value += model
        _size.value = _queue.value.size
    }

    private fun minusQueueSize(model: Formula) {
        if (_queue.value.isNotEmpty()) {
            _queue.value = _queue.value.filter {
                it.productId != model.productId
            }
            _size.value = _queue.value.size
        }
        _executingQueue.update { list ->
            list.filterNot { it.productId == processingProductId }
        }
    }

    private fun waitForProductReplyConfirm() {
        productReplyConfirmJob?.cancel()
        productReplyConfirmJob = scope.launch {
            val result = withTimeoutOrNull(REPLY_WAIT_TIME) {
                delay(REPLY_WAIT_TIME + 1)
            }
            if (result == null) {
                discardAndClear(HEAD_INDEX, _queue.value[HEAD_INDEX])
                Logger.d(TAG, "waitForProductReplyConfirm() called ${_queue.value}")
                // TODO show notification
            }
        }
    }

    private fun waitForOperationReplyConfirm() {
        operationReplyConfirmJob?.cancel()
        operationReplyConfirmJob = scope.launch {
            val result = withTimeoutOrNull(REPLY_WAIT_TIME) {
                delay(REPLY_WAIT_TIME + 1)
            }
            if (result == null) {
                _executingQueue.update { list ->
                    list.filterNot { ProductType.isOperationType(it.productType?.type) }
                }
                Logger.d(TAG, "waitForOperationReplyConfirm() called ${_executingQueue.value}")
            }
        }
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.MakeDrinkReply) {
            if (data.value == MAKE_DRINK_REPLY_VALUE) {
                if (data.id == processingProductId) {
                    productReplyConfirmJob?.cancel()
                } else {
                    operationReplyConfirmJob?.cancel()
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
                    MakeDrinkStatusEnum.LEFT_BREW_COMPLETED -> {}
                    MakeDrinkStatusEnum.LEFT_FINISHED -> {
                        if (processingProductId == productId) {
                            scope.launch {
                                mutex.withLock {
                                    // finish, proceed next drink
                                    discardAndClear(HEAD_INDEX, _queue.value[HEAD_INDEX])
                                    processBrewParams(params)
                                    handleMessage()
                                }
                            }
                        } else {
                            // TODO if not product, that must be operation.

                        }
                        _executingQueue.update { list ->
                            list.filterNot { it.productId == productId }
                        }
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