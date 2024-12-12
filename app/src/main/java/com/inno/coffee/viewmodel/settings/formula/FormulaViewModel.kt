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
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.COFFEE_INPUT_COMMAND_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.STEAM_INPUT_COMMAND_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormulaViewModel @Inject constructor(
    private val repository: FormulaRepository,
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val TAG = "FormulaViewModel"
        private const val FORMULA_JSON_FILE = "/formula.txt"
        private const val TEMPERATURE_UNIT = "temperature_unit"
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
    private val _tempUnit = MutableStateFlow(false)
    private val _leftTemp = MutableStateFlow(0)
    val leftTemp = temperatureDisplayFlow(_leftTemp)
    private val _rightTemp = MutableStateFlow(0)
    val rightTemp = temperatureDisplayFlow(_rightTemp)
    private val _wandTemp = MutableStateFlow(0)
    val wandTemp = temperatureDisplayFlow(_wandTemp)
    private val _steamTemp = MutableStateFlow(0)
    val steamTemp = temperatureDisplayFlow(_steamTemp)

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        viewModelScope.launch {
            _tempUnit.value = dataStore.getCoffeePreference(TEMPERATURE_UNIT, false)
        }
        DataCenter.subscribe(ReceivedDataType.COMMON_REPLY, subscriber)
    }

    fun loadDrinkTypeList(mainScreen: Boolean) {
        viewModelScope.launch(defaultDispatcher) {
            if (mainScreen) {
                _drinksList.value = repository.getAllFormula().filter {
                    ProductType.isFormulaCanShowType(it.productType?.type)
                            && it.productId < MAIN_SCREEN_PRODUCT_ID_LIMIT
                }
            } else {
                _drinksList.value = repository.getAllFormula().filter {
                    ProductType.isFormulaCanShowType(it.productType?.type)
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
                        beanHopper = it.beanHopper,
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
                it.beanHopper = formula.beanHopper
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

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.CommonReply) {
            val params = data.params
            val commandId = data.commandId
            if (commandId == COFFEE_INPUT_COMMAND_ID) {
                val leftTemp =
                    ((params[15].toInt() and 0xFF) shl 8) or (params[14].toInt() and 0xFF)
                _leftTemp.value = leftTemp
                val rightTemp =
                    ((params[17].toInt() and 0xFF) shl 8) or (params[16].toInt() and 0xFF)
                _rightTemp.value = rightTemp
                val leftFlow =
                    ((params[19].toInt() and 0xFF) shl 8) or (params[18].toInt() and 0xFF)
            } else if (commandId == STEAM_INPUT_COMMAND_ID) {
                // TODO 需要区分左右屏的蒸汽棒温度
//                val steamPressure =
//                    ((params[1].toInt() and 0xFF) shl 8) or (params[0].toInt() and 0xFF)
//                _steamPressure.value = steamPressure / 10f
//                _securityLevel.value = params[3] == ONE_IN_BYTE
//                _workLevel.value = params[5] == ONE_IN_BYTE
//                val leftWandTemp =
//                    ((params[7].toInt() and 0xFF) shl 8) or (params[6].toInt() and 0xFF)
//                _leftWandTemp.value = leftWandTemp / 10f
//                val rightWandTemp =
//                    ((params[9].toInt() and 0xFF) shl 8) or (params[8].toInt() and 0xFF)
//                _rightWandTemp.value = rightWandTemp / 10f
            }
        }
    }

    private fun temperatureDisplayFlow(temperatureFlow: StateFlow<Int>): StateFlow<String> {
        return _tempUnit.combine(temperatureFlow) { isFahrenheit, tempCelsius ->
            if (isFahrenheit) {
                val fahrenheit = tempCelsius * 1.8 + 32
                "$fahrenheit °F"
            } else {
                "$tempCelsius °C"
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, "")

    }

}