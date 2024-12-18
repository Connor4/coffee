package com.inno.coffee.viewmodel.settings.machinetest.serialport

import com.inno.serialport.core.SerialPortFinder
import com.inno.serialport.function.SerialPortDataManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.profile.ProductProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.nio.ByteBuffer
import java.nio.ByteOrder
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

    suspend fun sendCommand(productProfile: ProductProfile?) {
        productProfile?.let {
            val componentSize = productProfile.componentProfileList.componentList.size
            // id2 + preFlush2 + postFlush2 + ComponentProfileList: num2 + COMPONENT_SIZE * (comid2 + 2*para6)
            val productFileSize = 8 + 14 * componentSize
            val serializeBuffer = ByteBuffer.allocate(productFileSize)
            serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)
            serializeBuffer.putShort(productProfile.productId)
            serializeBuffer.putShort(productProfile.preFlush)
            serializeBuffer.putShort(productProfile.postFlush)
            serializeBuffer.putShort(productProfile.componentProfileList.componentNum)

            for (i in 0 until componentSize) {
                val componentProfile = productProfile.componentProfileList.componentList[i]
                serializeBuffer.putShort(componentProfile.componentId)
                for (para in componentProfile.para) {
                    serializeBuffer.putShort(para)
                }
            }
            serializeBuffer.flip()
            val serializeProductInfo = ByteArray(serializeBuffer.limit())
            serializeBuffer.get(serializeProductInfo)

            SerialPortDataManager.instance.sendCommand(MAKE_DRINKS_COMMAND_ID, componentSize,
                serializeProductInfo)
        }
    }

    fun fetchSerialPort() {
        val finder = SerialPortFinder()
        val path = finder.getAllDevicesPath()
        val devices = finder.getAllDevices()
        _serialPortPath.value = path
        _serialPortDevices.value = devices
    }

}