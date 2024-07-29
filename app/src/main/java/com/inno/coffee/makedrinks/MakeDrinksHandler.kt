package com.inno.coffee.makedrinks

import com.inno.common.utils.Logger
import com.inno.serialport.function.SerialPortDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MakeDrinksHandler {
    private const val TAG = "MakeDrinksHandler"
    private var messageHead: DrinkMessage? = null
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

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
        Logger.d(TAG, "message: ${messageHead.toString()}")
        //        handleMessage()
    }

    // every enqueue, he can just only run for once. until get response
    private fun handleMessage() {
        if (messageHead != null) {
            // id parse to command, send command
            val actionId = messageHead!!.actionId
            scope.launch {
                // TODO actionId to Command
                SerialPortDataManager.instance.sendCommand("")
            }
            // recycle the message
            val p = messageHead
            messageHead = messageHead!!.next
            DrinkMessage.recycleMessage(p!!)
        }
    }

}