package com.inno.coffee.function.makedrinks

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.utilities.HEAD_INDEX
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.PRODUCT_STOP
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

object MakeRightDrinksHandler {
    private const val TAG = "MakeRightDrinksHandler"
    private const val REPLY_WAIT_TIME = 2000L
    private const val RIGHT_OFFSET = 100
    private var messageHead: DrinkMessage? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()
    private var replyConfirmJob: Job? = null
    private var processingProductId = INVALID_INT
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }
    private val _queue = MutableStateFlow<List<DrinksModel>>(emptyList())
    val queue: StateFlow<List<DrinksModel>> = _queue.asStateFlow()
    private val _size = MutableStateFlow(0)
    val size: StateFlow<Int> = _size.asStateFlow()
    private val _status = MutableStateFlow(MakeDrinkStatusEnum.RIGHT_BREWING)
    val status: StateFlow<MakeDrinkStatusEnum> = _status.asStateFlow()

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    fun discardAndClear(index: Int, model: DrinksModel) {
        replyConfirmJob?.cancel()
        scope.launch {
            mutex.withLock {
                Logger.d(TAG,
                    "discard index $index, processingProductId: $processingProductId, model:" +
                            "${model.productId}")
                if (processingProductId == model.productId + RIGHT_OFFSET) {
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

    fun executeNow(model: DrinksModel) {
        Logger.d(TAG, "executeNow() called")
        scope.launch {
            mutex.withLock {
                val productProfile =
                    ProductProfileManager.convertProductProfile(model.productId + RIGHT_OFFSET,
                        false)
                SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)

                if (model.productId == PRODUCT_STOP && processingProductId != INVALID_INT) {
                    discardAndClear(HEAD_INDEX, _queue.value[HEAD_INDEX])
                }
            }
        }
    }

    fun enqueueMessage(model: DrinksModel) {
        scope.launch {
            mutex.withLock {
                val message = DrinkMessage.obtainMessage(model.productId + RIGHT_OFFSET)
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
            val productProfile =
                ProductProfileManager.convertProductProfile(processingProductId, false)
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
            waitForReplyConfirm()
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

    private fun addQueueSize(model: DrinksModel) {
        _queue.value += model
        _size.value = _queue.value.size
    }

    private fun minusQueueSize(model: DrinksModel) {
        if (_queue.value.isNotEmpty()) {
            _queue.value = _queue.value.filter {
                it.productId != model.productId
            }
            _size.value = _queue.value.size
        }
    }

    private fun waitForReplyConfirm() {
        replyConfirmJob?.cancel()
        replyConfirmJob = scope.launch {
            val result = withTimeoutOrNull(REPLY_WAIT_TIME) {
                delay(REPLY_WAIT_TIME + 1)
            }
            if (result == null) {
                discardAndClear(HEAD_INDEX, _queue.value[HEAD_INDEX])
            }
        }
    }

    private fun parseReceivedData(data: Any) {
        val drinkData = data as ReceivedData.HeartBeat
        drinkData.makeDrink?.let { reply ->
            val status = reply.status
            val productId = reply.value
            val params = reply.params
            when (status) {
                MakeDrinkStatusEnum.RIGHT_BREWING -> {
                    if (processingProductId == productId) {
                        replyConfirmJob?.cancel()
                    }
                }
                MakeDrinkStatusEnum.RIGHT_BREW_COMPLETED -> {}
                MakeDrinkStatusEnum.RIGHT_FINISHED -> {
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

                    }
                }
                else -> {}
            }
            _status.value = status
        }
    }

    private fun processBrewParams(params: ByteArray) {

    }

}