package com.inno.coffee.viewmodel.settings.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.MILK_DETECT_METHOD
import com.inno.coffee.utilities.MILK_FOAM_WARNING
import com.inno.coffee.utilities.MILK_OUTLET
import com.inno.coffee.utilities.MILK_SETTING_MODE
import com.inno.coffee.utilities.MILK_STEAM_PRESSURE
import com.inno.coffee.utilities.MILK_TANK_DETECT
import com.inno.coffee.utilities.MILK_TEMP_DETECT
import com.inno.coffee.utilities.MILK_VALVE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilkSettingViewModel @Inject constructor(
    private val coffeeDataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val TAG = "MilkSettingViewModel"
    }

    private val _detectMethod = MutableStateFlow(0)
    val detectMethod: StateFlow<Int> = _detectMethod
    private val _tankDetect = MutableStateFlow(0)
    val tankDetect: StateFlow<Int> = _tankDetect
    private val _valve = MutableStateFlow(0)
    val valve: StateFlow<Int> = _valve
    private val _outlet = MutableStateFlow(0)
    val outlet: StateFlow<Int> = _outlet
    private val _foamWarning = MutableStateFlow(0)
    val foamWarning: StateFlow<Int> = _foamWarning
    private val _tempDetect = MutableStateFlow(0)
    val tempDetect: StateFlow<Int> = _tempDetect
    private val _settingMode = MutableStateFlow(0)
    val settingMode: StateFlow<Int> = _settingMode
    private val _steamPressure = MutableStateFlow(1f)
    val steamPressure: StateFlow<Float> = _steamPressure

    private val _page = MutableStateFlow(0)
    val page = _page.asStateFlow()

    fun prevPage() {
        _page.value--.coerceIn(0, 9)
    }

    fun nextPage() {
        _page.value++.coerceIn(0, 9)
    }

    fun savePageOneValue(key: Int, value: Any) {
        Logger.d(TAG, "savePageOneValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                MILK_DETECT_METHOD -> _detectMethod.value = value as Int
                MILK_TANK_DETECT -> _tankDetect.value = value as Int
                MILK_VALVE -> _valve.value = value as Int
                MILK_OUTLET -> _outlet.value = value as Int
                MILK_FOAM_WARNING -> _foamWarning.value = value as Int
                MILK_TEMP_DETECT -> _tempDetect.value = value as Int
                MILK_SETTING_MODE -> _settingMode.value = value as Int
                MILK_STEAM_PRESSURE -> _steamPressure.value = value as Float
            }
        }
    }

}