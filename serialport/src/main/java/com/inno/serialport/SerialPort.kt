package com.inno.serialport

import com.inno.common.utils.Logger
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class SerialPort @Throws(SecurityException::class, IOException::class) private constructor(
    val device: File,
    val baudRate: Int,
    val dataBits: Int,
    val stopBits: Int,
    val parity: Int,
    val flag: Int
) {
    companion object {
        init {
            System.loadLibrary("serial_port")
        }

        private const val TAG = "SerialPort"
        private const val SU_PATH = "/system/bin/su"
    }

    private var mFd: FileDescriptor? = null
    private var mFileInputStream: FileInputStream? = null
    private var mFileOutputStream: FileOutputStream? = null

    init {
        if (!device.canRead() || !device.canWrite()) {
            try {
                val su = Runtime.getRuntime().exec(SU_PATH)
                val cmd = "chmod 666 ${device.absolutePath}\nexit\n"
                su.outputStream.write(cmd.toByteArray())
                if (su.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
                    throw SecurityException()
                }
            } catch (e: Exception) {
                Logger.d(TAG, "serialport init exception:[$e]")
                throw SecurityException()
            }
        }

        mFd = open(device.absolutePath, baudRate, dataBits, parity, stopBits, flag)
        if (mFd == null) {
            Logger.d(TAG, "null() called")
            throw IOException()
        }
        mFileInputStream = FileInputStream(mFd)
        mFileOutputStream = FileOutputStream(mFd)
    }

    override fun toString(): String {
        return "SerialPort(device=${device.absolutePath}, baudRate=$baudRate, dataBits=$dataBits," +
                " stopBits=$stopBits, parity=$parity, flag=$flag)"
    }

    private external fun open(
        path: String, baudRate: Int, dataBits: Int, parity: Int,
        stopBits: Int, flag: Int
    ): FileDescriptor?

    private external fun close()

    private external fun test(): Int

    class Builder {
        private var portName: String = "defaultPort"
        private var device: File = File(portName)
        private var baudRate: Int = 9600
        private var dataBits: Int = 8
        private var stopBits: Int = 1
        private var parity: Int = 0
        private var flag: Int = -1

        fun setPortName(portName: String) = apply {
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

        fun build(): SerialPort {
            return SerialPort(device, baudRate, dataBits, stopBits, parity, flag)
        }

    }

}