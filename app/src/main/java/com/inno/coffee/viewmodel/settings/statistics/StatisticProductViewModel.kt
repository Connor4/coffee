package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticProductViewModel @Inject constructor(
    private val repository: StatisticProductRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _drinksType = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksType: StateFlow<List<DrinksModel>> = _drinksType.asStateFlow()

    init {
        _drinksType.value = repository.drinkType
    }

    fun resetData() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.deleteAllProductCount()
            }
        }
    }

}