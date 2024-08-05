package com.inno.serialport.utilities

sealed class HeartBeatReply {
    data class MakeDrink(var status: MakeDrinkStatusEnum, var productId: Int) : HeartBeatReply()
    data class Error(var id: Int, var value: Int) : HeartBeatReply()
}