package com.inno.serialport.function.chain

import com.inno.serialport.utilities.FRAME_CMD_INDEX_HIGH
import com.inno.serialport.utilities.FRAME_CMD_INDEX_LOW
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class HeartBeatChain : Chain() {

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        val buffer = pullBufInfo.pollBuf
        val command =
            ((buffer[FRAME_CMD_INDEX_HIGH].toInt() and 0xFF) shl 8) or (buffer[FRAME_CMD_INDEX_LOW]
                .toInt() and 0xFF)
//        Logger.d("HeartBeatChain",
//            "HeartBeatChain buffer: ${buffer.toHexString()}, command: $command, 5 " +
//                    "${buffer[FRAME_CMD_INDEX_LOW]}, " +
//                    "6 ${buffer[FRAME_CMD_INDEX_HIGH]}")
        return command == 1
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData {
//        val buffer = pullBufInfo.pollBuf
//        val result = ((buffer[7].toInt() and 0xFF) shl 8) or (buffer[8].toInt() and 0xFF)
//        Logger.d("HeartBeatChain", "result: $result")
        return ReceivedData.HeartBeat(heartbeatStatus = true)
    }
}