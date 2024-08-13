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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object MakeRightDrinksHandler {
    private const val TAG = "MakeRightDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()
    private var processingProductId = INVALID_INT
    private val _size = MutableStateFlow(0)
    val size: StateFlow<Int> = _size
    private val _status = MutableStateFlow(MakeDrinkStatusEnum.RIGHT_BREWING)
    val status: StateFlow<MakeDrinkStatusEnum> = _status
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
                    recycleMessage()
                    handleMessage()
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
                _size.value++
                Logger.d(TAG, "message: ${messageHead.toString()}")
                handleMessage()
            }
        }
    }

    // every time enqueue, only run for once. until get response
    // id parse to command, send command
    private suspend fun handleMessage() {
        if (messageHead != null && processingProductId == INVALID_INT) {
            processingProductId = messageHead!!.actionId
            val productProfile =
                ProductProfileManager.convertProductProfile(
                    processingProductId, true)
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
        }
    }

    private fun recycleMessage() {
        // recycle the message
        val p = messageHead
        messageHead = p!!.next
        DrinkMessage.recycleMessage(p)
        Logger.d(TAG, "message: ${messageHead.toString()}")

        _size.value--
        processingProductId = INVALID_INT
    }

    private fun parseReceivedData(data: Any) {
        val drinkData = data as ReceivedData.HeartBeat
        drinkData.makeDrink?.let { reply ->
            val status = reply.status
            val productId = reply.productId
            when (status) {
                MakeDrinkStatusEnum.LEFT_BREWING -> {}
                MakeDrinkStatusEnum.LEFT_BREW_COMPLETED -> {}
                MakeDrinkStatusEnum.LEFT_FINISHED -> {
                    if (processingProductId == productId) {
                        scope.launch {
                            mutex.withLock {
                                // finish, proceed next drink
                                recycleMessage()
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

}