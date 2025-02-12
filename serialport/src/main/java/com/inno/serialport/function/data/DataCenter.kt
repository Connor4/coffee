package com.inno.serialport.function.data

import com.inno.serialport.function.CommunicationController
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

object DataCenter {
    private const val TAG = "DataCenter"
    private var collectJob: Job? = null
    private val subscribers = mutableMapOf<ReceivedDataType, MutableList<Subscriber>>()

    fun init() {
        collectJob = CoroutineScope(Dispatchers.IO).launch {
            CommunicationController.instance.receivedDataFlow.collect { data ->
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
                            notify(ReceivedDataType.HEARTBEAT_LIST, receivedData)
                        }
                        is ReceivedData.MakeDrinkReply -> {
                            notify(ReceivedDataType.MAKE_DRINK_REPLY, receivedData)
                        }
                        is ReceivedData.CommonReply -> {
                            notify(ReceivedDataType.COMMON_REPLY, receivedData)
                        }
                        is ReceivedData.FrontColor -> {
                            notify(ReceivedDataType.FRONT_COLOR, receivedData)
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
        subscribers.computeIfAbsent(type) { CopyOnWriteArrayList() }.add(subscriber)
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