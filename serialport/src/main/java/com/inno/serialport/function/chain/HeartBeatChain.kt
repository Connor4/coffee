package com.inno.serialport.function.chain

import com.inno.serialport.utilities.HEARTBEAT_COMMAND_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class HeartBeatChain : Chain() {

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return HEARTBEAT_COMMAND_ID == pullBufInfo.command
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        return ReceivedData.HeartBeat(heartbeatStatus = true)
    }

}