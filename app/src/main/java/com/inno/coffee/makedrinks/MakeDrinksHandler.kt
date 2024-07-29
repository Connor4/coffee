package com.inno.coffee.makedrinks

import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MakeDrinksHandler {
    private const val TAG = "MakeDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private

    @Synchronized
    fun executeNow(actionId: Int) {
        // rinse foam and steam need execute immediately, different from drinks

    }

    @Synchronized
    fun enqueueMessage(actionId: Int) {
        val message = DrinkMessage.obtainMessage(actionId)
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
        if (messageHead != null) {
            // id parse to command, send command
            val actionId = messageHead!!.actionId
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

}