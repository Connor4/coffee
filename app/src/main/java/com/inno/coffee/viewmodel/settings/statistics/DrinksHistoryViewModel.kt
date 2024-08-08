package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.ProductHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DrinksHistoryViewModel @Inject constructor(
    private val repository: DrinksHistoryRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val drinksHistory: StateFlow<List<ProductHistory>> = repository.getAllDrinksHistory().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun insertDrinksHistory(drinksHistory: ProductHistory) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.insertDrinksHistory(drinksHistory)
            }
        }
    }

    fun deleteAllDrinksHistory() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.deleteAllDrinksHistory()
            }
        }
    }

}