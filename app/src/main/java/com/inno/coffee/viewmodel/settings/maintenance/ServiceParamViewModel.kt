package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.MAINTENANCE_VALUE_CUPS
import com.inno.coffee.utilities.MAINTENANCE_VALUE_SCHEDULE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ServiceParamViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val repository: ServiceParamRepository,
) : ViewModel() {
    companion object {
        private const val TAG = "ServiceParamViewModel"
        private const val SERVICE_PARAM_CUPS = "service_param_cups"
        private const val SERVICE_PARAM_SCHEDULE = "service_param_schedule"
        private const val MAINTENANCE_DATE = "maintenance_date"
        private const val DEFAULT_MAINTENANCE_DATE = "2024-01-01 00:00:00"
    }

    private val _cups = MutableStateFlow(0)
    val cups = _cups
    private val _schedule = MutableStateFlow(0)
    val schedule = _schedule
    private val _totalCount = MutableStateFlow(0)
    val totalCount = _totalCount
    private val _leftCount = MutableStateFlow(0)
    val leftCount = _leftCount
    private val _rightCount = MutableStateFlow(0)
    val rightCount = _rightCount
    private val _maintenanceDate = MutableStateFlow("")
    val maintenanceDate = _maintenanceDate

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _cups.value =
                dataStore.getCoffeePreference(SERVICE_PARAM_CUPS, 100000)
            _schedule.value =
                dataStore.getCoffeePreference(SERVICE_PARAM_SCHEDULE, 12)

            val language = dataStore.getSystemLanguage()
            val time = dataStore.getCoffeePreference(MAINTENANCE_DATE, DEFAULT_MAINTENANCE_DATE)
            val dateTime = LocalDateTime.parse(time)
            val newDateTime = dateTime.plusMonths(_schedule.value.toLong())
            val date =
                TimeUtils.getDateString(Locale.forLanguageTag(language).language, newDateTime)

            _leftCount.value = repository.getBrewProductCount(true)
            _rightCount.value = repository.getBrewProductCount(false)
            _totalCount.value = _cups.value - _leftCount.value - _rightCount.value
            _maintenanceDate.value = date
        }
    }

    fun saveServiceParam(key: Int, value: Int) {
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                MAINTENANCE_VALUE_CUPS -> {
                    dataStore.saveCoffeePreference(SERVICE_PARAM_CUPS, value)
                    _cups.value = value
                    _totalCount.value = _cups.value - _leftCount.value - _rightCount.value
                }
                MAINTENANCE_VALUE_SCHEDULE -> {
                    dataStore.saveCoffeePreference(SERVICE_PARAM_SCHEDULE, value)

                    val systemLanguage = dataStore.getSystemLanguage()
                    val language = Locale.forLanguageTag(systemLanguage).language
                    val time =
                        dataStore.getCoffeePreference(MAINTENANCE_DATE, DEFAULT_MAINTENANCE_DATE)
                    val dateTime = LocalDateTime.parse(time)
                    val newDateTime = dateTime.plusMonths(_schedule.value.toLong())
                    val date = TimeUtils.getDateString(language, newDateTime)

                    _maintenanceDate.value = date
                    _schedule.value = value
                }
            }
        }

    }

}