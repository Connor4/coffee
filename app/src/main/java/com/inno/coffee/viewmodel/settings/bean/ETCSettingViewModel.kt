package com.inno.coffee.viewmodel.settings.bean

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.utilities.ETC_FRONT
import com.inno.coffee.utilities.ETC_REAR
import com.inno.coffee.utilities.SHOW_LEFT_SCREEN
import com.inno.coffee.viewmodel.settings.formula.FormulaRepository
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ETCSettingViewModel @Inject constructor(
    private val repository: FormulaRepository,
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    companion object {
        private const val TAG = "ETCSettingViewModel"
        private const val ETC_FRONT_EXTRACT_TIME = "etc_front_extract_time"
        private const val ETC_BACK_EXTRACT_TIME = "etc_back_extract_time"
        private const val ETC_FRONT_EXTRACT_RANGE_START = "etc_front_extract_range_start"
        private const val ETC_FRONT_EXTRACT_RANGE_END = "etc_front_extract_range_end"
        private const val ETC_REAR_EXTRACT_RANGE_START = "etc_rear_extract_range_start"
        private const val ETC_REAR_EXTRACT_RANGE_END = "etc_rear_extract_range_end"
        private const val ETC_BLADE_FRONT = "etc_blade_front"
        private const val ETC_BLADE_BACK = "etc_blade_back"
        private const val ETC_ADJUST_FRONT = "etc_adjust_front"
        private const val ETC_ADJUST_BACK = "etc_adjust_back"
        private const val TEMPERATURE_UNIT = "temperature_unit"
    }

    private val _etcExtractTime = MutableStateFlow(0f)
    val etcExtractTime = _etcExtractTime.asStateFlow()
    private val _etcRangeStart = MutableStateFlow(0f)
    val etcRangeStart = _etcRangeStart
    private val _etcRangeEnd = MutableStateFlow(0f)
    val etcRangeEnd = _etcRangeEnd

    private val _drinksList = MutableStateFlow<List<Formula>>(emptyList())
    val drinksList: StateFlow<List<Formula>> = _drinksList.asStateFlow()
    private val _formula = MutableStateFlow<Formula?>(null)
    val formula = _formula.asStateFlow()
    private val _tempUnit = MutableStateFlow(false)
    val tempUnit = _tempUnit
    private val _page = MutableStateFlow(0)
    val page = _page.asStateFlow()
    private val _totalPageCount = MutableStateFlow(0)
    val totalPageCount = _totalPageCount.asStateFlow()

    private val _blade = MutableStateFlow(0f)
    val blade = _blade.asStateFlow()
    private val _adjust = MutableStateFlow(0f)
    val adjust = _adjust.asStateFlow()

    private var frontSwitch = false
    private var rearSwitch = false

    init {
        viewModelScope.launch {
            _tempUnit.value = dataStore.getCoffeePreference(TEMPERATURE_UNIT, false)
            val etcFrontSwitch = dataStore.getCoffeePreference(ETC_FRONT, false)
            val etcRearSwitch = dataStore.getCoffeePreference(ETC_REAR, false)
            if (etcFrontSwitch && etcRearSwitch) {
                frontSwitch = true
                rearSwitch = true
                _totalPageCount.value = 7
                _etcExtractTime.value = dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_TIME, 18f)
            } else if (etcFrontSwitch) {
                frontSwitch = true
                _totalPageCount.value = 4
                _etcExtractTime.value = dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_TIME, 18f)
            } else if (etcRearSwitch) {
                rearSwitch = true
                _totalPageCount.value = 4
                _etcExtractTime.value = dataStore.getCoffeePreference(ETC_BACK_EXTRACT_TIME, 25f)
            }
        }
    }

    fun loadETCDrinkList(front: Boolean) {
        viewModelScope.launch(defaultDispatcher) {
            _drinksList.value = repository.getAllFormula().filter {
                it.beanHopper?.position == front && ProductType.isFormulaCanShowType(
                    it.productType?.type) && it.showType == SHOW_LEFT_SCREEN
            }
            _formula.value = _drinksList.value.firstOrNull()
        }
    }

    fun prevPage() {
        _page.value = (_page.value - 1 + _totalPageCount.value) % _totalPageCount.value
        Log.d(TAG, "prevPage() called ${_page.value}")
        loadPageContent()
    }

    fun nextPage() {
        _page.value = (_page.value + 1) % _totalPageCount.value
        Log.d(TAG, "nextPage() called ${_page.value}")
        loadPageContent()
    }

    fun getMappedPageIndex(pageIndex: Int): Int {
        return when {
            !frontSwitch && pageIndex == 1 -> 5  // 当frontSwitch关闭，第二页映射到第5页
            !frontSwitch && pageIndex == 2 -> 6  // 当frontSwitch关闭，第三页映射到第6页
            !frontSwitch && pageIndex == 3 -> 7  // 当frontSwitch关闭，第四页映射到第7页
            frontSwitch && pageIndex == 1 -> 2  // 当frontSwitch打开，第二页映射到第2页
            frontSwitch && pageIndex == 2 -> 3  // 当frontSwitch打开，第三页映射到第3页
            frontSwitch && pageIndex == 3 -> 4  // 当frontSwitch打开，第四页映射到第4页
            else -> pageIndex + 1
        }
    }

    fun loadPageContent() {
        val actualPageIndex = getMappedPageIndex(_page.value)
        when (actualPageIndex) {
            2 -> {
                loadETCDrinkList(true)
            }
            3 -> {
                viewModelScope.launch {
                    _etcExtractTime.value = dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_TIME,
                        18f)
                    _etcRangeStart.value =
                        dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_RANGE_START, 12f)
                    _etcRangeEnd.value =
                        dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_RANGE_END, 25f)
                }
            }
            4 -> {
                viewModelScope.launch {
                    _blade.value = dataStore.getCoffeePreference(ETC_BLADE_FRONT, 0f)
                    _adjust.value = dataStore.getCoffeePreference(ETC_ADJUST_FRONT, 0f)
                }
            }
            5 -> {
                loadETCDrinkList(false)
            }
            6 -> {
                viewModelScope.launch {
                    _etcExtractTime.value = dataStore.getCoffeePreference(ETC_BACK_EXTRACT_TIME,
                        25f)
                    _etcRangeStart.value =
                        dataStore.getCoffeePreference(ETC_REAR_EXTRACT_RANGE_START, 25f)
                    _etcRangeEnd.value =
                        dataStore.getCoffeePreference(ETC_REAR_EXTRACT_RANGE_END, 40f)
                }
            }
            7 -> {
                viewModelScope.launch {
                    _blade.value = dataStore.getCoffeePreference(ETC_BLADE_BACK, 0f)
                    _adjust.value = dataStore.getCoffeePreference(ETC_ADJUST_BACK, 0f)
                }
            }
        }

    }

    fun setEtcExtractTime(page: Int, value: Float) {
        viewModelScope.launch {
            val key = if (page == 2) {
                ETC_FRONT_EXTRACT_TIME
            } else {
                ETC_BACK_EXTRACT_TIME
            }
            dataStore.saveCoffeePreference(key, value)
            _etcExtractTime.value = value
        }
    }

    fun getFormula(productId: Int = -1) {
        viewModelScope.launch(defaultDispatcher) {
            _formula.value = repository.getFormulaByProductId(productId)
            Logger.d(TAG, "getFormula() ${_formula.value}")
        }
    }

    fun productTest(formula: Formula, main: Boolean) {
        Logger.d(TAG, "productTest() called with: formula = ${formula.productId}, main = $main")
        if (ProductType.isOperationType(formula.productType?.type)) {
            if (main) {
                MakeLeftDrinksHandler.executeNow(formula)
            } else {
                MakeRightDrinksHandler.executeNow(formula)
            }
        } else {
            if (main) {
                MakeLeftDrinksHandler.enqueueMessage(formula)
            } else {
                MakeRightDrinksHandler.enqueueMessage(formula)
            }
        }
    }

    fun updateFormula(formula: Formula) {
        viewModelScope.launch(defaultDispatcher) {
            repository.updateFormula(formula)
        }
    }


}