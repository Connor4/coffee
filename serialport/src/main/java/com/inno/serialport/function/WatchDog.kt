package com.inno.serialport.function

import com.inno.serialport.bean.PullBufInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchDog {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var missHeartBeat = 0

    fun start() {
        scope.launch {

        }
    }

    fun feedDog(pullBufInfo: PullBufInfo) {

    }


}