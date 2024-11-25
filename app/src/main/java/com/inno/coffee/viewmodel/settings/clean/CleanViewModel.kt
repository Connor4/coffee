package com.inno.coffee.viewmodel.settings.clean

import androidx.lifecycle.ViewModel
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CleanViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    private val _mode = MutableStateFlow(0)
    val mode = _mode
    private val _cleanPeriod = MutableStateFlow(0)
    val cleanPeriod = _cleanPeriod
    private val _cleanTime = MutableStateFlow("")
    val cleanTime = _cleanTime
    private val _timeTolerance = MutableStateFlow(0)
    val timeTolerance = _timeTolerance
    private val _weekendCleanMode = MutableStateFlow(0)
    val weekendCleanMode = _weekendCleanMode
    private val _milkWeekendCleanMode = MutableStateFlow(0)
    val milkWeekendCleanMode = _milkWeekendCleanMode
    private val _afterCleaning = MutableStateFlow(false)
    val afterCleaning = _afterCleaning
    private val _standbyButton = MutableStateFlow(false)
    val standbyButton = _standbyButton

}