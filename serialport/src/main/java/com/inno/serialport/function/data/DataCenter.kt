package com.inno.serialport.function.data

import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object DataCenter {
    private const val TAG = "DataCenter"
    private var collectJob: Job? = null
    private val subscribers = mutableMapOf<ReceivedDataType, MutableList<Subscriber>>()

    fun init() {
        collectJob = CoroutineScope(Dispatchers.IO).launch {
            SerialPortDataManager.instance.receivedDataFlow.collect { data ->
                data?.let { receivedData ->
                    when (receivedData) {
                        is ReceivedData.SerialErrorData -> {
                            notify(ReceivedDataType.SERIAL_PORT_ERROR, receivedData)
                        }
                        is ReceivedData.HeartBeat -> {
                            notify(ReceivedDataType.HEARTBEAT, receivedData)
                        }
                        is ReceivedData.HeatBeatList -> {
                            receivedData.list.forEach {
                                notify(ReceivedDataType.HEARTBEAT, it)
                            }
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