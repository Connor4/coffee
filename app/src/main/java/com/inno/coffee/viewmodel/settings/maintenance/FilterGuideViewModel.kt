package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterGuideViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    private val FILTER_GUIDE = "filter_guide"

    private val _filterGuide = MutableStateFlow(false)
    val filterGuide = _filterGuide

    fun init() {
        viewModelScope.launch {
            _filterGuide.value = dataStore.getCoffeePreference(FILTER_GUIDE, false)
        }
    }

    fun setFilterGuide(value: Boolean) {
        viewModelScope.launch {
            dataStore.saveCoffeePreference(FILTER_GUIDE, value)
            _filterGuide.value = value
        }
    }

}