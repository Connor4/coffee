package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo

class RealHandler {
    private var head: Handler? = null

    init {
        val handlerList = listOf(BoilerHandler(), GrindHandler())
        handlerList.let {
            for (i in it.indices) {
                it[i].setNextHandler(it[i + 1])
            }
            head = it[0]
        }
    }

    fun proceed(pullBufInfo: PullBufInfo): Int? {
        return head?.handleRequest(pullBufInfo)
    }

}