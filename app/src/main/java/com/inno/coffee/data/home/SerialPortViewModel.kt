package com.inno.coffee.data.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.function.createInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SerialPortViewModel @Inject constructor(
    private val repository: SerialPortRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    val receivedDataFlow: SharedFlow<PullBufInfo?> = repository.receivedDataFlow
    val serialPortPath: StateFlow<String> = repository.serialPortPath.map {
        it.joinToString(separator = ", ")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ""
    )
    val serialPortDevices: StateFlow<String> = repository.serialPortDevices.map {
        it.joinToString(separator = ", ")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ""
    )

    fun openSerialPort() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.openSerialPort()
            }
        }
    }

    fun closeSerialPort() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.closeSerialPort()
            }
        }
    }

    fun sendCommand(command: String) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val createInfo = createInfo()
                val info = Json.encodeToString(createInfo)
                repository.sendCommand(info)
            }
        }
    }

    fun findSerialPort() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.fetchSerialPort()
            }
        }
    }

}