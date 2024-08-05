package com.inno.serialport.utilities

import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

sealed class HeartBeatReply {
    data class MakeDrink(var status: MakeDrinkStatusEnum, var productId: Int) : HeartBeatReply()
    data class Error(var status: ErrorStatusEnum, var value: Int) : HeartBeatReply()
    data class BoilerTemperature(var status: BoilerStatusEnum, var value: Int) : HeartBeatReply()
}