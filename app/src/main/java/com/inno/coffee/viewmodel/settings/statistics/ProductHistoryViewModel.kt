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
class ProductHistoryViewModel @Inject constructor(
    private val repository: ProductHistoryRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val productHistory: StateFlow<List<ProductHistory>> = repository.getAllProductHistory().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun insertProductHistory(drinksHistory: ProductHistory) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.insertProductHistory(drinksHistory)
            }
        }
    }

    fun deleteAllProductHistory() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.deleteAllProductHistory()
            }
        }
    }

}