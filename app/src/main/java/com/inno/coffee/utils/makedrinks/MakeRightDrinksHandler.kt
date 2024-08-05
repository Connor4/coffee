package com.inno.coffee.utils.makedrinks

import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utils.formula.ProductProfileManager
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
import kotlinx.coroutines.launch

object MakeRightDrinksHandler {
    private const val TAG = "MakeRightDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var processingProductId = INVALID_INT
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    @Synchronized
    fun enqueueMessage(productId: Int) {
        val message = DrinkMessage.obtainMessage(productId)
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
                    ProductProfileManager.convertProductProfile(processingProductId, false)
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
                MakeDrinkStatusEnum.RIGHT_BREWING -> {}
                MakeDrinkStatusEnum.RIGHT_BREWING_COMPLETE -> {}
                MakeDrinkStatusEnum.RIGHT_FINISHED -> {
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