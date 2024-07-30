package com.inno.serialport.function.data

interface Subscriber {
    fun onDataReceived(data: Any)
}