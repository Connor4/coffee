package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.function.CommandControlManager
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.GRINDER_ADJ_COARSER_ID
import com.inno.serialport.utilities.GRINDER_ADJ_FINER_ID
import com.inno.serialport.utilities.GRINDER_ADJ_GRIND_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrinderAdjustViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    companion object {
        private const val TAG = "GrinderAdjustViewModel"
        private const val LEFT_GRINDER_VALUE = "left_grinder_value"
        private const val RIGHT_GRINDER_VALUE = "right_grinder_value"
        private const val LEFT_DEFAULT_VALUE = 1
        private const val RIGHT_DEFAULT_VALUE = 1
        private const val MIN_VALUE = 1
        private const val MAX_VALUE = 100
    }

    private val _leftGrinderValue = MutableStateFlow(0)
    val leftGrinderValue = _leftGrinderValue

    private val _rightGrinderValue = MutableStateFlow(0)
    val rightGrinderValue = _rightGrinderValue

    init {
        viewModelScope.launch {
            _leftGrinderValue.value =
                dataStore.getCoffeePreference(LEFT_GRINDER_VALUE, LEFT_DEFAULT_VALUE)
            _rightGrinderValue.value =
                dataStore.getCoffeePreference(RIGHT_GRINDER_VALUE, RIGHT_DEFAULT_VALUE)
        }
    }

    fun saveGrinderValue(left: Boolean, add: Boolean) {
        Logger.d(TAG, "saveGrinderValue() called with: left = $left, add = $add")
        viewModelScope.launch {
            if (left) {
                if (_leftGrinderValue.value <= MAX_VALUE && add) {
                    dataStore.saveCoffeePreference(LEFT_GRINDER_VALUE, leftGrinderValue.value++)
                    CommandControlManager.sendTestCommand(GRINDER_ADJ_COARSER_ID, 0)
                }
                if (_leftGrinderValue.value > MIN_VALUE && !add) {
                    dataStore.saveCoffeePreference(LEFT_GRINDER_VALUE, leftGrinderValue.value--)
                    CommandControlManager.sendTestCommand(GRINDER_ADJ_FINER_ID, 0)
                }
            } else {
                if (_rightGrinderValue.value <= MAX_VALUE && add) {
                    dataStore.saveCoffeePreference(RIGHT_GRINDER_VALUE, rightGrinderValue.value++)
                    CommandControlManager.sendTestCommand(GRINDER_ADJ_COARSER_ID, 1)
                }
                if (_rightGrinderValue.value > MIN_VALUE && !add) {
                    dataStore.saveCoffeePreference(RIGHT_GRINDER_VALUE, rightGrinderValue.value--)
                    CommandControlManager.sendTestCommand(GRINDER_ADJ_FINER_ID, 1)
                }
            }
        }
    }

    fun resetGrinderValue(left: Boolean) {
        viewModelScope.launch {
            if (left) {
                dataStore.saveCoffeePreference(LEFT_GRINDER_VALUE, LEFT_DEFAULT_VALUE)
                _leftGrinderValue.value = LEFT_DEFAULT_VALUE
            } else {
                dataStore.saveCoffeePreference(RIGHT_GRINDER_VALUE, RIGHT_DEFAULT_VALUE)
                _rightGrinderValue.value = RIGHT_DEFAULT_VALUE
            }
        }
    }

    fun grinderTest(left: Boolean) {
        viewModelScope.launch {
            CommandControlManager.sendTestCommand(GRINDER_ADJ_GRIND_ID, if (left) 0 else 1)
        }
    }

}