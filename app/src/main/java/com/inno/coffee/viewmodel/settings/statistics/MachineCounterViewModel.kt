package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MachineCounterViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            val resetTime = dataStore.getLastResetProductTime()
            val systemLanguage = dataStore.getSystemLanguage()
            val language = Locale.forLanguageTag(systemLanguage).language
            _time.value = TimeUtils.getNowTimeInYearAndHour(resetTime, language)
        }
    }
}