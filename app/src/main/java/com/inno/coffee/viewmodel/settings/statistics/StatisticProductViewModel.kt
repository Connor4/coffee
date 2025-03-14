package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.SHOW_LEFT_SCREEN
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class StatisticProductViewModel @Inject constructor(
    private val repository: StatisticProductRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    companion object {
        private const val TAG = "StatisticProductViewModel"
    }

    private val _typeCounts = MutableStateFlow<List<ProductTypeCount>>(emptyList())
    val typeCounts: StateFlow<List<ProductTypeCount>> = _typeCounts
    private val _productCount = MutableStateFlow(0)
    val productCount: StateFlow<Int> = _productCount.asStateFlow()
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()
    private val _drinksList = MutableStateFlow<List<Formula>>(emptyList())
    val drinksList: StateFlow<List<Formula>> = _drinksList.asStateFlow()
    private val _formula = MutableStateFlow<Formula?>(null)
    val formula = _formula.asStateFlow()

    fun loadDrinkTypeList() {
        Logger.d(TAG, "loadDrinkTypeList() called")
        viewModelScope.launch(defaultDispatcher) {
            _drinksList.value = repository.getAllFormula().filter {
                !ProductType.isOperationType(
                    it.productType?.type) && it.showType == SHOW_LEFT_SCREEN
            }
            val resetTime = dataStore.getLastResetProductTime()
            val localDateTime = LocalDateTime.parse(resetTime)
            val startTime = localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
            val endTime = System.currentTimeMillis()
            getProductCount(_drinksList.value.first().productId)
            _time.value = TimeUtils.getYearMonthDayFormat(localDateTime)
            _typeCounts.value = repository.getTypeCountsByTime(startTime, endTime)
        }
    }

    fun resetData() {
        Logger.d(TAG, "resetData()")
        viewModelScope.launch(defaultDispatcher) {
            val nowTime = LocalDateTime.now()
            val startTime = nowTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
            _time.value = TimeUtils.getYearMonthDayFormat(nowTime)
            _typeCounts.value = repository.getTypeCountsByTime(startTime, startTime)
            getProductCount(_drinksList.value.first().productId)
            dataStore.saveLastResetProductTime(nowTime.toString())
        }
    }

    fun getProductCount(productId: Int) {
        viewModelScope.launch(defaultDispatcher) {
            _formula.value = repository.getFormulaByProductId(productId)
            _productCount.value = repository.getProductCountByProductId(productId)
            Logger.d(TAG, "getProductCount() productCount =  ${_productCount.value}" +
                    " formula = ${_formula.value}")
        }
    }

}