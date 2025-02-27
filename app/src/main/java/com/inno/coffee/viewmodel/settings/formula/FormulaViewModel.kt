package com.inno.coffee.viewmodel.settings.formula

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MAIN_SCREEN_PRODUCT_ID_LIMIT
import com.inno.coffee.utilities.SHOW_LEFT_SCREEN
import com.inno.coffee.utilities.SHOW_RIGHT_SCREEN
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
    val updateFormulaFlag = repository.updateFormula
    private val _tempUnit = MutableStateFlow(false)
    val tempUnit = _tempUnit
    private val _leftTemp = MutableStateFlow(0)
    val leftTemp = temperatureDisplayFlow(_leftTemp)
    private val _rightTemp = MutableStateFlow(0)
    val rightTemp = temperatureDisplayFlow(_rightTemp)
    private val _wandTemp = MutableStateFlow(0)
    val wandTemp = temperatureDisplayFlow(_wandTemp)
    private val _steamPressure = MutableStateFlow(0f)
    val steamPressure = _steamPressure

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
                            && it.showType == SHOW_LEFT_SCREEN
                }
            } else {
                _drinksList.value = repository.getAllFormula().filter {
                    ProductType.isFormulaCanShowType(
                        it.productType?.type) && (it.showType == SHOW_RIGHT_SCREEN)
                }
            }
            _formula.value = _drinksList.value.first()
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

    fun refreshDrinkTypeList(mainScreen: Boolean) {
        viewModelScope.launch(defaultDispatcher) {
            if (mainScreen) {
                _drinksList.value = repository.getAllFormula().filter {
                    ProductType.isFormulaCanShowType(it.productType?.type)
                            && it.showType == SHOW_LEFT_SCREEN
                }
            } else {
                _drinksList.value = repository.getAllFormula().filter {
                    ProductType.isFormulaCanShowType(
                        it.productType?.type) && (it.showType == SHOW_RIGHT_SCREEN)
                }
            }
        }
    }

    fun loadFromSdCard(context: Context) {
        viewModelScope.launch(defaultDispatcher) {
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

    fun dismissFileNotFoundDialog() {
        loadFileErrorDialogFlag.value = false
    }

    fun insertFormula(formula: Formula) {
        viewModelScope.launch(defaultDispatcher) {
            repository.insertFormula(formula)
        }
    }

    fun updateFormula(formula: Formula) {
        viewModelScope.launch(defaultDispatcher) {
            repository.updateFormula(formula)
        }
    }

    fun getFormula(productId: Int = -1) {
        viewModelScope.launch(defaultDispatcher) {
            _formula.value = repository.getFormulaByProductId(productId)
            Logger.d(TAG, "getFormula() ${_formula.value}")
        }
    }

    fun setFormulaCups(targetCup: Int, source: Formula?) {
        if (source == null) {
            return
        }
        viewModelScope.launch(defaultDispatcher) {
            val targetProductId = if (targetCup == 1) {
                source.cups?.single
            } else {
                source.cups?.double
            }
            val target = repository.getFormulaByProductId(targetProductId ?: INVALID_INT)
            target?.let {
                val new = Formula(
                    productId = source.productId,
                    preFlush = target.preFlush,
                    postFlush = target.postFlush,
                    productType = target.productType,
                    productPrice = target.productPrice,
                    productName = target.productName,
                    beanHopper = target.beanHopper,
                    coffeeWater = target.coffeeWater,
                    powderDosage = target.powderDosage,
                    pressWeight = target.pressWeight,
                    preMakeTime = target.preMakeTime,
                    postPreMakeWaitTime = target.postPreMakeWaitTime,
                    secPressWeight = target.secPressWeight,
                    hotWater = target.hotWater,
                    waterSequence = target.waterSequence,
                    coffeeCycles = target.coffeeCycles,
                    bypassWater = target.bypassWater,

                    manualFoamTime = target.manualFoamTime,
                    autoFoamTemperature = target.autoFoamTemperature,
                    foamMode = target.foamMode,
                    stopAirTime = target.stopAirTime,
                    stopAirTemperature = target.stopAirTemperature,
                    texture = target.texture,
                    mixHotWater = target.mixHotWater,
                    cleanWand = target.cleanWand,

                    appearance = target.appearance,
                    milkDelayTime = target.milkDelayTime,
                    milkOutput = target.milkOutput,
                    milkSequence = target.milkSequence,
                    cups = target.cups,
                    imageRes = target.imageRes,
                    steamBoiler = target.steamBoiler,
                    waterInputValue = target.waterInputValue,
                    leftValueLeftBoiler = target.leftValueLeftBoiler,
                    middleValueLeftBoiler = target.middleValueLeftBoiler,
                    rightValueLeftBoiler = target.rightValueLeftBoiler,
                    leftValueRightBoiler = target.leftValueRightBoiler,
                    middleValueRightBoiler = target.middleValueRightBoiler,
                    rightValueRightBoiler = target.rightValueRightBoiler,

                    steamPurgeValve = target.steamPurgeValve,
                    steamPurgeMixValve = target.steamPurgeMixValve,
                    steamWaterFillValve = target.steamWaterFillValve,
                    steamHotWaterValve = target.steamHotWaterValve,
                    steamHotWaterValveMix = target.steamHotWaterValveMix,
                    steamOutSteamValve1 = target.steamOutSteamValve1,
                    steamOutSteamValve2 = target.steamOutSteamValve2,
                    steamEverFoamValve1 = target.steamEverFoamValve1,
                    steamEverFoamValve2 = target.steamEverFoamValve2,
                    waterPump = target.waterPump,
                    airPump = target.airPump,
                    milkFoamer = target.milkFoamer,
                    id = source.id
                ).apply {
                    cups?.current = targetCup
                }
//                _formula.value = new
                repository.updateFormula(new)
                repository.updateFormulaFlag()
                Logger.d(TAG, "setFormulaCups: newFormula = $new")
            }
        }
    }

    fun assimilationProduct(source: Formula?) {
        if (source == null) {
            return
        }
        viewModelScope.launch(defaultDispatcher) {
            val productId = source.productId
            val targetProductId = if (source.showType == SHOW_LEFT_SCREEN) {
                productId + MAIN_SCREEN_PRODUCT_ID_LIMIT
            } else {
                productId - MAIN_SCREEN_PRODUCT_ID_LIMIT
            }
            val target = repository.getFormulaByProductId(targetProductId)
            target?.let {
                it.preFlush = source.preFlush
                it.postFlush = source.postFlush
                it.productType = source.productType
                it.productPrice = source.productPrice
                it.productName = source.productName
                it.beanHopper = source.beanHopper
                it.coffeeWater = source.coffeeWater
                it.powderDosage = source.powderDosage
                it.pressWeight = source.pressWeight
                it.preMakeTime = source.preMakeTime
                it.postPreMakeWaitTime = source.postPreMakeWaitTime
                it.secPressWeight = source.secPressWeight
                it.hotWater = source.hotWater
                it.waterSequence = source.waterSequence
                it.coffeeCycles = source.coffeeCycles
                it.bypassWater = source.bypassWater

                it.manualFoamTime = source.manualFoamTime
                it.autoFoamTemperature = source.autoFoamTemperature
                it.foamMode = source.foamMode
                it.stopAirTime = source.stopAirTime
                it.stopAirTemperature = source.stopAirTemperature
                it.texture = source.texture
                it.mixHotWater = source.mixHotWater
                it.cleanWand = source.cleanWand

                it.appearance = source.appearance
                it.milkDelayTime = source.milkDelayTime
                it.milkOutput = source.milkOutput
                it.milkSequence = source.milkSequence

                it.cups = source.cups
                it.imageRes = source.imageRes
                // TODO check this
                // because left and right value is different, we can't copy valve value
                repository.updateFormula(it)
                repository.updateFormulaFlag()
                Logger.d(TAG, "assimilationProduct() called $it")
            }
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

    fun learnWater() {
        //学习水量使用出热水接口，人工点击开始停止，然后界面显示对应出水量。粉量测试功能也未确定，两个都需要后续再开发
    }

    fun powderTest() {
        //学习水量使用出热水接口，人工点击开始停止，然后界面显示对应出水量。粉量测试功能也未确定，两个都需要后续再开发
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