package com.inno.serialport.utilities

sealed class ReceivedData {
    data class SerialErrorData(var code: Int, var info: String) : ReceivedData()

    data class HeartBeat(
        var grinder: HeartBeatReply.GrinderPowderDosage? = null,
        var makeDrink: HeartBeatReply.MakeDrink? = null,
        var temperature: HeartBeatReply.BoilerTemperature? = null,
        var error: HeartBeatReply.Error? = null,
    ) : ReceivedData()

    data class HeatBeatList(var list: MutableList<HeartBeat> = mutableListOf()) : ReceivedData()

    data class MakeDrinkReply(var id: Int, var value: Int) : ReceivedData()
}