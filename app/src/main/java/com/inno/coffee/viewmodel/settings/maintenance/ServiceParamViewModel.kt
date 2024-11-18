package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.MAINTENANCE_VALUE_CUPS
import com.inno.coffee.utilities.MAINTENANCE_VALUE_SCHEDULE
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceParamViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val SERVICE_PARAM_CUPS = "service_param_cups"
    private val SERVICE_PARAM_SCHEDULE = "service_param_schedule"

    private val _cups = MutableStateFlow(0)
    val cups = _cups
    private val _schedule = MutableStateFlow(0)
    val schedule = _schedule

    fun init() {
        viewModelScope.launch {
            _cups.value =
                dataStore.getCoffeePreference(SERVICE_PARAM_CUPS, 100000)
            _schedule.value =
                dataStore.getCoffeePreference(SERVICE_PARAM_SCHEDULE, 12)
        }
    }

    fun saveServiceParam(key: Int, value: Int) {
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                MAINTENANCE_VALUE_CUPS -> {
                    dataStore.saveCoffeePreference(SERVICE_PARAM_CUPS, value)
                    _cups.value = value
                }
                MAINTENANCE_VALUE_SCHEDULE -> {
                    dataStore.saveCoffeePreference(SERVICE_PARAM_SCHEDULE, value)
                    _schedule.value = value
                }
            }
        }

    }

}