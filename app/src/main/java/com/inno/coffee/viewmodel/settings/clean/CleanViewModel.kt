package com.inno.coffee.viewmodel.settings.clean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.CLEAN_MILK_WEEKEND_CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_PERIOD_TIME
import com.inno.coffee.utilities.CLEAN_STANDBY_AFTER_CLEANING
import com.inno.coffee.utilities.CLEAN_STANDBY_BUTTON
import com.inno.coffee.utilities.CLEAN_TIME_TOLERANCE
import com.inno.coffee.utilities.CLEAN_WEEKEND_CLEAN_MODE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        private const val STANDBY_MONDAY = "clean_standby_monday"
        private const val STANDBY_MONDAY_END = "clean_standby_monday_end"
        private const val STANDBY_TUESDAY = "clean_standby_tuesday"
        private const val STANDBY_TUESDAY_END = "clean_standby_tuesday_end"
        private const val STANDBY_WEDNESDAY = "clean_standby_wednesday"
        private const val STANDBY_WEDNESDAY_END = "clean_standby_wednesday_end"
        private const val STANDBY_THURSDAY = "clean_standby_thursday"
        private const val STANDBY_THURSDAY_END = "clean_standby_thursday_end"
        private const val STANDBY_FRIDAY = "clean_standby_friday"
        private const val STANDBY_FRIDAY_END = "clean_standby_friday_end"
        private const val STANDBY_SATURDAY = "clean_standby_saturday"
        private const val STANDBY_SATURDAY_END = "clean_standby_saturday_end"
        private const val STANDBY_SUNDAY = "clean_standby_sunday"
        private const val STANDBY_SUNDAY_END = "clean_standby_sunday_end"
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

    private val _standbyMonday = MutableStateFlow(0)
    val standbyMonday = decodeTimeToString(_standbyMonday)
    private val _standbyMondayEnd = MutableStateFlow(0)
    val standbyMondayEnd = decodeTimeToString(_standbyMondayEnd)
    private val _standbyTuesday = MutableStateFlow(0)
    val standbyTuesday = decodeTimeToString(_standbyTuesday)
    private val _standbyTuesdayEnd = MutableStateFlow(0)
    val standbyTuesdayEnd = decodeTimeToString(_standbyTuesdayEnd)
    private val _standbyWednesday = MutableStateFlow(0)
    val standbyWednesday = decodeTimeToString(_standbyWednesday)
    private val _standbyWednesdayEnd = MutableStateFlow(0)
    val standbyWednesdayEnd = decodeTimeToString(_standbyWednesdayEnd)
    private val _standbyThursday = MutableStateFlow(0)
    val standbyThursday = decodeTimeToString(_standbyThursday)
    private val _standbyThursdayEnd = MutableStateFlow(0)
    val standbyThursdayEnd = decodeTimeToString(_standbyThursdayEnd)
    private val _standbyFriday = MutableStateFlow(0)
    val standbyFriday = decodeTimeToString(_standbyFriday)
    private val _standbyFridayEnd = MutableStateFlow(0)
    val standbyFridayEnd = decodeTimeToString(_standbyFridayEnd)
    private val _standbySaturday = MutableStateFlow(0)
    val standbySaturday = decodeTimeToString(_standbySaturday)
    private val _standbySaturdayEnd = MutableStateFlow(0)
    val standbySaturdayEnd = decodeTimeToString(_standbySaturdayEnd)
    private val _standbySunday = MutableStateFlow(0)
    val standbySunday = decodeTimeToString(_standbySunday)
    private val _standbySundayEnd = MutableStateFlow(0)
    val standbySundayEnd = decodeTimeToString(_standbySundayEnd)

    private val _switchValue = MutableStateFlow(0)
    val switchValue = _switchValue

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            val time = dataStore.getCoffeePreference(CLEAN_TIME, 0)
            val monday = dataStore.getCoffeePreference(STANDBY_MONDAY, 0)
            val mondayEnd = dataStore.getCoffeePreference(STANDBY_MONDAY_END, 0)
            val tuesday = dataStore.getCoffeePreference(STANDBY_TUESDAY, 0)
            val tuesdayEnd = dataStore.getCoffeePreference(STANDBY_TUESDAY_END, 0)
            val wednesday = dataStore.getCoffeePreference(STANDBY_WEDNESDAY, 0)
            val wednesdayEnd = dataStore.getCoffeePreference(STANDBY_WEDNESDAY_END, 0)
            val thursday = dataStore.getCoffeePreference(STANDBY_THURSDAY, 0)
            val thursdayEnd = dataStore.getCoffeePreference(STANDBY_THURSDAY_END, 0)
            val friday = dataStore.getCoffeePreference(STANDBY_FRIDAY, 0)
            val fridayEnd = dataStore.getCoffeePreference(STANDBY_FRIDAY_END, 0)
            val saturday = dataStore.getCoffeePreference(STANDBY_SATURDAY, 0)
            val saturdayEnd = dataStore.getCoffeePreference(STANDBY_SATURDAY_END, 0)
            val sunday = dataStore.getCoffeePreference(STANDBY_SUNDAY, 0)
            val sundayEnd = dataStore.getCoffeePreference(STANDBY_SUNDAY_END, 0)

            _cleanTime.value = decodeTimeForString(time)
            _standbyMonday.value = monday
            _standbyMondayEnd.value = mondayEnd
            _standbyTuesday.value = tuesday
            _standbyTuesdayEnd.value = tuesdayEnd
            _standbyWednesday.value = wednesday
            _standbyWednesdayEnd.value = wednesdayEnd
            _standbyThursday.value = thursday
            _standbyThursdayEnd.value = thursdayEnd
            _standbyFriday.value = friday
            _standbyFridayEnd.value = fridayEnd
            _standbySaturday.value = saturday
            _standbySaturdayEnd.value = saturdayEnd
            _standbySunday.value = sunday
            _standbySundayEnd.value = sundayEnd

            _mode.value = dataStore.getCoffeePreference(MODE, 0)
            _cleanPeriod.value = dataStore.getCoffeePreference(CLEAN_PERIOD, 1)
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

    fun saveCleanTime(hour: Int, minute: Int) {
        _cleanTime.value = String.format("%02d:%02d", hour, minute)
        viewModelScope.launch {
            val timeValue = encodeTime(hour, minute)
            dataStore.saveCoffeePreference(CLEAN_TIME, timeValue)
        }
    }

    fun getHourAndMinute(index: Int): Pair<Int, Int> {
        val (hour, minute) = when (index) {
            0 -> decodeTime(_standbyMonday.value)
            1 -> decodeTime(_standbyMondayEnd.value)
            2 -> decodeTime(_standbyTuesday.value)
            3 -> decodeTime(_standbyTuesdayEnd.value)
            4 -> decodeTime(_standbyWednesday.value)
            5 -> decodeTime(_standbyWednesdayEnd.value)
            6 -> decodeTime(_standbyThursday.value)
            7 -> decodeTime(_standbyThursdayEnd.value)
            8 -> decodeTime(_standbyFriday.value)
            9 -> decodeTime(_standbyFridayEnd.value)
            10 -> decodeTime(_standbySaturday.value)
            11 -> decodeTime(_standbySaturdayEnd.value)
            12 -> decodeTime(_standbySunday.value)
            13 -> decodeTime(_standbySundayEnd.value)
            else -> Pair(0, 0)
        }
        return Pair(hour, minute)
    }

    fun saveStandbyTime(index: Int, hour: Int, minute: Int) {
        viewModelScope.launch {
            val timeValue = encodeTime(hour, minute)
            when (index) {
                0 -> {
                    _standbyMonday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_MONDAY, timeValue)
                }
                1 -> {
                    _standbyMondayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_MONDAY_END, timeValue)
                }
                2 -> {
                    _standbyTuesday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_TUESDAY, timeValue)
                }
                3 -> {
                    _standbyTuesdayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_TUESDAY_END, timeValue)
                }
                4 -> {
                    _standbyWednesday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_WEDNESDAY, timeValue)
                }
                5 -> {
                    _standbyWednesdayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_WEDNESDAY_END, timeValue)
                }
                6 -> {
                    _standbyThursday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_THURSDAY, timeValue)
                }
                7 -> {
                    _standbyThursdayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_THURSDAY_END, timeValue)
                }
                8 -> {
                    _standbyFriday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_FRIDAY, timeValue)
                }
                9 -> {
                    _standbyFridayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_FRIDAY_END, timeValue)
                }
                10 -> {
                    _standbySaturday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_SATURDAY, timeValue)
                }
                11 -> {
                    _standbySaturdayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_SATURDAY_END, timeValue)
                }
                12 -> {
                    _standbySunday.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_SUNDAY, timeValue)
                }
                13 -> {
                    _standbySundayEnd.value = timeValue
                    dataStore.saveCoffeePreference(STANDBY_SUNDAY_END, timeValue)
                }
            }
        }
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

    private fun decodeTimeForString(timeValue: Int): String {
        val (hour, minute) = decodeTime(timeValue)
        return String.format("%02d:%02d", hour, minute)
    }

    private fun encodeTime(hour: Int, minute: Int): Int {
        if (hour in 0..23 || minute in 0..59) {
            return (hour shl 8) or minute
        }
        return 0
    }

    private fun decodeTime(timeValue: Int): Pair<Int, Int> {
        val hour = (timeValue shr 8) and 0xFF
        val minute = timeValue and 0xFF
        return Pair(hour, minute)
    }

    private fun decodeTimeToString(time: StateFlow<Int>): StateFlow<String> {
        return time.map {
            val (hour, minute) = decodeTime(it)
            String.format("%02d:%02d", hour, minute)
        }.stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

}