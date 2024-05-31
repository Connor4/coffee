package com.inno.serialport.core

import com.inno.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object SerialPortManager : CoroutineScope {
    private const val TAG = "SerialPortManager"

    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job
    private const val RETRY_COUNT = 10
    private var retryCount = 0

    fun open(port: SerialPort) {
        port.openSerialPort()
    }

    fun close(port: SerialPort) {
        job.cancel()
        retryCount = 0
        port.closeSerialPort()
    }

    fun readData(
        port: SerialPort,
        onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: () -> Unit
    ) {
        launch {
            readFromSerialPort(port, onSuccess, onFailure)
        }
    }

    fun writeData(port: SerialPort, frame: ByteArray) {
        launch {
            writeToSerialPort(port, frame)
        }
    }

    private suspend fun readFromSerialPort(
        port: SerialPort, onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(port.portFrameSize)
            try {
                while (isActive) {
                    delay(500)
                    val bytesRead = port.mFileInputStream?.read(buffer)
                    if (bytesRead != null && bytesRead > 0) {
                        onSuccess(buffer, bytesRead)
                    } else if (bytesRead == -1) {
                        if (retryCount++ == RETRY_COUNT) {
                            Logger.e(TAG, "readFromSerialPort: reach max retry count")
                            onFailure()
                            close(port)
                            delay(1000)
                            open(port)
                        }
                    }
                }
            } catch (e: IOException) {
                Logger.e(TAG, "readFromSerialPort Exception $e")
                onFailure()
                close(port)
            }
        }
    }

    private suspend fun writeToSerialPort(port: SerialPort, frame: ByteArray) {
        withContext(Dispatchers.IO) {
            try {
                if (isActive) {
                    port.mFileOutputStream?.write(frame)
                }
            } catch (e: IOException) {
                Logger.e(TAG, "writeToSerialPort Exception $e")
            }
        }
    }

    private suspend fun reopen() {
        withContext(Dispatchers.Main) {

        }
    }

}