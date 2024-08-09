package com.inno.coffee.function.makedrinks

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.utilities.INVALID_INT
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// 1 多次点击需要可排队
// 2 停止、冲水跟制作饮品不一样，不可以多次点击
// 3 由于主副屏上的viewmodel独立，只能按照时间戳排队进去给manager处理队列
// 4 完成制作通知去除队列任务
// 5 副屏饮品id+100
// 6 主屏副屏独立使用handler处理各自任务，同用任务只让主屏handler执行即可
// 7 制作异常，需要取消并执行清除操作。
object MakeLeftDrinksHandler {
    private const val TAG = "MakeLeftDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()
    private var processingProductId = INVALID_INT
    private val _size = MutableStateFlow(0)
    val size: StateFlow<Int> = _size
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    fun discardAndClear(discardProductId: Int) {
        scope.launch {
            mutex.withLock {
                if (discardProductId == processingProductId) {

                }
            }
        }
    }

    fun executeNow(model: DrinksModel) {
        // rinse foam and steam need execute immediately, different from drinks
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
                _size.value++
//        Logger.d(TAG, "message: ${messageHead.toString()}")
                handleMessage()
            }
        }
    }

    // every time enqueue, only run for once. until get response
    private suspend fun handleMessage() {
        if (messageHead != null && processingProductId == INVALID_INT) {
            // id parse to command, send command
            processingProductId = messageHead!!.actionId
            val productProfile =
                ProductProfileManager.convertProductProfile(processingProductId, true)
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
            // recycle the message
            val p = messageHead
            messageHead = messageHead!!.next
            DrinkMessage.recycleMessage(p!!)
        }
    }

    private fun parseReceivedData(data: Any) {
        val drinkData = data as ReceivedData.HeartBeat
        drinkData.makeDrink?.let { reply ->
            val status = reply.status
            val productId = reply.productId
            when (status) {
                MakeDrinkStatusEnum.LEFT_BREWING -> {}
                MakeDrinkStatusEnum.LEFT_BREWING_COMPLETE -> {}
                MakeDrinkStatusEnum.LEFT_FINISHED -> {
                    if (processingProductId == productId) {
                        scope.launch {
                            mutex.withLock {
                                // finish, proceed next drink
                                processingProductId = INVALID_INT
                                handleMessage()
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }

}