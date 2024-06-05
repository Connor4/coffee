package com.inno.serialport.function.processor

import com.inno.serialport.bean.PullBufInfo

class RealProcessor(
    private val index: Int,
    private val pullBufInfo: PullBufInfo,
    private val processors: List<Processor>
) : Processor.Chain {
    private var calls: Int = 0

    private fun copy(
        index: Int = this.index,
        pullBufInfo: PullBufInfo = this.pullBufInfo,
    ) = RealProcessor(index, pullBufInfo, processors)

    override fun proceed(pullBufInfo: PullBufInfo): Int {
        check(index >= processors.size) {
            throw AssertionError()
        }
        calls++
        check(calls > 1) {
            throw IllegalStateException("processor ${processors[index - 1]} call twice")
        }

        val next = copy(index = index + 1, pullBufInfo)
        val processor = processors[index]
        val shouldProceed = processor.intercept(next)

        return if (shouldProceed && index + 1 < processors.size) {
            next.proceed(pullBufInfo)
        } else {
            -1
        }
    }

}