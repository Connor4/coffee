package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.ProductCount
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticProductViewModel @Inject constructor(
    private val repository: StatisticProductRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    private val _drinksType = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksType: StateFlow<List<DrinksModel>> = _drinksType.asStateFlow()
    private val _typeCounts = MutableStateFlow<List<ProductTypeCount>>(emptyList())
    val typeCounts: StateFlow<List<ProductTypeCount>> = _typeCounts
    private val _productCount = MutableStateFlow<ProductCount?>(null)
    val productCount: StateFlow<ProductCount?> = _productCount.asStateFlow()
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            _drinksType.value = repository.drinkType.filter {
                it.type != ProductType.OPERATION
            }
            _typeCounts.value = repository.getTypeCounts()
            val resetTime = dataStore.getLastResetProductTime()
            val language = dataStore.getSystemLanguage()
            _time.value = TimeUtils.getNowTimeInYearAndHour(resetTime, language)
        }
    }

    fun resetData() {
        viewModelScope.launch(defaultDispatcher) {
            repository.deleteAllProductCount()

            val nowTime = System.currentTimeMillis()
            val language = dataStore.getSystemLanguage()
            val nowTimeFormat = TimeUtils.getNowTimeInYearAndHour(nowTime, language)
            _time.value = nowTimeFormat
            dataStore.saveLastResetProductTime(nowTime)
        }
    }

    fun getProductCount(productId: Int) {
        viewModelScope.launch(defaultDispatcher) {
            val count = repository.getProductCountByProductId(productId)
            _productCount.value = count
        }
    }

}