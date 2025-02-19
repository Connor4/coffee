package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ETCSettingViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    companion object {
        private const val ETC_FRONT_EXTRACT_TIME = "etc_front_extract_time"
        private const val ETC_BACK_EXTRACT_TIME = "etc_back_extract_time"
    }

    private val _etcFrontExtractTime = MutableStateFlow(0f)
    val etcFrontExtractTime = _etcFrontExtractTime
    private val _etcBackExtractTime = MutableStateFlow(0f)
    val etcBackExtractTime = _etcBackExtractTime

    init {
        viewModelScope.launch {
            _etcBackExtractTime.value = dataStore.getCoffeePreference(ETC_BACK_EXTRACT_TIME, 18f)
            _etcFrontExtractTime.value = dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_TIME, 18f)
        }
    }

    fun setEtcFrontExtractTime(value: Float) {
        viewModelScope.launch {
            dataStore.saveCoffeePreference(ETC_FRONT_EXTRACT_TIME, value)
            _etcFrontExtractTime.value = value
        }
    }

    fun setEtcBackExtractTime(value: Float) {
        viewModelScope.launch {
            dataStore.saveCoffeePreference(ETC_BACK_EXTRACT_TIME, value)
            _etcBackExtractTime.value = value
        }
    }

}