package com.inno.serialport.function.processor

import com.inno.serialport.utilities.PullBufInfo

interface Processor {
    fun intercept(chain: Chain): Boolean

    interface Chain {
        fun proceed(pullBufInfo: PullBufInfo): Int
    }
}