package com.inno.coffee.viewmodel.settings.clean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.CLEAN_MILK_WEEKEND_CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_PERIOD_TIME
import com.inno.coffee.utilities.CLEAN_SET_TIME
import com.inno.coffee.utilities.CLEAN_STANDBY_AFTER_CLEANING
import com.inno.coffee.utilities.CLEAN_STANDBY_BUTTON
import com.inno.coffee.utilities.CLEAN_TIME_TOLERANCE
import com.inno.coffee.utilities.CLEAN_WEEKEND_CLEAN_MODE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleanViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    companion object {
        private const val TAG = "CleanViewModel"
        private const val MODE = "clean_mode"
        private const val CLEAN_PERIOD = "clean_period"
        private const val CLEAN_TIME = "clean_time"
        private const val TIME_TOLERANCE = "clean_time_tolerance"
        private const val WEEKEND_CLEAN_MODE = "clean_weekend_clean_mode"
        private const val MILK_WEEKEND_CLEAN_MODE = "clean_milk_weekend_clean_mode"
        private const val AFTER_CLEANING = "clean_after_cleaning"
        private const val STANDBY_BUTTON = "clean_standby_button"
        private const val SWITCH_VALUE = "clean_switch_value"
    }

    private val _mode = MutableStateFlow(0)
    val mode = _mode
    private val _cleanPeriod = MutableStateFlow(1)
    val cleanPeriod = _cleanPeriod
    private val _cleanTime = MutableStateFlow("")
    val cleanTime = _cleanTime
    private val _timeTolerance = MutableStateFlow(1)
    val timeTolerance = _timeTolerance
    private val _weekendCleanMode = MutableStateFlow(0)
    val weekendCleanMode = _weekendCleanMode
    private val _milkWeekendCleanMode = MutableStateFlow(false)
    val milkWeekendCleanMode = _milkWeekendCleanMode
    private val _afterCleaning = MutableStateFlow(false)
    val afterCleaning = _afterCleaning
    private val _standbyButton = MutableStateFlow(false)
    val standbyButton = _standbyButton

    private val _switchValue = MutableStateFlow(0)
    val switchValue = _switchValue

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _mode.value = dataStore.getCoffeePreference(MODE, 0)
            _cleanPeriod.value = dataStore.getCoffeePreference(CLEAN_PERIOD, 1)
            val time = dataStore.getCoffeePreference(CLEAN_TIME, 0)
            val (hour, minute) = decodeTime(time)
            _cleanTime.value = String.format("%02d:%02d", hour, minute)
            _timeTolerance.value = dataStore.getCoffeePreference(TIME_TOLERANCE, 1)
            _weekendCleanMode.value = dataStore.getCoffeePreference(WEEKEND_CLEAN_MODE, 0)
            _milkWeekendCleanMode.value = dataStore.getCoffeePreference(MILK_WEEKEND_CLEAN_MODE,
                false)
            _afterCleaning.value = dataStore.getCoffeePreference(AFTER_CLEANING, false)
            _standbyButton.value = dataStore.getCoffeePreference(STANDBY_BUTTON, false)

            _switchValue.value = dataStore.getCoffeePreference(SWITCH_VALUE, 0)
        }
    }

    fun isFlagSet(flagIndex: Int): Boolean {
        return (_switchValue.value and (1 shl flagIndex)) != 0
    }

    fun setFlag(flagIndex: Int, value: Boolean) {
        _switchValue.value = _switchValue.value xor (1 shl flagIndex)
        Logger.d(TAG, "setFlag() called with: flagIndex = $flagIndex, value = $value, newValue = " +
                "${_switchValue.value}")
        viewModelScope.launch {
            dataStore.saveCoffeePreference(SWITCH_VALUE, _switchValue.value)
        }
    }

    fun encodeTime(hour: Int, minute: Int): Int {
        if (hour in 0..23 || minute in 0..59) {
            return (hour shl 8) or minute
        }
        return (0 shl 8) or 0
    }

    private fun decodeTime(timeValue: Int): Pair<Int, Int> {
        val hour = (timeValue shr 8) and 0xFF
        val minute = timeValue and 0xFF
        return Pair(hour, minute)
    }

    fun setCleanValue(key: Int, value: Any) {
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                CLEAN_MODE -> {
                    dataStore.saveCoffeePreference(MODE, value as Int)
                    _mode.value = value
                }
                CLEAN_PERIOD_TIME -> {
                    dataStore.saveCoffeePreference(CLEAN_PERIOD, value as Int)
                    _cleanPeriod.value = value
                }
                CLEAN_SET_TIME -> {
                    dataStore.saveCoffeePreference(CLEAN_TIME, value as Int)
                    val (hour, minute) = decodeTime(value)
                    _cleanTime.value = String.format("%02d:%02d", hour, minute)
                }
                CLEAN_TIME_TOLERANCE -> {
                    dataStore.saveCoffeePreference(TIME_TOLERANCE, value as Int)
                    _timeTolerance.value = value
                }
                CLEAN_WEEKEND_CLEAN_MODE -> {
                    dataStore.saveCoffeePreference(WEEKEND_CLEAN_MODE, value as Int)
                    _weekendCleanMode.value = value
                }
                CLEAN_MILK_WEEKEND_CLEAN_MODE -> {
                    dataStore.saveCoffeePreference(MILK_WEEKEND_CLEAN_MODE, value as Boolean)
                    _milkWeekendCleanMode.value = value
                }
                CLEAN_STANDBY_AFTER_CLEANING -> {
                    dataStore.saveCoffeePreference(AFTER_CLEANING, value as Boolean)
                    _afterCleaning.value = value
                }
                CLEAN_STANDBY_BUTTON -> {
                    dataStore.saveCoffeePreference(STANDBY_BUTTON, value as Boolean)
                    _standbyButton.value = value
                }
            }
        }
    }

}