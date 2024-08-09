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

object MakeLeftDrinksHandler {
    private const val TAG = "MakeLeftDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
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

    @Synchronized
    fun executeNow(model: DrinksModel) {
        // rinse foam and steam need execute immediately, different from drinks
        scope.launch {
            val productProfile = ProductProfileManager.convertProductProfile(model.productId, true)
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
        }
    }

    @Synchronized
    fun enqueueMessage(model: DrinksModel) {
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

    // every time enqueue, only run for once. until get response
    private fun handleMessage() {
        if (messageHead != null && processingProductId == INVALID_INT) {
            // id parse to command, send command
            processingProductId = messageHead!!.actionId
            scope.launch {
                val productProfile =
                    ProductProfileManager.convertProductProfile(processingProductId, true)
                SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, productProfile)
            }
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
                        // finish, proceed next drink
                        processingProductId = INVALID_INT
                        handleMessage()
                    }
                }
                else -> {}
            }
        }
    }

}