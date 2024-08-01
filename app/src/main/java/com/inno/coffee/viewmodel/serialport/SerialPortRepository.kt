package com.inno.coffee.viewmodel.serialport

import com.inno.serialport.core.SerialPortFinder
import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND
import com.inno.serialport.utilities.ReceivedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SerialPortRepository @Inject constructor(
//    private val dataSource: SerialPortDataSource
) {
    val receivedDataFlow: Flow<ReceivedData?> = SerialPortDataManager.instance.receivedDataFlow
        .map {
            // todo 转换数据
            it
        }
    private val _serialPortPath = MutableStateFlow<List<String>>(emptyList())
    val serialPortPath: StateFlow<List<String>> = _serialPortPath
    private val _serialPortDevices = MutableStateFlow<List<String>>(emptyList())
    val serialPortDevices: StateFlow<List<String>> = _serialPortDevices

    suspend fun openSerialPort() {
        SerialPortDataManager.instance.open()
        DataCenter.init()
    }

    fun closeSerialPort() {
        SerialPortDataManager.instance.close()
        DataCenter.destroy()
    }

    suspend fun sendCommand(command: String) {
        SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND, command)
    }

    fun fetchSerialPort() {
        val finder = SerialPortFinder()
        val path = finder.getAllDevicesPath()
        val devices = finder.getAllDevices()
        _serialPortPath.value = path
        _serialPortDevices.value = devices
    }

}