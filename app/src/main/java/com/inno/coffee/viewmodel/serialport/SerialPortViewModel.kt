package com.inno.coffee.viewmodel.serialport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.serialport.function.driver.createProductProfile
import com.inno.serialport.utilities.ReceivedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SerialPortViewModel @Inject constructor(
    private val repository: SerialPortRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    val receivedDataFlow: Flow<ReceivedData?> = repository.receivedDataFlow
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
                val createInfo = createProductProfile()
                repository.sendCommand(createInfo)
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