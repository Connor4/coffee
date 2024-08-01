package com.inno.coffee.makedrinks

import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND
import com.inno.serialport.utilities.MakeDrinkStatusEnum
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MakeLeftDrinksHandler {
    private const val TAG = "MakeLeftDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var processingProductId = -1
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.MAKE_DRINK, subscriber)
    }

    @Synchronized
    fun executeNow(productId: Int) {
        // rinse foam and steam need execute immediately, different from drinks
        scope.launch {
            // TODO actionId to Command, retrieve data from receipt files
            // TODO see {@link ProductProfile}
            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND, "")
        }
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
        if (messageHead != null && processingProductId == -1) {
            // id parse to command, send command
            processingProductId = messageHead!!.actionId
            scope.launch {
                // TODO actionId to Command, retrieve data from receipt files
                // TODO see {@link ProductProfile}
                SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND, "")
            }
            // recycle the message
            val p = messageHead
            messageHead = messageHead!!.next
            DrinkMessage.recycleMessage(p!!)
        }
    }

    private fun parseReceivedData(data: Any) {
        val drinkData = data as ReceivedData.DrinkData
        val status = drinkData.status
        val productId = drinkData.productId
        when (status) {
            MakeDrinkStatusEnum.LEFT_BREWING -> {}
            MakeDrinkStatusEnum.LEFT_BREWING_COMPLETE -> {}
            MakeDrinkStatusEnum.LEFT_FINISHED -> {
                if (processingProductId == productId) {
                    // finish, proceed next drink
                    processingProductId = -1
                    handleMessage()
                }
            }
            else -> {}
        }
    }

}