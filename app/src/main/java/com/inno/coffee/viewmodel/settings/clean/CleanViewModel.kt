package com.inno.coffee.viewmodel.settings.clean

import android.util.Log
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
import com.inno.coffee.utilities.DEFAULT_END_TIME
import com.inno.coffee.utilities.DEFAULT_START_TIME
import com.inno.coffee.utilities.STANDBY_FRIDAY
import com.inno.coffee.utilities.STANDBY_FRIDAY_END
import com.inno.coffee.utilities.STANDBY_MONDAY
import com.inno.coffee.utilities.STANDBY_MONDAY_END
import com.inno.coffee.utilities.STANDBY_SATURDAY
import com.inno.coffee.utilities.STANDBY_SATURDAY_END
import com.inno.coffee.utilities.STANDBY_SUNDAY
import com.inno.coffee.utilities.STANDBY_SUNDAY_END
import com.inno.coffee.utilities.STANDBY_THURSDAY
import com.inno.coffee.utilities.STANDBY_THURSDAY_END
import com.inno.coffee.utilities.STANDBY_TUESDAY
import com.inno.coffee.utilities.STANDBY_TUESDAY_END
import com.inno.coffee.utilities.STANDBY_WEDNESDAY
import com.inno.coffee.utilities.STANDBY_WEDNESDAY_END
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
    }

    private val _mode = MutableStateFlow(0)
    val mode = _mode
    private val _cleanPeriod = MutableStateFlow(1)
    val cleanPeriod = _cleanPeriod
    private val _cleanTime = MutableStateFlow(0)
    val cleanTime = decodeTimeToString(_cleanTime)
    private val _timeTolerance = MutableStateFlow(1)
    val timeTolerance = _timeTolerance
    private val _weekendCleanMode = MutableStateFlow(0)
    val weekendCleanMode = _weekendCleanMode
    private val _milkWeekendCleanMode = MutableStateFlow(false)
    val milkWeekendCleanMode = _milkWeekendCleanMode
    private val _afterCleaning = MutableStateFlow(false)
    val afterCleaning = _afterCleaning
    private val _standbyButton = MutableStateFlow(true)
    val standbyButton = _standbyButton

    private val _standbyMonday = MutableStateFlow(DEFAULT_START_TIME)
    val standbyMonday = decodeTimeToString(_standbyMonday)
    private val _standbyMondayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbyMondayEnd = decodeTimeToString(_standbyMondayEnd)
    private val _standbyTuesday = MutableStateFlow(DEFAULT_START_TIME)
    val standbyTuesday = decodeTimeToString(_standbyTuesday)
    private val _standbyTuesdayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbyTuesdayEnd = decodeTimeToString(_standbyTuesdayEnd)
    private val _standbyWednesday = MutableStateFlow(DEFAULT_START_TIME)
    val standbyWednesday = decodeTimeToString(_standbyWednesday)
    private val _standbyWednesdayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbyWednesdayEnd = decodeTimeToString(_standbyWednesdayEnd)
    private val _standbyThursday = MutableStateFlow(DEFAULT_START_TIME)
    val standbyThursday = decodeTimeToString(_standbyThursday)
    private val _standbyThursdayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbyThursdayEnd = decodeTimeToString(_standbyThursdayEnd)
    private val _standbyFriday = MutableStateFlow(DEFAULT_START_TIME)
    val standbyFriday = decodeTimeToString(_standbyFriday)
    private val _standbyFridayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbyFridayEnd = decodeTimeToString(_standbyFridayEnd)
    private val _standbySaturday = MutableStateFlow(DEFAULT_START_TIME)
    val standbySaturday = decodeTimeToString(_standbySaturday)
    private val _standbySaturdayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbySaturdayEnd = decodeTimeToString(_standbySaturdayEnd)
    private val _standbySunday = MutableStateFlow(DEFAULT_START_TIME)
    val standbySunday = decodeTimeToString(_standbySunday)
    private val _standbySundayEnd = MutableStateFlow(DEFAULT_END_TIME)
    val standbySundayEnd = decodeTimeToString(_standbySundayEnd)

    private val _switchValue = MutableStateFlow(0)
    val switchValue = _switchValue

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            val time = dataStore.getCoffeePreference(CLEAN_TIME, 0)
            val monday = dataStore.getCoffeePreference(STANDBY_MONDAY, DEFAULT_START_TIME)
            val mondayEnd = dataStore.getCoffeePreference(STANDBY_MONDAY_END, DEFAULT_END_TIME)
            val tuesday = dataStore.getCoffeePreference(STANDBY_TUESDAY, DEFAULT_START_TIME)
            val tuesdayEnd = dataStore.getCoffeePreference(STANDBY_TUESDAY_END, DEFAULT_END_TIME)
            val wednesday = dataStore.getCoffeePreference(STANDBY_WEDNESDAY, DEFAULT_START_TIME)
            val wednesdayEnd =
                dataStore.getCoffeePreference(STANDBY_WEDNESDAY_END, DEFAULT_END_TIME)
            val thursday = dataStore.getCoffeePreference(STANDBY_THURSDAY, DEFAULT_START_TIME)
            val thursdayEnd = dataStore.getCoffeePreference(STANDBY_THURSDAY_END, DEFAULT_END_TIME)
            val friday = dataStore.getCoffeePreference(STANDBY_FRIDAY, DEFAULT_START_TIME)
            val fridayEnd = dataStore.getCoffeePreference(STANDBY_FRIDAY_END, DEFAULT_END_TIME)
            val saturday = dataStore.getCoffeePreference(STANDBY_SATURDAY, DEFAULT_START_TIME)
            val saturdayEnd = dataStore.getCoffeePreference(STANDBY_SATURDAY_END, DEFAULT_END_TIME)
            val sunday = dataStore.getCoffeePreference(STANDBY_SUNDAY, DEFAULT_START_TIME)
            val sundayEnd = dataStore.getCoffeePreference(STANDBY_SUNDAY_END, DEFAULT_END_TIME)

            _cleanTime.value = time
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
            _standbyButton.value = dataStore.getCoffeePreference(STANDBY_BUTTON, true)

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
        viewModelScope.launch(defaultDispatcher) {
            dataStore.saveCoffeePreference(SWITCH_VALUE, _switchValue.value)
        }
    }

    fun saveCleanTime(hour: Int, minute: Int) {
        val timeValue = encodeTime(hour, minute)
        _cleanTime.value = timeValue
        viewModelScope.launch(defaultDispatcher) {
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
            14 -> decodeTime(_cleanTime.value)
            else -> Pair(0, 0)
        }
        return Pair(hour, minute)
    }

    fun timeCheck(index: Int, hour: Int, minute: Int): Boolean {
        val timeValue = encodeTime(hour, minute)
        return when (index) {
            0 -> return _standbyMondayEnd.value > timeValue
            1 -> return _standbyMonday.value < timeValue
            2 -> return _standbyTuesdayEnd.value > timeValue
            3 -> return _standbyTuesday.value < timeValue
            4 -> return _standbyWednesdayEnd.value > timeValue
            5 -> return _standbyWednesday.value < timeValue
            6 -> return _standbyThursdayEnd.value > timeValue
            7 -> return _standbyThursday.value < timeValue
            8 -> return _standbyFridayEnd.value > timeValue
            9 -> return _standbyFriday.value < timeValue
            10 -> return _standbySaturdayEnd.value > timeValue
            11 -> return _standbySaturday.value < timeValue
            12 -> return _standbySundayEnd.value > timeValue
            13 -> return _standbySunday.value < timeValue
            else -> false
        }
    }

    fun saveStandbyTime(index: Int, hour: Int, minute: Int) {
        viewModelScope.launch(defaultDispatcher) {
            val timeValue = encodeTime(hour, minute)
            Log.d(TAG, "saveStandbyTime() called $timeValue")
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