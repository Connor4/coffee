package com.inno.serialport.function.chain

import com.inno.serialport.bean.HandleResult
import com.inno.serialport.bean.PullBufInfo

class RealChainHandler {
    private var head: Chain? = null

    init {
        val handlerList = listOf(ErrorChain(), HeartBeatChain(),
            BoilerChain(), GrindChain())
        handlerList.let {
            val size = it.size - 1
            for (i in 0 until size) {
                it[i].setNextHandler(it[i + 1])
            }
            head = it[0]
        }
    }

    fun proceed(pullBufInfo: PullBufInfo): HandleResult {
        val buffer = pullBufInfo.pollBuf
        val length = ((buffer[3].toInt() and 0xFF) shl 8) or (buffer[4].toInt() and 0xFF)
        val command = ((buffer[5].toInt() and 0xFF) shl 8) or (buffer[6].toInt() and 0xFF)
        val result = buffer.sliceArray(6 until buffer.size - 3)
        val handleResult = HandleResult(length, command, result)
        return head!!.handleRequest(handleResult)
    }

}