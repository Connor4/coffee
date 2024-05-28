package com.inno.serialport

import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream

class SerialPort private constructor(
    val portName: String,
    val baudRate: Int,
    val dataBits: Int,
    val stopBits: Int,
    val parity: Int,
    val flag: Int
) {
    companion object {
        private const val TAG = "SerialPort"
    }

    private var mFd: FileDescriptor? = null
    private var mFileInputStream: FileInputStream? = null
    private var mFileOutputStream: FileOutputStream? = null

    init {

    }

    class Builder() {
        private var portName: String = "defaultPort"
        private var baudRate: Int = 9600
        private var dataBits: Int = 8
        private var stopBits: Int = 1
        private var parity: Int = 0
        private var flag: Int = -1

        fun setPortName(portName: String) = apply {
            this.portName = portName
        }

        fun baudRate(baudRate: Int) = apply {
            this.baudRate = baudRate
        }

        fun dataBits(dataBits: Int) = apply {
            this.dataBits = dataBits
        }

        fun parity(parity: Int) = apply {
            this.parity = parity
        }

        fun stopBits(stopBits: Int) = apply {
            this.stopBits = stopBits
        }

        fun flag(flag: Int) = apply {
            this.flag = flag
        }

        fun build() = apply {
            SerialPort(portName, baudRate, dataBits, stopBits, parity, flag)
        }

    }

    override fun toString(): String {
        return "SerialPort(portName='$portName', baudRate=$baudRate, dataBits=$dataBits, stopBits=$stopBits, parity=$parity, flag=$flag)"
    }


}