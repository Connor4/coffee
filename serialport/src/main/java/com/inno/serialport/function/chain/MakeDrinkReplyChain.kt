package com.inno.serialport.function.chain

import com.inno.serialport.utilities.HeartBeatReply
import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND_ID
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class MakeDrinkReplyChain : Chain() {
    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        return MAKE_DRINKS_COMMAND_ID == pullBufInfo.command
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData? {
        val makeDrinkReply = ReceivedData.HeartBeat()
        val data = pullBufInfo.pollBuf
        val value = ((data[1].toInt() and 0xFF) shl 8) or (data[0].toInt() and 0xFF)
        makeDrinkReply.makeDrinkReply = HeartBeatReply.MakeDrinkReplay(100, value)
        return makeDrinkReply
    }
}