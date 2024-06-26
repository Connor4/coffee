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

    fun proceed(pullBufInfo: PullBufInfo): HandleResult? {
        return head!!.handleRequest(pullBufInfo)
    }

}