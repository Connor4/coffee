package com.inno.coffee.data.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.DrinksHistory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DrinksHistoryViewModel @Inject constructor(
    private val drinksHistoryRepository: DrinksHistoryRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _drinksHistory = MutableStateFlow<List<DrinksHistory>>(emptyList())
    val drinksHistory: StateFlow<List<DrinksHistory>> = _drinksHistory.asStateFlow()

    init {
        viewModelScope.launch {
            drinksHistoryRepository.getAllDrinksHistory()
                .flowOn(defaultDispatcher)
                .collect {
                    _drinksHistory.value = it
                }
        }
    }

    fun insertDrinksHistory(drinksHistory: DrinksHistory) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                drinksHistoryRepository.insertDrinksHistory(drinksHistory)
            }
        }
    }

    fun deleteAllDrinksHistory() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                drinksHistoryRepository.deleteAllDrinksHistory()
            }
        }
    }

}