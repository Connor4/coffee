package com.inno.serialport.core

import com.inno.common.utils.Logger
import com.inno.serialport.utilities.statusenum.SerialErrorTypeEnum
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

object SerialPortManager {
    private const val TAG = "SerialPortManager"

    private const val READ_RETRY_COUNT = 10
    private const val OPEN_RETRY_COUNT = 3
    private var dataRetryCount = 0
    private var openRetryCount = 0
    private var shouldIntercept = false
    private var portMutex = Mutex()

    fun open(port: SerialPort) {
        port.openSerialPort()
    }

    fun close(port: SerialPort) {
        dataRetryCount = 0
        port.closeSerialPort()
    }

    suspend fun readFromSerialPort(
        port: SerialPort, onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: (type: SerialErrorTypeEnum) -> Unit
    ) {
        portMutex.withLock {
//            if (shouldIntercept) {
//                Logger.e(TAG, "failed to read, try to reboot")
//                onFailure(SerialErrorTypeEnum.MAX_OPEN_TRY)
//                return
//            }
            val buffer = ByteArray(port.portFrameSize)

            try {
                val bytesRead = port.mFileInputStream?.read(buffer)
//                val bytesRead = 1
                Logger.d(TAG, "bytesRead $bytesRead")
                when {
                    bytesRead != null && bytesRead > 0 -> {
                        onSuccess(buffer, bytesRead)
//                        onSuccess(HEART_BEAT_REPLY, 1)
                    }

//                    bytesRead != null && bytesRead < 0 -> {
//                        retry(port, onFailure)
//                    }

//                    else -> {
//                        retry(port, onFailure)
//                    }
                }
            } catch (e: IOException) {
                Logger.e(TAG, "readFromSerialPort Exception $e")
                onFailure(SerialErrorTypeEnum.IO_EXCEPTION)
                close(port)
                open(port)
            }
        }
    }

    fun writeToSerialPort(port: SerialPort, frame: ByteArray) {
        try {
            port.mFileOutputStream?.write(frame)
        } catch (e: IOException) {
            Logger.e(TAG, "writeToSerialPort Exception $e")
        }
    }

    private fun retry(port: SerialPort,
        onFailure: (type: SerialErrorTypeEnum) -> Unit) {
        if (++dataRetryCount >= READ_RETRY_COUNT) {
            Logger.e(TAG, "reached max retry read count, reopen port")
            close(port)
            onFailure(SerialErrorTypeEnum.MAX_READ_TRY)
            if (++openRetryCount < OPEN_RETRY_COUNT) {
                Logger.e(TAG,
                    "attempting to reopen port, attempt $openRetryCount")
                onFailure(SerialErrorTypeEnum.MAX_READ_TRY)
                open(port)
            } else {
                Logger.e(TAG, "reached max reopen count, need reboot")
                shouldIntercept = true
                onFailure(SerialErrorTypeEnum.MAX_OPEN_TRY)
            }
        } else {
            Logger.e(TAG, "read failed, retry time $dataRetryCount")
            onFailure(SerialErrorTypeEnum.READ_FAIL)
        }
    }

}