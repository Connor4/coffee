package com.inno.coffee.viewmodel.settings.config

import androidx.lifecycle.ViewModel
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MilkSettingViewModel @Inject constructor(
    private val coffeeDataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

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
}