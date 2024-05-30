package com.inno.serialport.core

import com.inno.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object SerialPortManager : CoroutineScope {
    private const val TAG = "SerialPortManager"

    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job

    fun start(
        port: SerialPort,
        onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: () -> Unit
    ) {
        port.openSerialPort()
        readData(port, onSuccess, onFailure)
    }

    fun stop(port: SerialPort) {
        job.cancel()
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

    fun writeData(port: SerialPort, data: ByteArray) {
        launch {
            writeToSerialPort(port, data)
        }
    }

    private suspend fun readFromSerialPort(
        port: SerialPort, onSuccess: (buffer: ByteArray, size: Int) -> Unit,
        onFailure: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            try {
                while (isActive) {
                    val bytesRead = port.mFileInputStream?.read(buffer)
                    if (bytesRead != null && bytesRead > 0) {
                        onSuccess(buffer, bytesRead)
                    }
                }
            } catch (e: IOException) {
                Logger.e(TAG, "readFromSerialPort Exception $e")
                onFailure()
            }
        }
    }

    private suspend fun writeToSerialPort(port: SerialPort, data: ByteArray) {
        withContext(Dispatchers.IO) {
            try {
                if (isActive) {
                    port.mFileOutputStream?.write(data)
                }
            } catch (e: IOException) {
                Logger.e(TAG, "writeToSerialPort Exception $e")
            }
        }
    }

}