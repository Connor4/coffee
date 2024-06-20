package com.inno.coffee.data.home

import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.core.SerialPortFinder
import com.inno.serialport.function.SerialPortDataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SerialPortRepository @Inject constructor(
//    private val dataSource: SerialPortDataSource
) {
    val receivedDataFlow: SharedFlow<PullBufInfo?> = SerialPortDataManager.instance.receivedDataFlow
    private val _serialPortPath = MutableStateFlow<List<String>>(emptyList())
    val serialPortPath: StateFlow<List<String>> = _serialPortPath
    private val _serialPortDevices = MutableStateFlow<List<String>>(emptyList())
    val serialPortDevices: StateFlow<List<String>> = _serialPortDevices

    suspend fun openSerialPort() {
        SerialPortDataManager.instance.open()
    }

    fun closeSerialPort() {
        SerialPortDataManager.instance.close()
    }

    fun sendCommand(command: String) {
        SerialPortDataManager.instance.sendCommand(command)
    }

    fun fetchSerialPort() {
        val finder = SerialPortFinder()
        val path = finder.getAllDevicesPath()
        val devices = finder.getAllDevices()
        _serialPortPath.value = path
        _serialPortDevices.value = devices
    }

}