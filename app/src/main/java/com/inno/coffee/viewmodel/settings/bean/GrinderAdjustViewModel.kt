package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrinderAdjustViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    private val TAG = "GrinderAdjustViewModel"
    private val LEFT_GRINDER_VALUE = "left_grinder_value"
    private val RIGHT_GRINDER_VALUE = "right_grinder_value"
    private val LEFT_DEFAULT_VALUE = 0
    private val RIGHT_DEFAULT_VALUE = 0

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
                dataStore.saveCoffeePreference(LEFT_GRINDER_VALUE,
                    if (add) _leftGrinderValue.value++ else _leftGrinderValue.value--)
            } else {
                dataStore.saveCoffeePreference(RIGHT_GRINDER_VALUE,
                    if (add) _rightGrinderValue.value++ else _rightGrinderValue.value--)
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
            if (left) {

            } else {

            }
        }
    }

}