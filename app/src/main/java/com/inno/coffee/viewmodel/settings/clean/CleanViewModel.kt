package com.inno.coffee.viewmodel.settings.clean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleanViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
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
    private val _cleanPeriod = MutableStateFlow(0)
    val cleanPeriod = _cleanPeriod
    private val _cleanTime = MutableStateFlow("")
    val cleanTime = _cleanTime
    private val _timeTolerance = MutableStateFlow(0)
    val timeTolerance = _timeTolerance
    private val _weekendCleanMode = MutableStateFlow(0)
    val weekendCleanMode = _weekendCleanMode
    private val _milkWeekendCleanMode = MutableStateFlow(0)
    val milkWeekendCleanMode = _milkWeekendCleanMode
    private val _afterCleaning = MutableStateFlow(false)
    val afterCleaning = _afterCleaning
    private val _standbyButton = MutableStateFlow(false)
    val standbyButton = _standbyButton

    private val _switchValue = MutableStateFlow(0)

    fun init() {
        viewModelScope.launch {
            _mode.value = dataStore.getCoffeePreference(MODE, 0)
            _cleanPeriod.value = dataStore.getCoffeePreference(CLEAN_PERIOD, 0)
            _cleanTime.value = dataStore.getCoffeePreference(CLEAN_TIME, "")
            _timeTolerance.value = dataStore.getCoffeePreference(TIME_TOLERANCE, 0)
            _weekendCleanMode.value = dataStore.getCoffeePreference(WEEKEND_CLEAN_MODE, 0)
            _milkWeekendCleanMode.value = dataStore.getCoffeePreference(MILK_WEEKEND_CLEAN_MODE, 0)
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

}