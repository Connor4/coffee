package com.inno.coffee.function.makedrinks

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.utilities.INVALID_INT
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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// 1 多次点击需要可排队 done
// 2 停止、冲水跟制作饮品不一样，不可以多次点击 done
// 3 由于主副屏上的viewmodel独立，只能按照时间戳排队进去给manager处理队列 done
// 4 完成制作通知去除队列任务 done
// 5 副屏饮品id+100 done
// 6 主屏副屏独立使用handler处理各自任务，同用任务只让主屏handler执行即可 done
// TODO 7 制作异常，需要取消并执行清除操作。同时提供入头操作
object MakeLeftDrinksHandler {
    private const val TAG = "MakeLeftDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()
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
    private val _status = MutableStateFlow(MakeDrinkStatusEnum.LEFT_BREWING)
    val status: StateFlow<MakeDrinkStatusEnum> = _status.asStateFlow()

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    fun discardAndClear(index: Int, model: DrinksModel) {
        scope.launch {
            mutex.withLock {
                Logger.d(TAG, "index $index, processingProductId: $processingProductId, model:" +
                        "${model.productId}")
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

    fun executeNow(model: DrinksModel) {
        scope.launch {
            mutex.withLock {
                val productProfile =
                    ProductProfileManager.convertProductProfile(model.productId, true)
                SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
            }
        }
    }

    fun enqueueMessage(model: DrinksModel) {
        scope.launch {
            mutex.withLock {
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
            val productProfile =
                ProductProfileManager.convertProductProfile(processingProductId, true)
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
        }
    }

    private fun recycleMessage(index: Int) {
        if (messageHead == null || index < 0) {
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
        _queue.value = _queue.value.filter {
            it.productId != model.productId
        }
        _size.value = _queue.value.size
    }

    private fun parseReceivedData(data: Any) {
        val drinkData = data as ReceivedData.HeartBeat
        drinkData.makeDrink?.let { reply ->
            val status = reply.status
            val productId = reply.value
            val params = reply.params
            when (status) {
                MakeDrinkStatusEnum.LEFT_BREWING -> {}
                MakeDrinkStatusEnum.LEFT_BREW_COMPLETED -> {}
                MakeDrinkStatusEnum.LEFT_FINISHED -> {
                    if (processingProductId == productId) {
                        scope.launch {
                            mutex.withLock {
                                // finish, proceed next drink
                                recycleMessage(0)
                                processBrewParams(params)
                                handleMessage()
                            }
                        }
                    }
                }
                else -> {}
            }
            _status.value = status
        }
    }

    private fun processBrewParams(params: Int) {

    }

}