package com.inno.serialport.function.chain

import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class RealChainHandler {
    private var head: Chain? = null

    init {
        val handlerList = listOf(
            SerialPortErrorChain(), HeartBeatChain(), MakeDrinkReplyChain(), CommonReplyChain(),
            FrontColorChain()
        )
        handlerList.let {
            val size = it.size - 1
            for (i in 0 until size) {
                it[i].setNextHandler(it[i + 1])
            }
            head = it[0]
        }
    }

    fun proceed(pullBufInfo: PullBufInfo): ReceivedData? {
        return head!!.handleRequest(pullBufInfo)
    }

}