package com.inno.serialport.driver

import com.inno.serialport.core.SerialPort
import com.inno.serialport.core.SerialPortManager

class RS485Driver(
    devicePath: String,
    baudRate: Int,
    dataBits: Int,
    stopBits: Int,
    parity: Int,
    flag: Int
) : Driver {
    private var mSerialPort: SerialPort? = null
    val onSuccess: (buffer: ByteArray, size: Int) -> Unit = { buffer, size ->

    }

    val onFailure: () -> Unit = {

    }

    init {
        mSerialPort = SerialPort.Builder()
            .portName(devicePath)
            .baudRate(baudRate)
            .dataBits(dataBits)
            .stopBits(stopBits)
            .parity(parity)
            .flag(flag)
            .build()
        SerialPortManager.start(mSerialPort!!, onSuccess, onFailure)
    }

    override fun send(data: ByteArray) {
//        serialPort?.setRTS(true)
//        serialPort?.setRTS(false)
        mSerialPort?.let {
            SerialPortManager.writeData(it, data)
        }
    }

    override fun receive(): ByteArray? {
        val buffer = ByteArray(1024)
        val size = mSerialPort?.mFileInputStream?.read(buffer)

        return size?.let { buffer.copyOf(it) }
    }

    fun close() {
        mSerialPort?.let {
            SerialPortManager.stop(it)
        }
    }
}