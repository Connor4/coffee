package com.inno.coffee.viewmodel.settings.formula

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MAIN_SCREEN_PRODUCT_ID_LIMIT
import com.inno.coffee.utilities.SECOND_SCREEN_PRODUCT_ID_LIMIT
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormulaViewModel @Inject constructor(
    private val repository: FormulaRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val TAG = "FormulaViewModel"
        private const val FORMULA_JSON_FILE = "/formula.txt"
    }

    val formulaList: StateFlow<List<Formula>> = repository.getAllFormulaFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
    val loadFileErrorDialogFlag = mutableStateOf(false)
    private val _drinksList = MutableStateFlow<List<Formula>>(emptyList())
    val drinksList: StateFlow<List<Formula>> = _drinksList.asStateFlow()
    private val _formula = MutableStateFlow<Formula?>(null)
    val formula = _formula.asStateFlow()

    fun loadDrinkTypeList(mainScreen: Boolean) {
        viewModelScope.launch(defaultDispatcher) {
            if (mainScreen) {
                _drinksList.value = repository.getAllFormula().filter {
                    !ProductType.isOperationType(it.productType?.type)
                            && it.productId < MAIN_SCREEN_PRODUCT_ID_LIMIT
                }
            } else {
                _drinksList.value = repository.getAllFormula().filter {
                    !ProductType.isOperationType(it.productType?.type)
                            && (it.productId in (MAIN_SCREEN_PRODUCT_ID_LIMIT + 1)..<SECOND_SCREEN_PRODUCT_ID_LIMIT)
                }
            }
            val first = _drinksList.value.first()
            _formula.value = repository.getFormulaByProductId(first.productId)
        }
    }

    fun loadFromSdCard(context: Context) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val path = context.filesDir.absolutePath + FORMULA_JSON_FILE
                val file = File(path)
                if (file.exists()) {
                    val jsonContent = file.readText()
                    try {
                        val list: List<Formula> = Json.decodeFromString(jsonContent)
                        repository.insertFormulaList(list)
                    } catch (e: Exception) {
                        Logger.e("FormulaViewModel", "decodeFromString Exception $e")
                        loadFileErrorDialogFlag.value = true
                    }
                } else {
                    loadFileErrorDialogFlag.value = true
                }
            }
        }
    }

    fun dismissFileNotFoundDialog() {
        loadFileErrorDialogFlag.value = false
    }

    fun insertFormula(formula: Formula) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.insertFormula(formula)
            }
        }
    }

    fun updateFormula(formula: Formula) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.updateFormula(formula)
            }
        }
    }

    fun getFormula(productId: Int = -1) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                _formula.value = repository.getFormulaByProductId(productId)
                Logger.d(TAG, "getFormula() ${_formula.value}")
            }
        }
    }

    fun setFormulaCups(targetCup: Int, formula: Formula?) {
        if (formula == null) {
            return
        }
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val targetProductId = if (targetCup == 1) {
                    formula.cups?.single
                } else {
                    formula.cups?.double
                }
                val targetFormula = repository.getFormulaByProductId(targetProductId ?: INVALID_INT)
                targetFormula?.let {
                    val newFormula = Formula(
                        productId = formula.productId,
                        productType = it.productType,
                        productName = formula.productName,
                        preFlush = it.preFlush,
                        postFlush = it.postFlush,
                        vat = it.vat,
                        coffeeWater = it.coffeeWater,
                        powderDosage = it.powderDosage,
                        pressWeight = it.pressWeight,
                        preMakeTime = it.preMakeTime,
                        postPreMakeWaitTime = it.postPreMakeWaitTime,
                        secPressWeight = it.secPressWeight,
                        hotWater = it.hotWater,
                        waterSequence = it.waterSequence,
                        coffeeCycles = it.coffeeCycles,
                        bypassWater = it.bypassWater,
                        cups = formula.cups,
                        waterPump = it.waterPump,
                        waterInputValue = it.waterInputValue,
                        leftValueLeftBoiler = it.leftValueLeftBoiler,
                        middleValueLeftBoiler = it.middleValueLeftBoiler,
                        rightValueLeftBoiler = it.rightValueLeftBoiler,
                        leftValueRightBoiler = it.leftValueRightBoiler,
                        middleValueRightBoiler = it.middleValueRightBoiler,
                        rightValueRightBoiler = it.rightValueRightBoiler,
                        id = formula.id
                    ).apply {
                        cups?.current = targetCup
                    }
                    repository.updateFormula(newFormula)
                    _formula.value = newFormula
                    Logger.d(TAG, "setFormulaCups: newFormula = $newFormula")
                }
            }
        }
    }

    fun assimilationProduct(formula: Formula?) {
        if (formula == null) {
            return
        }
        viewModelScope.launch(defaultDispatcher) {
            val productId = formula.productId
            val targetProductId = if (productId < MAIN_SCREEN_PRODUCT_ID_LIMIT) {
                productId + MAIN_SCREEN_PRODUCT_ID_LIMIT
            } else {
                productId - MAIN_SCREEN_PRODUCT_ID_LIMIT
            }
            val targetFormula = repository.getFormulaByProductId(targetProductId)
            targetFormula?.let {
                it.productType = formula.productType
                it.preFlush = formula.preFlush
                it.postFlush = formula.postFlush
                it.vat = formula.vat
                it.coffeeWater = formula.coffeeWater
                it.powderDosage = formula.powderDosage
                it.pressWeight = formula.pressWeight
                it.preMakeTime = formula.preMakeTime
                it.postPreMakeWaitTime = formula.postPreMakeWaitTime
                it.secPressWeight = formula.secPressWeight
                it.hotWater = formula.hotWater
                it.waterSequence = formula.waterSequence
                it.coffeeCycles = formula.coffeeCycles
                it.bypassWater = formula.bypassWater
                it.cups = formula.cups
                it.waterPump = formula.waterPump
                it.waterInputValue = formula.waterInputValue
                it.leftValueLeftBoiler = formula.leftValueLeftBoiler
                it.middleValueLeftBoiler = formula.middleValueLeftBoiler
                it.rightValueLeftBoiler = formula.rightValueLeftBoiler
                it.leftValueRightBoiler = formula.leftValueRightBoiler
                it.middleValueRightBoiler = formula.middleValueRightBoiler
                it.rightValueRightBoiler = formula.rightValueRightBoiler
                repository.updateFormula(it)
            }
        }
    }

}