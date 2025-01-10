package com.inno.serialport.function.driver

import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.statusenum.SerialErrorTypeEnum

interface IDriver {

    fun openSerialPort()
    fun closeSerialPort()
    fun readFromSerialPort(
        onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: (type: SerialErrorTypeEnum) -> Unit,
    )

    fun writeToSerialPort(frame: ByteArray)
    fun send(command: Short, infoSize: Int, address: Byte, commandInfo: ByteArray)
    fun receive(): List<PullBufInfo>
    fun heartbeat()

}