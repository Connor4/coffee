package com.inno.serialport.utilities

sealed class ReceivedData {
    data class SerialErrorData(var info: String, var reboot: Boolean = false) : ReceivedData()

    data class HeartBeat(var makeDrink: HeartBeatReply.MakeDrink? = null,
        var temperature: HeartBeatReply.BoilerTemperature? = null,
        var error: HeartBeatReply.Error? = null) : ReceivedData()

    data class HeatBeatList(var list: MutableList<HeartBeat> = mutableListOf()) :
        ReceivedData()
}