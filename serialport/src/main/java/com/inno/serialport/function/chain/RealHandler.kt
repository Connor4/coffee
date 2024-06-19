package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo

class RealHandler {
    private var head: Handler? = null

    init {
        val handlerList = listOf(BoilerHandler(), GrindHandler())
        handlerList.let {
            val size = it.size - 1
            for (i in 0 until size) {
                it[i].setNextHandler(it[i + 1])
            }
            head = it[0]
        }
    }

    fun proceed(pullBufInfo: PullBufInfo): Int? {
        return head?.handleRequest(pullBufInfo)
    }

}