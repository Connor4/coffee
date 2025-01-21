package com.inno.serialport.utilities

import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import com.inno.serialport.utilities.statusenum.CleanMachineEnum
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import com.inno.serialport.utilities.statusenum.GrinderStatusEnum
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

sealed class HeartBeatReply {

    data class MakeDrinkStatus(
        var status: MakeDrinkStatusEnum, var value: Int,
        var params: ByteArray = byteArrayOf(),
    ) : HeartBeatReply()

    data class Error(var status: ErrorStatusEnum, var value: Int) : HeartBeatReply()

    data class BoilerTemperature(
        var status: BoilerStatusEnum,
        var value: ByteArray = byteArrayOf(),
    ) : HeartBeatReply()

    data class GrinderPowderDosage(
        var status: GrinderStatusEnum,
        val value: Int,
    ) : HeartBeatReply()

    data class CleanMachineStatus(
        var status: CleanMachineEnum,
    ) : HeartBeatReply()
}