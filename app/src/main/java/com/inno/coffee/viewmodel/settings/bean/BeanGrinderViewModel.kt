package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.CommandControlManager
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
import com.inno.coffee.utilities.ETC_FRONT
import com.inno.coffee.utilities.ETC_FRONT_REFERENCE
import com.inno.coffee.utilities.ETC_REAR
import com.inno.coffee.utilities.ETC_REAR_REFERENCE
import com.inno.coffee.utilities.FRONT_HOPPER_NAME
import com.inno.coffee.utilities.GRINDER_LIMIT_CAPACITY_FRONT
import com.inno.coffee.utilities.GRINDER_LIMIT_CAPACITY_REAR
import com.inno.coffee.utilities.GRINDING_CAPACITY_FRONT
import com.inno.coffee.utilities.GRINDING_CAPACITY_REAR
import com.inno.coffee.utilities.LEVELLING
import com.inno.coffee.utilities.PQC
import com.inno.coffee.utilities.REAR_HOPPER_NAME
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.BEAN_GRINDER_SETTING
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
    companion object {
        private const val TAG = "BeanGrinderViewModel"
    }

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
                dataStore.getCoffeePreference(ETC_FRONT_REFERENCE, 0.00f)
            _etcRearReference.value =
                dataStore.getCoffeePreference(ETC_REAR_REFERENCE, 0.00f)
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

            val pqcConOff = if (_pqc.value) 1 else 0
            val rearCapacity = _grindingCapacityRear.value * 100
            val frontCapacity = _grindingCapacityFront.value * 100
            val levelling = if (_levelling.value) 1 else 0
            val etcFront = if (_etcFront.value) 1 else 0
            val frontReference = _etcFrontReference.value
            val etcRear = if (_etcRear.value) 1 else 0
            val rearReference = _etcRearReference.value
            val rearLimitCapacity = _rearGrinderLimitCapacity.value * 100
            val frontLimitCapacity = _frontGrinderLimitCapacity.value * 100

            CommandControlManager.sendTestCommand(BEAN_GRINDER_SETTING, pqcConOff,
                rearCapacity.toInt(), frontCapacity.toInt(), levelling, etcFront,
                frontReference.toInt(), etcRear, rearReference.toInt(),
                rearLimitCapacity.toInt(), frontLimitCapacity.toInt(), 0, 0, 0, 0)
        }
    }

}