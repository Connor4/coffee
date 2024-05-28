package com.inno.serialport

import com.inno.common.utils.Logger
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.util.Vector

class SerialPortFinder {
    companion object {
        private const val TAG = "SerialPortFinder"
        private const val DRIVER_PATH = "/proc/tty/drivers"
        private const val DRIVER_NAME_LENGTH = 0x15
        private const val MIN_WORDS_COUNT = 5
        private const val DRIVER_TYPE_INDEX = 4
        private const val DRIVER_NAME_INDEX = 1
        private const val TARGET_DRIVER_TYPE = "serial"
    }

    private var mDrivers: Vector<Driver>? = null

    fun getAllDevicesPath(): Array<String> {
        val devicesPath = mutableListOf<String>()
        try {
            for (driver in getDrivers()) {
                for (device in driver.getDevices()) {
                    devicesPath.add(device.absolutePath)
                }
            }
        } catch (e: IOException) {
            Logger.d(TAG, "getAllDevicesPath() IOException: [$e]")
        }
        return devicesPath.toTypedArray()
    }

    fun getAllDevices(): Array<String> {
        val devices = mutableListOf<String>()
        try {
            for (driver in getDrivers()) {
                for (device in driver.getDevices()) {
                    val value = "${device.name} (${driver.driverName})"
                    devices.add(value)
                }
            }
        } catch (e: IOException) {
            Logger.d(TAG, "getAllDevices() IOException: [$e]")
        }
        return devices.toTypedArray()
    }

    private fun getDrivers(): Vector<Driver> {
        Logger.d(TAG, "getDrivers() called ${mDrivers?.size}")
        if (mDrivers == null) {
            mDrivers = Vector<Driver>()
            LineNumberReader(FileReader(DRIVER_PATH)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let {
                        processLine(it)
                    }
                }
            }

        }
        return mDrivers!!
    }

    private fun processLine(line: String) {
        val driverName = line.substring(0, DRIVER_NAME_LENGTH).trim()
        val words = line.split(Regex("\\s+"))
        if (words.size >= MIN_WORDS_COUNT && words[words.size - DRIVER_NAME_INDEX] == TARGET_DRIVER_TYPE) {
            val device = words[words.size - DRIVER_TYPE_INDEX]
            mDrivers!!.add(Driver(driverName, device))
        }
    }


    open class Driver(val driverName: String, private val root: String) {
        companion object {
            private const val TAG = "Driver"
            private const val ROOT_PATH = "/dev"
        }

        private var mDevices: Vector<File>? = null

        fun getDevices(): Vector<File> {
            Logger.d(TAG, "getDevices() called ${mDevices?.size}")
            if (mDevices == null) {
                mDevices = Vector<File>()
                val dev = File(ROOT_PATH)
                dev.listFiles()?.let {
                    for (file in it) {
                        if (file.absolutePath.startsWith(root)) {
                            Logger.d(TAG, "addDevice path:[${file.absolutePath}]")
                            mDevices!!.add(file)
                        }
                    }
                }

            }
            return mDevices!!
        }

    }

}