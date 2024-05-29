package com.inno.serialport.core

import com.inno.common.utils.Logger
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.util.Vector

/**
 * TODO 可以整理getAll方法为同一个方法，返回一个pair数据
 */
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
            Logger.e(TAG, "getAllDevicesPath() IOException: [$e]")
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
            Logger.e(TAG, "getAllDevices() IOException: [$e]")
        }
        return devices.toTypedArray()
    }

    /**
     * 获取内容示例：
     * serial              /dev/ttyS  4 64-67 serial
     * pty_slave           /dev/pts  136 0-255 pty:slave
     * pty_master          /dev/ptm  128 0-255 pty:master
     * unknown             /dev/tty   4 1-63 console
     *
     */
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

    /**
     * line内容:
     * serial              /dev/ttyS  4 64-67 serial
     *
     * split:
     *["serial", "/dev/ttyS", "4", "64-67", "serial"]
     *
     *w[w.length - 4] 对应于数组中的第二个元素 /dev/ttyS，表示设备路径。
     */
    private fun processLine(line: String) {
        val driverName = line.substring(0, DRIVER_NAME_LENGTH).trim()
        val words = line.split(Regex("\\s+"))
        if (words.size >= MIN_WORDS_COUNT && words[words.size - DRIVER_NAME_INDEX] == TARGET_DRIVER_TYPE) {
            val driverRootPath = words[words.size - DRIVER_TYPE_INDEX]
            Logger.d(TAG, "deviceName: $driverName deviceRootPath: $driverRootPath")
            mDrivers!!.add(Driver(driverName, driverRootPath))
        }
    }


    open class Driver(val driverName: String, private val driverRootPath: String) {
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
                        if (file.absolutePath.startsWith(driverRootPath)) {
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