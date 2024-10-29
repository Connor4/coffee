package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.MAIN_SCREEN_PRODUCT_ID_LIMIT
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StatisticDayProductViewModel @Inject constructor(
    private val repository: StatisticProductRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val TAG = "StatisticDayProductViewModel"
    private val _typeCounts = MutableStateFlow<List<ProductTypeCount>>(emptyList())
    val typeCounts: StateFlow<List<ProductTypeCount>> = _typeCounts
    private val _productCount = MutableStateFlow(0)
    val productCount: StateFlow<Int> = _productCount.asStateFlow()
    private val _drinksList = MutableStateFlow<List<Formula>>(emptyList())
    val drinksList: StateFlow<List<Formula>> = _drinksList.asStateFlow()
    private val _formula = MutableStateFlow<Formula?>(null)
    val formula = _formula.asStateFlow()
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()

    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0

    fun loadDrinkTypeList() {
        viewModelScope.launch(defaultDispatcher) {
            _drinksList.value = repository.getAllFormula().filter {
                it.productType?.type != ProductType.OPERATION.value
                        && it.productId < MAIN_SCREEN_PRODUCT_ID_LIMIT
            }
            initQueryTime(_drinksList.value.first())
        }
    }

    fun initQueryTime(formula: Formula) {
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH) + 1
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        _time.value = "$currentYear/$currentMonth/$currentDay"
        getDayProductCount(formula)
    }

    fun getDayProductCount(formula: Formula) {
        viewModelScope.launch(defaultDispatcher) {
            _formula.value = repository.getFormulaByProductId(formula.productId)

            val (startTime, endTime) = getTimestampsForDate(currentYear, currentMonth, currentDay)
            _productCount.value =
                repository.getProductCountsForDate(startTime, endTime, formula.productId)
            _typeCounts.value = repository.getDayTypeCounts(startTime, endTime)
        }
    }

    fun getTimestampsForDate(year: Int, month: Int, day: Int): Pair<Long, Long> {
        val millisecondsPerDay = 86400000L

        val startOfDay = Calendar.getInstance().apply {
            set(year, month - 1, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfDay = startOfDay + millisecondsPerDay - 1
        return Pair(startOfDay, endOfDay)
    }

}