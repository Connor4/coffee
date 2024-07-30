package com.inno.serialport.function.data

import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object DataCenter {
    private var collectJob: Job? = null
    private val subscribers = mutableMapOf<ReceivedDataType, MutableList<Subscriber>>()

    fun init() {
        collectJob = CoroutineScope(Dispatchers.IO).launch {
            SerialPortDataManager.instance.receivedDataFlow.collect { data ->
                data?.let {
                    when (it) {
                        is ReceivedData.ErrorData -> {
                            notify(ReceivedDataType.ERROR, it)
                        }
                        is ReceivedData.HeartBeat -> {
                            notify(ReceivedDataType.HEARTBEAT, it)
                        }
                        is ReceivedData.DrinkData -> {
                            notify(ReceivedDataType.MAKE_DRINK, it)
                        }
                    }
                }
            }
        }
    }

    fun destroy() {
        collectJob?.cancel()
        subscribers.clear()
    }

    fun subscribe(type: ReceivedDataType, subscriber: Subscriber) {
        subscribers.computeIfAbsent(type) { mutableListOf() }.add(subscriber)
    }

    fun unsubscribe(type: ReceivedDataType, subscriber: Subscriber) {
        subscribers[type]?.remove(subscriber)
    }

    private fun notify(type: ReceivedDataType, data: Any) {
        subscribers[type]?.forEach {
            it.onDataReceived(data)
        }
    }

}