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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MachineCounterViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val repository: MachineCounterRepository,
) : ViewModel() {

    private val _absoluteCounter = MutableStateFlow(0)
    val absoluteCounter: StateFlow<Int> = _absoluteCounter.asStateFlow()
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            val resetTime = dataStore.getLastResetProductTime()

            _time.value = TimeUtils.getFullFormat(LocalDateTime.parse(resetTime))
            _absoluteCounter.value = repository.getAllProductCount()
        }
    }
}