package com.inno.serialport.core

import com.inno.common.utils.Logger
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class SerialPort private constructor(
    val device: File,
    val portName: String,
    val baudRate: Int,
    val dataBits: Int,
    val stopBits: Int,
    val parity: Int,
    val flag: Int,
    val portFrameSize: Int
) {
    companion object {
        init {
            System.loadLibrary("serial_port")
        }

        private const val TAG = "SerialPort"
        private const val SU_PATH = "/system/bin/su"
    }

    private var mFd: FileDescriptor? = null
    var mFileInputStream: FileInputStream? = null
    var mFileOutputStream: FileOutputStream? = null

    fun openSerialPort(): Boolean {
        if (!device.canRead() || !device.canWrite()) {
            try {
                val su = Runtime.getRuntime().exec(SU_PATH)
                val cmd = "chmod 666 ${device.absolutePath}\nexit\n"
                su.outputStream.write(cmd.toByteArray())
                if (su.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
                    Logger.e(TAG, "openSerialPort failed, lack of permission")
                    return false
                }
            } catch (e: Exception) {
                Logger.e(TAG, "chmod exception:[$e]")
                return false
            }
        }

        mFd = open(device.absolutePath, baudRate, dataBits, parity, stopBits, flag)
        if (mFd == null) {
            Logger.e(TAG, "mFd is null")
        } else {
            mFileInputStream = FileInputStream(mFd)
            mFileOutputStream = FileOutputStream(mFd)
        }
        return mFd != null
    }

    fun closeSerialPort() {
        try {
            mFileInputStream?.close()
            mFileOutputStream?.close()
        } catch (e: IOException) {
            Logger.e(TAG, "closeSerialPort IOException $e")
        } finally {
            mFileInputStream = null
            mFileOutputStream = null
        }
        mFd?.let {
            close()
        }
        mFd = null
    }

    fun setSerialRTS(state: Boolean) {
        setRTS(state)
    }

    override fun toString(): String {
        return "SerialPort(device=${device.absolutePath}, baudRate=$baudRate, dataBits=$dataBits," +
                " stopBits=$stopBits, parity=$parity, flag=$flag)"
    }

    external fun open(
        path: String, baudRate: Int, dataBits: Int, parity: Int,
        stopBits: Int, flag: Int
    ): FileDescriptor?

    external fun close()

    external fun setRTS(state: Boolean)

    class Builder {
        private var portName: String = "defaultPort"
        private var device: File = File(portName)
        private var baudRate: Int = 9600
        private var dataBits: Int = 8
        private var stopBits: Int = 1
        private var parity: Int = 0
        private var flag: Int = -1
        private var portFrameSize: Int = 256

        fun portName(portName: String) = apply {
            this.portName = portName
            this.device = File(portName)
        }

        fun device(file: File) = apply {
            this.device = file
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

        fun portFrameSize(size: Int) = apply {
            this.portFrameSize = size
        }

        fun build(): SerialPort {
            return SerialPort(
                device,
                portName,
                baudRate,
                dataBits,
                stopBits,
                parity,
                flag,
                portFrameSize
            )
        }

    }

}