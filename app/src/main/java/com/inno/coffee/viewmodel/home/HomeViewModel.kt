package com.inno.coffee.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.R
import com.inno.coffee.data.LoginState
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_CHECK_FINISH
import com.inno.coffee.function.statistic.StatisticManager
import com.inno.coffee.ui.notice.GlobalDialogLeftManager
import com.inno.coffee.ui.notice.GlobalDialogRightManager
import com.inno.coffee.utilities.BREW_BALANCE
import com.inno.coffee.utilities.BREW_PRE_HEATING
import com.inno.coffee.utilities.COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.COLD_RINSE
import com.inno.coffee.utilities.ETC_FRONT
import com.inno.coffee.utilities.ETC_FRONT_REFERENCE
import com.inno.coffee.utilities.ETC_REAR
import com.inno.coffee.utilities.ETC_REAR_REFERENCE
import com.inno.coffee.utilities.GRINDER_LIMIT_CAPACITY_FRONT
import com.inno.coffee.utilities.GRINDER_LIMIT_CAPACITY_REAR
import com.inno.coffee.utilities.GRINDER_PURGE_FUNCTION
import com.inno.coffee.utilities.GRINDING_CAPACITY_FRONT
import com.inno.coffee.utilities.GRINDING_CAPACITY_REAR
import com.inno.coffee.utilities.GROUNDS_QUANTITY
import com.inno.coffee.utilities.HOME_LEFT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.HOME_RIGHT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.LEVELLING
import com.inno.coffee.utilities.LOCK_AND_CLEAN_TIME
import com.inno.coffee.utilities.MACHINE_PRIORITY
import com.inno.coffee.utilities.MILK_RINSE
import com.inno.coffee.utilities.NTC_LEFT
import com.inno.coffee.utilities.NTC_RIGHT
import com.inno.coffee.utilities.NUMBER_OF_CYCLES_RINSE
import com.inno.coffee.utilities.PQC
import com.inno.coffee.utilities.SHOW_LEFT_SCREEN
import com.inno.coffee.utilities.SHOW_RIGHT_SCREEN
import com.inno.coffee.utilities.SINK_RINSE
import com.inno.coffee.utilities.STEAM_BOILER_PRESSURE
import com.inno.coffee.utilities.WARM_RINSE
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.enums.ProductType
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.common.utils.TimeUtils
import com.inno.common.utils.UserSessionManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.BEAN_GRINDER_SETTING
import com.inno.serialport.utilities.MACHINE_PARAM_COMMAND_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
        private const val TEMPERATURE_UNIT = "temperature_unit"
        private const val SHOW_EXTRACTION_TIME = "show_extraction_time"
        private const val BACK_TO_FIRST_PAGE = "back_to_first_page"
        private const val STANDBY_BUTTON = "clean_standby_button"

        private const val FRONT_LIGHT_COLOR = "front_light_color"
    }

    private val _drinkItemList = MutableStateFlow<List<Formula>>(emptyList())
    val drinkItemList: StateFlow<List<Formula>> = _drinkItemList.asStateFlow()
    private val temperatureUnit: Flow<Boolean> =
        dataStore.getCoffeePreferenceFlow(TEMPERATURE_UNIT, false)
    val autoReturnEnabled: Flow<Boolean> =
        dataStore.getCoffeePreferenceFlow(BACK_TO_FIRST_PAGE, false)
    val showExtractionTime: Flow<Boolean> =
        dataStore.getCoffeePreferenceFlow(SHOW_EXTRACTION_TIME, true)
    val showGrinderButton: Flow<Int> = dataStore.getShowGrinderAdjustButtonFlow()
    val showProductPrice: Flow<Boolean> = dataStore.getShowProductPriceFlow()
    val showProductName: Flow<Boolean> = dataStore.getShowProductNameFlow()
    val numberOfProductPerPage: Flow<Int> = dataStore.getNumberOfProductPerPageFlow()
    val standbyButton: Flow<Boolean> = dataStore.getCoffeePreferenceFlow(STANDBY_BUTTON, true)
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _countdown = MutableStateFlow(LOCK_AND_CLEAN_TIME)
    val countdown: StateFlow<Int> = _countdown
    private val _leftBoilerTemp = MutableStateFlow(HOME_LEFT_COFFEE_BOILER_TEMP)
    val leftBoilerTemp = temperatureDisplayFlow(_leftBoilerTemp)
    private val _rightBoilerTemp = MutableStateFlow(HOME_RIGHT_COFFEE_BOILER_TEMP)
    val rightBoilerTemp = temperatureDisplayFlow(_rightBoilerTemp)
    private val _steamBoilerTemp = MutableStateFlow(HOME_LEFT_COFFEE_BOILER_TEMP)
    val steamBoilerTemp = temperatureDisplayFlow(_steamBoilerTemp)
    private val _steamBoilerPressure = MutableStateFlow(0)
    val steamBoilerPressure = _steamBoilerPressure

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()
    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date.asStateFlow()

    private val checking: Boolean
        get() = SelfCheckManager.step.value != STEP_CHECK_FINISH
    val leftExtractionTime = MakeLeftDrinksHandler.extractionTime
    val rightExtractionTime = MakeRightDrinksHandler.extractionTime

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        DataCenter.unsubscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    fun showWarningDialog(main: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            if (main) {
                GlobalDialogLeftManager.getInstance().showDialog()
            } else {
                GlobalDialogRightManager.getInstance().showDialog()
            }
        }
    }

    fun selfCheckWashMachine() {
        viewModelScope.launch {
            SelfCheckManager.waitWashMachine()
        }
    }

    fun selfCheckPutWashPill() {
        viewModelScope.launch {
            SelfCheckManager.putWashPill()
        }
    }

    fun selfCheckContainer() {
        viewModelScope.launch {
            SelfCheckManager.checkCleanContainer(true)
        }
    }

    fun selfCheckSkipWash() {
        viewModelScope.launch {
            SelfCheckManager.skipWashMachine()
        }
    }

    fun selfCheckReleaseSteam() {
        viewModelScope.launch {
            SelfCheckManager.updateReleaseSteam()
            // TODO 1.找不到配方需要提示 2. 不应该使用值
            repository.getFormulaByProductId(81)?.let {
                startMakeDrink(it, true)
            }
            repository.getFormulaByProductId(181)?.let {
                startMakeDrink(it, false)
            }
        }
    }

    fun setScreenDisplay(left: Boolean) {
        Logger.d(TAG, "setScreenDisplay() called with: left = $left")
        val displayScreen = if (left) SHOW_LEFT_SCREEN else SHOW_RIGHT_SCREEN
        viewModelScope.launch {
            repository.getSideFormulas(displayScreen).collect {
                _drinkItemList.value = it
            }
        }
    }

    fun selfCheckIoStatus() {
        viewModelScope.launch {
            DataCenter.init()
            SelfCheckManager.ioStatusCheck()
        }
    }

    fun getCurrentDate() {
        viewModelScope.launch {
            _time.value = TimeUtils.getHourMinuteFormat()
            _date.value = TimeUtils.getYearMonthDayFormat()
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
        _username.value = ""
        _password.value = ""
    }

    fun authenticateGrinder(password: String) {
        Logger.d(TAG, "authenticateGrinder() called with: password = $password")
        viewModelScope.launch(defaultDispatcher) {
            if (!UserSessionManager.isLoggedIn()) {
                if (password.isBlank()) {
                    _loginState.value = LoginState.Error(R.string.permission_valid_empty)
                    return@launch
                }
                val isAuthenticated = repository.authenticateGrinder(password)
                if (!isAuthenticated) {
                    _loginState.value =
                        LoginState.Error(R.string.home_login_authenticate_fail)
                    return@launch
                }
            }
            _loginState.value = LoginState.Success
        }
    }

    fun authenticateUser(password: String) {
        Logger.d(TAG, "authenticateUser() called with: password = $password")
        viewModelScope.launch(defaultDispatcher) {
            if (!UserSessionManager.isLoggedIn()) {
                if (password.isBlank()) {
                    _loginState.value = LoginState.Error(R.string.permission_valid_empty)
                    return@launch
                }
                val isAuthenticated = repository.authenticateUserByPassword(password)
                if (!isAuthenticated) {
                    _loginState.value =
                        LoginState.Error(R.string.home_login_authenticate_fail)
                    return@launch
                }
            }
            _loginState.value = LoginState.Success
        }
    }

    fun cancelMakeDrink() {

    }

    fun startMakeDrink(formula: Formula, main: Boolean) {
        Logger.d(TAG, "startMakeDrink() called with: formula = ${formula.productId}," +
                " main = $main, selfCheck = $checking")
        if (ProductType.isOperationType(formula.productType?.type)) {
            if (checking && ProductType.assertType(formula.productType?.type, ProductType.RINSE)) {
                SelfCheckManager.checkColdRinse()
                viewModelScope.launch {
                    repository.getFormulaByProductId(98)?.let {
                        MakeLeftDrinksHandler.executeNow(it)
                    }
                    // 下发命令
                    delay(200)
                    repository.getFormulaByProductId(198)?.let {
                        MakeRightDrinksHandler.executeNow(it)
                    }
                }
            } else {
                if (main) {
                    MakeLeftDrinksHandler.executeNow(formula)
                } else {
                    MakeRightDrinksHandler.executeNow(formula)
                }
            }
        } else {
            if (main) {
                MakeLeftDrinksHandler.enqueueMessage(formula)
            } else {
                MakeRightDrinksHandler.enqueueMessage(formula)
            }
        }
        StatisticManager.countProductType(formula)
    }

    fun removeQueueDrink(index: Int, formula: Formula, second: Boolean) {
        if (second) {
            MakeRightDrinksHandler.finishAndClear(formula.productId)
        } else {
            MakeLeftDrinksHandler.finishAndClear(formula.productId)
        }
    }

    /**
     * 1. in self check status, only enable rinse.
     * 2. rinse and hot water, only enable stop、 steam and self.
     * 3. steam active, the other steam inactive.
     * 4. when making, disable other product, except stop、steam and self.
     * 5. when not making, enable all product.
     */
    fun enableMask(self: Formula, makingList: List<Formula>): Boolean {
//        Logger.d(TAG, "enableMask() called with: making = ${makingList.size}, " +
//                "self = ${self.productId}")
        val selfType = self.productType?.type
        if (checking) {
            return !ProductType.assertType(selfType, ProductType.RINSE)
        }

        var shouldEnableMask = false
        for (making in makingList) {
//            Logger.d(TAG, "enableMask() called with: making = ${making.productId}")
            val makingType = making.productType?.type

            if (ProductType.assertType(makingType, ProductType.RINSE) ||
                    ProductType.assertType(makingType, ProductType.HOT_WATER)) {
                val stop = ProductType.assertType(selfType, ProductType.STOP)
                val steam = ProductType.assertType(selfType, ProductType.STEAM)
                val foam = ProductType.assertType(selfType, ProductType.FOAM)
                val id = making.productId == self.productId
                Logger.d(TAG, "RINSE() called with: STOP = $stop, STEAM = $steam FOAM = $foam," +
                        " ID = $id ")
                shouldEnableMask = shouldEnableMask || !(stop || steam || foam || id)
            } else if (ProductType.assertType(makingType, ProductType.STEAM) ||
                    ProductType.assertType(makingType, ProductType.FOAM)) {
                val steam = ProductType.assertType(selfType, ProductType.STEAM)
                val foam = ProductType.assertType(selfType, ProductType.FOAM)
                val id = self.productId != making.productId
                Logger.d(TAG, "STEAM() called with: STEAM = $steam, FOAM = $foam, ID = $id")
                shouldEnableMask = shouldEnableMask || (steam && id) || (foam && id)
            } else {
                making.let {
                    val operation = ProductType.isMakingEnableType(selfType)
                    val id = making.productId == self.productId
                    shouldEnableMask = shouldEnableMask || !(operation || id)
                }
            }
        }
        return shouldEnableMask
    }

    fun enableSelect(main: Boolean, self: Formula): Boolean {
        val product: Formula?
        val operation: Formula?
        if (main) {
            product = MakeLeftDrinksHandler.productQueue.value.firstOrNull {
                self.productId == it.productId
            }
            operation = MakeLeftDrinksHandler.operationQueue.value.firstOrNull {
                self.productId == it.productId
            }
        } else {
            product = MakeRightDrinksHandler.productQueue.value.firstOrNull {
                self.productId == it.productId
            }
            operation = MakeRightDrinksHandler.operationQueue.value.firstOrNull {
                self.productId == it.productId
            }
        }
        return product != null || operation != null
    }

    fun cleanWandSteam(main: Boolean) {
        if (!checking) {
            val formula = Formula(productId = 201, preFlush = false, postFlush = false,
                productType = FormulaItem.FormulaProductType(ProductType.STEAM.value),
                cleanWand = 1, steamBoiler = 1, waterPump = 1, waterInputValue = 1,
                steamOutSteamValve1 = 1)
            startMakeDrink(formula, main)
        }
    }

    fun stopMaking(main: Boolean) {
        if (!checking) {
            _drinkItemList.value.firstOrNull {
                ProductType.assertType(it.productType?.type, ProductType.STOP)
            }?.let {
                startMakeDrink(it, main)
            }
        }
    }

    suspend fun startCountDown() {
        while (_countdown.value >= 0) {
            delay(1000)
            _countdown.value--
        }
        _countdown.value = LOCK_AND_CLEAN_TIME
    }

    private fun temperatureDisplayFlow(temperatureFlow: StateFlow<Int>): StateFlow<String> {
        return temperatureUnit.combine(temperatureFlow) { isFahrenheit, tempCelsius ->
            if (isFahrenheit) {
                val fahrenheit = tempCelsius * 1.8 + 32
                "$fahrenheit°F"
            } else {
                "$tempCelsius°C"
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, "")

    }

    fun initPresetParams() {
        viewModelScope.launch(defaultDispatcher) {
            delay(2000)
            val frontColor = dataStore.getCoffeePreference(FRONT_LIGHT_COLOR, 0)

            val boilerTemp = dataStore.getCoffeePreference(COFFEE_BOILER_TEMP, 90f)
            val coldRinseQuantity = dataStore.getCoffeePreference(COLD_RINSE, 500)
            val warmRinseQuantity = dataStore.getCoffeePreference(WARM_RINSE, 100)
            val groundsDrawerQuantity = dataStore.getCoffeePreference(GROUNDS_QUANTITY, 0)
            val brewGroupLoadBalancing = dataStore.getCoffeePreference(BREW_BALANCE, false)
            val brewGroupPreHeating = dataStore.getCoffeePreference(BREW_PRE_HEATING, 0)
            val grinderPurgeFunction = dataStore.getCoffeePreference(GRINDER_PURGE_FUNCTION, 0)
            val numberOfCyclesRinse = dataStore.getCoffeePreference(NUMBER_OF_CYCLES_RINSE, 0)
            val steamBoilerPressure = dataStore.getCoffeePreference(STEAM_BOILER_PRESSURE, 1f)
            val ntcCorrectionSteamLeft = dataStore.getCoffeePreference(NTC_LEFT, 0f)
            val ntcCorrectionSteamRight = dataStore.getCoffeePreference(NTC_RIGHT, 0f)
            val sinkRinse = dataStore.getCoffeePreference(SINK_RINSE, false)
            val milkRinse = dataStore.getCoffeePreference(MILK_RINSE, false)
            val priority = dataStore.getCoffeePreference(MACHINE_PRIORITY, false)

            val levelling = dataStore.getCoffeePreference(LEVELLING, false)
            val pqc = dataStore.getCoffeePreference(PQC, false)
            val grindingCapacityFront =
                dataStore.getCoffeePreference(GRINDING_CAPACITY_FRONT, 1.00f)
            val grindingCapacityRear =
                dataStore.getCoffeePreference(GRINDING_CAPACITY_REAR, 1.00f)
            val etcFront = dataStore.getCoffeePreference(ETC_FRONT, false)
            val etcRear = dataStore.getCoffeePreference(ETC_REAR, false)
            val rearGrinderLimitCapacity =
                dataStore.getCoffeePreference(GRINDER_LIMIT_CAPACITY_REAR, 1.00f)
            val frontGrinderLimitCapacity =
                dataStore.getCoffeePreference(GRINDER_LIMIT_CAPACITY_FRONT, 1.00f)
            val etcFrontReference =
                dataStore.getCoffeePreference(ETC_FRONT_REFERENCE, 0.00f)
            val etcRearReference =
                dataStore.getCoffeePreference(ETC_REAR_REFERENCE, 0.00f)

            val balance = if (brewGroupLoadBalancing) 1 else 0
            val sinkRinseValue = if (sinkRinse) 1 else 0
            val milkRinseValue = if (milkRinse) 1 else 0
            val priorityValue = if (priority) 1 else 0
            Logger.d(TAG, "initMachineParams() called with: boilerTemp = $boilerTemp, " +
                    "coldRinseQuantity = $coldRinseQuantity, " +
                    "warmRinseQuantity = $warmRinseQuantity, " +
                    "groundsDrawerQuantity = $groundsDrawerQuantity, " +
                    "brewGroupLoadBalancing = $brewGroupLoadBalancing, " +
                    "brewGroupPreHeating = $brewGroupPreHeating, " +
                    "grinderPurgeFunction = $grinderPurgeFunction, " +
                    "numberOfCyclesRinse = $numberOfCyclesRinse, " +
                    "steamBoilerPressure = $steamBoilerPressure, " +
                    "ntcCorrectionSteamLeft = $ntcCorrectionSteamLeft, " +
                    "ntcCorrectionSteamRight = $ntcCorrectionSteamRight, " +
                    "skinRinse = $sinkRinse, milkRinse = $milkRinse, priority = $priority"
            )

            val pqcConOffValue = if (pqc) 1 else 0
            val rearCapacityValue = grindingCapacityRear * 100
            val frontCapacityValue = grindingCapacityFront * 100
            val levellingValue = if (levelling) 1 else 0
            val etcFrontValue = if (etcFront) 1 else 0
            val etcRearValue = if (etcRear) 1 else 0
            val rearLimitCapacityValue = rearGrinderLimitCapacity * 100
            val frontLimitCapacityValue = frontGrinderLimitCapacity * 100
            delay(2000)

            CommandControlManager.sendFrontColor(frontColor)

            CommandControlManager.sendTestCommand(MACHINE_PARAM_COMMAND_ID,
                boilerTemp.toInt(), boilerTemp.toInt(),
                coldRinseQuantity, warmRinseQuantity, groundsDrawerQuantity,
                balance, brewGroupPreHeating, grinderPurgeFunction, 0,
                numberOfCyclesRinse, (steamBoilerPressure * 10).toInt(),
                ntcCorrectionSteamLeft.toInt(), ntcCorrectionSteamRight.toInt(), sinkRinseValue,
                milkRinseValue, priorityValue)

            CommandControlManager.sendTestCommand(BEAN_GRINDER_SETTING, pqcConOffValue,
                rearCapacityValue.toInt(), frontCapacityValue.toInt(), levellingValue,
                etcFrontValue,
                etcFrontReference.toInt(), etcRearValue, etcRearReference.toInt(),
                rearLimitCapacityValue.toInt(), frontLimitCapacityValue.toInt(), 0, 0, 0, 0)
        }
    }

    private fun parseReceivedData(data: Any) {
        val boiler = data as ReceivedData.HeartBeat
        boiler.temperature?.let { reply ->
            when (reply.status) {
                BoilerStatusEnum.BOILER_TEMPERATURE -> {
                    _leftBoilerTemp.value = ((reply.value[1].toInt() and 0xFF) shl 8) or
                            (reply.value[0].toInt() and 0xFF)
                    _rightBoilerTemp.value = ((reply.value[3].toInt() and 0xFF) shl 8) or
                            (reply.value[2].toInt() and 0xFF)
                    _steamBoilerTemp.value = ((reply.value[5].toInt() and 0xFF) shl 8) or
                            (reply.value[4].toInt() and 0xFF)
                }
                else -> {}
            }
        }
    }

}
