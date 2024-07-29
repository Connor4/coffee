package com.inno.serialport.function.chain

import com.inno.serialport.utilities.MAKE_DRINKS_COMMAND
import com.inno.serialport.utilities.PullBufInfo
import com.inno.serialport.utilities.ReceivedData

class MakeDrinksChain : Chain() {

    override fun canHandle(pullBufInfo: PullBufInfo): Boolean {
        val command = pullBufInfo.command
        return command.toShort() == MAKE_DRINKS_COMMAND
    }

    override fun handle(pullBufInfo: PullBufInfo): ReceivedData? {
        TODO("Not yet implemented")
    }

}