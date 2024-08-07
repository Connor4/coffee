package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import com.inno.coffee.data.DrinksModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StatisticProductViewModel @Inject constructor(
    private val repository: StatisticProductRepository,
) : ViewModel() {

    private val _drinksType = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksType: StateFlow<List<DrinksModel>> = _drinksType.asStateFlow()

    init {
        _drinksType.value = repository.drinkType
    }

    fun resetData() {

    }

}