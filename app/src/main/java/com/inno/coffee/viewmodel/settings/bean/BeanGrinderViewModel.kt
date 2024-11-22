package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.BEAN_KEY_INDEX_ETC_FRONT
import com.inno.coffee.utilities.BEAN_KEY_INDEX_ETC_REAR
import com.inno.coffee.utilities.BEAN_KEY_INDEX_FRONT_HOPPER
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_FRONT
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_REAR
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDING_CAPACITY_FRONT
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDING_CAPACITY_REAR
import com.inno.coffee.utilities.BEAN_KEY_INDEX_LEVELLING
import com.inno.coffee.utilities.BEAN_KEY_INDEX_PQC
import com.inno.coffee.utilities.BEAN_KEY_INDEX_REAR_HOPPER
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeanGrinderViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val TAG = "BeanGrinderViewModel"
    private val REAR_HOPPER_NAME = "rear_hopper_name"
    private val FRONT_HOPPER_NAME = "front_hopper_name"
    private val LEVELLING = "levelling"
    private val PQC = "pqc"
    private val GRINDING_CAPACITY_FRONT = "grinding_capacity_front"
    private val GRINDING_CAPACITY_REAR = "grinding_capacity_rear"
    private val ETC_FRONT = "etc_front"
    private val ETC_REAR = "etc_rear"
    private val GRINDER_LIMIT_CAPACITY_REAR = "grinder_limit_capacity_rear"
    private val GRINDER_LIMIT_CAPACITY_FRONT = "grinder_limit_capacity_front"
    private val ETC_FRONT_REFERNECE = "etc_front_reference"
    private val ETC_REAR_REFERNECE = "etc_rear_reference"

    private val _rearHopperName = MutableStateFlow("")
    val rearHopperName: StateFlow<String> = _rearHopperName
    private val _frontHopperName = MutableStateFlow("")
    val frontHopperName: StateFlow<String> = _frontHopperName
    private val _levelling = MutableStateFlow(false)
    val levelling: StateFlow<Boolean> = _levelling
    private val _pqc = MutableStateFlow(false)
    val pqc: StateFlow<Boolean> = _pqc
    private val _grindingCapacityFront = MutableStateFlow(1.00f)
    val grindingCapacityFront: StateFlow<Float> = _grindingCapacityFront
    private val _grindingCapacityRear = MutableStateFlow(1.00f)
    val grindingCapacityRear: StateFlow<Float> = _grindingCapacityRear
    private val _etcFront = MutableStateFlow(false)
    val etcFront: StateFlow<Boolean> = _etcFront
    private val _etcRear = MutableStateFlow(false)
    val etcRear: StateFlow<Boolean> = _etcRear
    private val _rearGrinderLimitCapacity = MutableStateFlow(1.00f)
    val rearGrinderLimitCapacity: StateFlow<Float> = _rearGrinderLimitCapacity
    private val _frontGrinderLimitCapacity = MutableStateFlow(1.00f)
    val frontGrinderLimitCapacity: StateFlow<Float> = _frontGrinderLimitCapacity
    private val _etcFrontReference = MutableStateFlow(0.00f)
    val etcFrontReference: StateFlow<Float> = _etcFrontReference
    private val _etcRearReference = MutableStateFlow(0.00f)
    val etcRearReference: StateFlow<Float> = _etcRearReference

    fun init() {
        viewModelScope.launch {
            _rearHopperName.value = dataStore.getCoffeePreference(REAR_HOPPER_NAME, "rear")
            _frontHopperName.value = dataStore.getCoffeePreference(FRONT_HOPPER_NAME, "front")
            _levelling.value = dataStore.getCoffeePreference(LEVELLING, false)
            _pqc.value = dataStore.getCoffeePreference(PQC, false)
            _grindingCapacityFront.value =
                dataStore.getCoffeePreference(GRINDING_CAPACITY_FRONT, 1.00f)
            _grindingCapacityRear.value =
                dataStore.getCoffeePreference(GRINDING_CAPACITY_REAR, 1.00f)
            _etcFront.value = dataStore.getCoffeePreference(ETC_FRONT, false)
            _etcRear.value = dataStore.getCoffeePreference(ETC_REAR, false)
            _rearGrinderLimitCapacity.value =
                dataStore.getCoffeePreference(GRINDER_LIMIT_CAPACITY_REAR, 1.00f)
            _frontGrinderLimitCapacity.value =
                dataStore.getCoffeePreference(GRINDER_LIMIT_CAPACITY_FRONT, 1.00f)
            _etcFrontReference.value =
                dataStore.getCoffeePreference(ETC_FRONT_REFERNECE, 0.00f)
            _etcRearReference.value =
                dataStore.getCoffeePreference(ETC_REAR_REFERNECE, 0.00f)
        }
    }

    fun saveBeanGrinderValue(key: Int, value: Any) {
        Logger.d(TAG, "saveBeanGrinderValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                BEAN_KEY_INDEX_REAR_HOPPER -> {
                    dataStore.saveCoffeePreference(REAR_HOPPER_NAME, value as String)
                    _rearHopperName.value = value
                }
                BEAN_KEY_INDEX_FRONT_HOPPER -> {
                    dataStore.saveCoffeePreference(FRONT_HOPPER_NAME, value as String)
                    _frontHopperName.value = value
                }
                BEAN_KEY_INDEX_LEVELLING -> {
                    dataStore.saveCoffeePreference(LEVELLING, value as Boolean)
                    _levelling.value = value
                }
                BEAN_KEY_INDEX_PQC -> {
                    dataStore.saveCoffeePreference(PQC, value as Boolean)
                    _pqc.value = value
                }
                BEAN_KEY_INDEX_GRINDING_CAPACITY_REAR -> {
                    dataStore.saveCoffeePreference(GRINDING_CAPACITY_REAR, value as Float)
                    _grindingCapacityRear.value = value
                }
                BEAN_KEY_INDEX_GRINDING_CAPACITY_FRONT -> {
                    dataStore.saveCoffeePreference(GRINDING_CAPACITY_FRONT, value as Float)
                    _grindingCapacityFront.value = value
                }
                BEAN_KEY_INDEX_ETC_REAR -> {
                    dataStore.saveCoffeePreference(ETC_REAR, value as Boolean)
                    _etcRear.value = value
                }
                BEAN_KEY_INDEX_ETC_FRONT -> {
                    dataStore.saveCoffeePreference(ETC_FRONT, value as Boolean)
                    _etcFront.value = value
                }
                BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_REAR -> {
                    dataStore.saveCoffeePreference(GRINDER_LIMIT_CAPACITY_REAR, value as Float)
                    _rearGrinderLimitCapacity.value = value
                }
                BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_FRONT -> {
                    dataStore.saveCoffeePreference(GRINDER_LIMIT_CAPACITY_FRONT, value as Float)
                    _frontGrinderLimitCapacity.value = value
                }
            }
        }
    }


}