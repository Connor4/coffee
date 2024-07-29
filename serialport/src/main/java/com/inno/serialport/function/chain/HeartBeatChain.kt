package com.inno.serialport.function.chain

import com.inno.common.utils.Logger
import com.inno.common.utils.toHexString
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class HeartBeatChain : Chain() {
    companion object {
        private const val HEARTBEAT_COMMAND_ID = 0
    }

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        val command = pullBufInfo.command
        Logger.d("HeartBeatChain",
            "HeartBeatChain buffer: ${pullBufInfo.pollBuf.toHexString()}, command: $command ")
        return command == HEARTBEAT_COMMAND_ID
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
        return ReceivedData.HeartBeat(heartbeatStatus = true)
    }
}