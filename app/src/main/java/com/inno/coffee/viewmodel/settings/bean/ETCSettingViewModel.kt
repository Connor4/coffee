package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.utilities.ETC_FRONT
import com.inno.coffee.utilities.ETC_REAR
import com.inno.coffee.utilities.SHOW_LEFT_SCREEN
import com.inno.coffee.utilities.SHOW_RIGHT_SCREEN
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
        private const val TEMPERATURE_UNIT = "temperature_unit"
    }

    private val _etcFrontExtractTime = MutableStateFlow(0f)
    val etcFrontExtractTime = _etcFrontExtractTime
    private val _etcBackExtractTime = MutableStateFlow(0f)
    val etcBackExtractTime = _etcBackExtractTime

    private val _drinksList = MutableStateFlow<List<Formula>>(emptyList())
    val drinksList: StateFlow<List<Formula>> = _drinksList.asStateFlow()
    private val _formula = MutableStateFlow<Formula?>(null)
    val formula = _formula.asStateFlow()
    private val _tempUnit = MutableStateFlow(false)
    val tempUnit = _tempUnit

    var index = 0
    var totalViewpagerCount = 0
    var range = 0..0

    init {
        viewModelScope.launch {
            _etcBackExtractTime.value = dataStore.getCoffeePreference(ETC_BACK_EXTRACT_TIME, 18f)
            _etcFrontExtractTime.value = dataStore.getCoffeePreference(ETC_FRONT_EXTRACT_TIME, 18f)
            _tempUnit.value = dataStore.getCoffeePreference(TEMPERATURE_UNIT, false)
            val etcFrontSwitch = dataStore.getCoffeePreference(ETC_FRONT, false)
            val etcRearSwitch = dataStore.getCoffeePreference(ETC_REAR, false)
            if (etcFrontSwitch && etcRearSwitch) {
                index = 1
                range = 1..6
                totalViewpagerCount = 7
            } else if (etcFrontSwitch) {
                index = 1
                range = 1..3
                totalViewpagerCount = 4
            } else if (etcRearSwitch) {
                index = 4
                range = 4..6
                totalViewpagerCount = 4
            }
        }
    }

    fun loadETCDrinkList(mainScreen: Boolean, front: Boolean) {
        viewModelScope.launch(defaultDispatcher) {
            if (mainScreen) {
                _drinksList.value = repository.getAllFormula().filter {
                    it.beanHopper?.position == front && ProductType.isFormulaCanShowType(
                        it.productType?.type) && it.showType == SHOW_LEFT_SCREEN
                }
            } else {
                _drinksList.value = repository.getAllFormula().filter {
                    it.beanHopper?.position == front && ProductType.isFormulaCanShowType(
                        it.productType?.type) && (it.showType == SHOW_RIGHT_SCREEN)
                }
            }
            _formula.value = _drinksList.value.first()
        }
    }

    fun setEtcFrontExtractTime(value: Float) {
        viewModelScope.launch {
            dataStore.saveCoffeePreference(ETC_FRONT_EXTRACT_TIME, value)
            _etcFrontExtractTime.value = value
        }
    }

    fun setEtcBackExtractTime(value: Float) {
        viewModelScope.launch {
            dataStore.saveCoffeePreference(ETC_BACK_EXTRACT_TIME, value)
            _etcBackExtractTime.value = value
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