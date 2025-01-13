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
    private lateinit var memoryDateTime: LocalDateTime

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _cups.value =
                dataStore.getCoffeePreference(SERVICE_PARAM_CUPS, 100000)
            _schedule.value =
                dataStore.getCoffeePreference(SERVICE_PARAM_SCHEDULE, 12)

            val time = dataStore.getCoffeePreference(MAINTENANCE_DATE, DEFAULT_MAINTENANCE_DATE)
            memoryDateTime = LocalDateTime.parse(time)
            val nextMonth = memoryDateTime.plusMonths(_schedule.value.toLong())

            _leftCount.value = repository.getBrewProductCount(true)
            _rightCount.value = repository.getBrewProductCount(false)
            _totalCount.value = _cups.value - _leftCount.value - _rightCount.value
            _maintenanceDate.value = TimeUtils.getYearMonthDayFormat(nextMonth)
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
                    if (value == _schedule.value) {
                        return@launch
                    }
                    val newDate = if (value > _schedule.value) {
                        memoryDateTime.plusMonths(1).also { memoryDateTime = it }
                    } else {
                        memoryDateTime.minusMonths(1).also { memoryDateTime = it }
                    }

                    _maintenanceDate.value = TimeUtils.getYearMonthDayFormat(newDate)
                    _schedule.value = value

                    dataStore.saveCoffeePreference(SERVICE_PARAM_SCHEDULE, value)
                }
            }
        }
    }

    fun resetServiceParam() {
    }

}