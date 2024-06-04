package com.inno.serialport.function.chain

import com.inno.serialport.bean.PullBufInfo

interface Processor {
    fun intercept(chain: Chain): Boolean

    interface Chain {
        fun proceed(pullBufInfo: PullBufInfo): Int
    }
}