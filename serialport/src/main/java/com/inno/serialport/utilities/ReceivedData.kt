package com.inno.serialport.utilities

sealed class ReceivedData {
    data class ErrorData(var info: String, var reboot: Boolean = false) : ReceivedData()
    data class PartData(var info: String) : ReceivedData()
    data class HeartBeat(var heartbeatStatus: Boolean = false, var reboot: Boolean = false,
        var info: String = "") : ReceivedData()
}