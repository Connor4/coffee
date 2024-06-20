package com.inno.serialport.core

import com.inno.common.utils.Logger
import com.inno.serialport.bean.SerialErrorType
import java.io.IOException

object SerialPortManager {
    private const val TAG = "SerialPortManager"

    private const val READ_RETRY_COUNT = 10
    private const val OPEN_RETRY_COUNT = 3
    private var dataRetryCount = 0
    private var openRetryCount = 0

    fun open(port: SerialPort) {
        port.openSerialPort()
    }

    fun close(port: SerialPort) {
        dataRetryCount = 0
        port.closeSerialPort()
    }

    fun readFromSerialPort(
        port: SerialPort, onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: (type: SerialErrorType) -> Unit
    ) {
        val buffer = ByteArray(port.portFrameSize)
        try {
            val bytesRead = port.mFileInputStream?.read(buffer)

            when {
                bytesRead != null && bytesRead > 0 -> {
                    onSuccess(buffer, bytesRead)
                }

                bytesRead == -1 -> {
                    if (++dataRetryCount == READ_RETRY_COUNT) {
                        Logger.e(TAG, "Max data retry count reached")
                        close(port)
                        onFailure(SerialErrorType.MAX_READ_TRY)
                        if (++openRetryCount < OPEN_RETRY_COUNT) {
                            Logger.e(
                                TAG,
                                "Attempting to reopen port, attempt $openRetryCount"
                            )
                            open(port)
                        } else {
                            Logger.e(TAG, "Max open retry count reached")
                            onFailure(SerialErrorType.MAX_OPEN_TRY)
                        }
                    }
                }

                bytesRead == 0 -> {
                    onFailure(SerialErrorType.SERIAL_CONNECTION_ERROR)
                }
            }
        } catch (e: IOException) {
            Logger.e(TAG, "readFromSerialPort Exception $e")
            close(port)
            onFailure(SerialErrorType.IO_EXCEPTION)
        }
    }

    fun writeToSerialPort(port: SerialPort, frame: ByteArray) {
        try {
            port.mFileOutputStream?.write(frame)
        } catch (e: IOException) {
            Logger.e(TAG, "writeToSerialPort Exception $e")
        }
    }

}