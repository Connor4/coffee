package com.inno.serialport.utilities

sealed class ReceivedData {
    data class SerialErrorData(var info: String, var reboot: Boolean = false) : ReceivedData()
    data class HeartBeat(var makeDrink: HeartBeatReply.MakeDrink? = null,
        var error: HeartBeatReply.Error? = null) : ReceivedData()
}