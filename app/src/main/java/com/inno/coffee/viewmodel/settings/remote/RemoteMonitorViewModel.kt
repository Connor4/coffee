package com.inno.coffee.viewmodel.settings.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.REMOTE_MACHINE_ID
import com.inno.coffee.utilities.REMOTE_TELEMETRY_SWITCH
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMonitorViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val TAG = "RemoteMonitorViewModel"
        private const val TELEMETRY_SWITCH = "telemetry_switch"
        private const val TELEMETRY_ID = "telemetry_id"
    }

    private val _telemetrySwitch = MutableStateFlow(false)
    val telemetrySwitch = _telemetrySwitch.asStateFlow()
    private val _telemetryId = MutableStateFlow("")
    val telemetryId = _telemetryId.asStateFlow()

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _telemetrySwitch.value = dataStore.getCoffeePreference(TELEMETRY_SWITCH, false)
            _telemetryId.value = dataStore.getCoffeePreference(TELEMETRY_ID, "10000")
        }
    }

    fun checkConnection() {

    }

    fun saveRemoteMonitorValue(index: Int, value: Any) {
        viewModelScope.launch(defaultDispatcher) {
            when (index) {
                REMOTE_TELEMETRY_SWITCH -> {
                    dataStore.saveCoffeePreference(TELEMETRY_SWITCH, value as Boolean)
                    _telemetrySwitch.value = value
                }
                REMOTE_MACHINE_ID -> {
                    dataStore.saveCoffeePreference(TELEMETRY_ID, value as String)
                    _telemetryId.value = value
                }
            }
        }
    }

}