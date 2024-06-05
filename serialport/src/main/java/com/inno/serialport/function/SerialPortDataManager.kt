package com.inno.serialport.function

import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.function.chain.BoilerProcessor
import com.inno.serialport.function.chain.GrindProcessor
import com.inno.serialport.function.chain.RealProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        private const val PULL_INTERVAL_MILLIS = 500L
    }

    private val driver = RS485Driver()
    private val pullBuffInfo = ArrayBlockingQueue<PullBufInfo>(128)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val processors = listOf(BoilerProcessor(), GrindProcessor())

    init {
        scope.launch {
            receiveData()
        }
        scope.launch {
            processData()
        }
    }

    suspend fun sendCommand(command: String) {
        withContext(Dispatchers.IO) {
            val createInfo = createInfo()
            val infoString = Json.encodeToString(createInfo)
            driver.send(infoString)
        }
    }

    private suspend fun receiveData() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                delay(PULL_INTERVAL_MILLIS)
                val receive = driver.receive()
                pullBuffInfo.put(receive)
            }
        }
    }

    private suspend fun processData() {
        withContext(Dispatchers.IO) {
            while (isActive) {
                pullBuffInfo.take()?.let {
                    // 进行process，责任链处理完后然后放入业务各个list中进行ui更新，这里的list是所有端口数据list
                    // 每个类型id只上传一个int状态，要以大类作为一个类，保存当前最新状态
                    val chain = RealProcessor(0, it, processors)
                    val result = chain.proceed(it)
                    // TODO 处理result进行ui业务更新
                    updateUI(result)
                }
            }
        }
    }

    private fun updateUI(result: Int) {

    }
}