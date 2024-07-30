package com.inno.serialport.utilities

sealed class ReceivedData {
    data class ErrorData(var info: String, var reboot: Boolean = false) : ReceivedData()
    data class DrinkData(var status: MakeDrinkStatusEnum, var productId: Int) : ReceivedData()
    data class HeartBeat(var heartbeatStatus: Boolean = false, var reboot: Boolean = false,
        var info: String = "") : ReceivedData()
}