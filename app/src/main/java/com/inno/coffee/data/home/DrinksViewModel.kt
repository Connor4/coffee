package com.inno.coffee.data.home

import androidx.lifecycle.ViewModel
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DrinksViewModel @Inject constructor(
    private val drinksRepository: DrinksRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _drinksTypes = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksTypes: StateFlow<List<DrinksModel>> = _drinksTypes.asStateFlow()

    init {
        _drinksTypes.value = drinksRepository.drinksType
    }

}
