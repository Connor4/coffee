package com.inno.serialport.utilities

sealed class ReceivedData {
    data class SerialErrorData(var code: Int, var info: String) : ReceivedData()

    data class HeartBeat(
        var grinder: HeartBeatReply.GrinderPowderDosage? = null,
        var makeDrinkStatus: HeartBeatReply.MakeDrinkStatus? = null,
        var temperature: HeartBeatReply.BoilerTemperature? = null,
        var error: HeartBeatReply.Error? = null,
        var cleanMachine: HeartBeatReply.CleanMachineStatus? = null,
    ) : ReceivedData()

    data class HeatBeatList(var list: MutableList<HeartBeat> = mutableListOf()) : ReceivedData()

    data class MakeDrinkReply(var id: Int, var value: Int) : ReceivedData()

    data class CommonReply(var commandId: Short, var params: ByteArray = byteArrayOf()) :
            ReceivedData()

    data class FrontColor(var commandId: Short, var params: ByteArray = byteArrayOf()) :
            ReceivedData()
}