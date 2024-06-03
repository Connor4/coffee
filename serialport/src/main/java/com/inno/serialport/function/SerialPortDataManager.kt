package com.inno.serialport.function

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ArrayBlockingQueue

/**
 * 1. string to json
 * 2. select chain to process, generate data bean
 * 3. as a repository to hold generate data
 * 4. need two function 1) repeat send get data 2) send command, when 2, 1 need stop
 *
 */
class SerialPortDataManager private constructor() {

    companion object {
        val instance: SerialPortDataManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SerialPortDataManager()
        }

        private const val TAG = "SerialPortDataManager"
    }

    private val driver = RS485Driver()
    private val originString = ArrayBlockingQueue<String>(1024)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            receiveData()
        }
        scope.launch {
            processData()
        }
    }

    private suspend fun receiveData() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                driver.receive()?.let {
                    originString.put(it)
                }
            }
        }
    }

    private suspend fun processData() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                originString.take()?.let {
                    // 进行process，责任链处理完后然后放入业务各个list中进行ui更新，这里的list是所有端口数据list
                    // 每个类型id只上传一个int状态，要以大类作为一个类，保存当前最新状态
                }
            }
        }
    }

}