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
import com.inno.coffee.function.statistic.StatisticManager
import com.inno.coffee.ui.notice.GlobalDialogLeftManager
import com.inno.coffee.ui.notice.GlobalDialogRightManager
import com.inno.coffee.utilities.HOME_LEFT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.HOME_RIGHT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.LOCK_AND_CLEAN_TIME
import com.inno.coffee.utilities.MAIN_SCREEN_PRODUCT_ID_LIMIT
import com.inno.coffee.utilities.MAKE_DRINK_FAIL
import com.inno.coffee.utilities.MAKE_DRINK_FINISH
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.enums.ProductType
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.common.utils.TimeUtils
import com.inno.common.utils.UserSessionManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
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

        private const val COFFEE_BOILER_TEMP = "coffee_boiler_temp"
        private const val COLD_RINSE = "code_rinse"
        private const val WARM_RINSE = "warm_rinse"
        private const val GROUNDS_QUANTITY = "grounds_quantity"
        private const val BREW_BALANCE = "brew_balance"
        private const val BREW_PRE_HEATING = "brew_pre_heating"
        private const val GRINDER_PURGE_FUNCTION = "grinder_purge_function"
        private const val NUMBER_OF_CYCLES_RINSE = "number_of_cycles_rinse"
        private const val STEAM_BOILER_PRESSURE = "steam_boiler_pressure"
        private const val NTC_LEFT = "ntc_left"
        private const val NTC_RIGHT = "ntc_right"
    }

    val formulaList: StateFlow<List<Formula>> = repository.getLeftAllFormulas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
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
    //    private val _selfCheck = MutableStateFlow(false)
//    val selfCheck = _selfCheck.asStateFlow()

    private val checking: Boolean
        get() = SelfCheckManager.checking.value
    val leftExtractionTime = MakeLeftDrinksHandler.extractionTime
    val rightExtractionTime = MakeRightDrinksHandler.extractionTime

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
        // TODO this function will be call twice, because two screen. find a better place
        initMachineParams()
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

    fun selfCheckReleaseSteam() {
        viewModelScope.launch {
            formulaList.value.firstOrNull {
                ProductType.assertType(it.productType?.type, ProductType.STEAM)
            }?.let {
                startMakeDrink(it, true)
                startMakeDrink(it, false)
            } ?: run {
                // TODO notice can not release steam
            }
            SelfCheckManager.updateReleaseSteam()
        }
    }

    fun selfCheckIoStatus() {
        viewModelScope.launch {
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

    fun startMakeDrink(formula: Formula, main: Boolean) {
        Logger.d(TAG, "startMakeDrink() called with: formula = ${formula.productId}," +
                " main = $main, selfCheck = $checking")
        if (ProductType.isOperationType(formula.productType?.type)) {
            if (checking && ProductType.assertType(formula.productType?.type, ProductType.RINSE)) {
                viewModelScope.launch(defaultDispatcher) {
                    val (leftRinse, rightRinse) = repository.getAllFormulas().filter {
                        ProductType.assertType(it.productType?.type, ProductType.RINSE)
                    }.partition { it.productId < MAIN_SCREEN_PRODUCT_ID_LIMIT }
                    SelfCheckManager.startRinse()
                    // TODO null need to notify
                    leftRinse.firstOrNull()?.let {
                        MakeLeftDrinksHandler.executeNow(it) { status ->
                            when (status) {
                                MAKE_DRINK_FAIL -> SelfCheckManager.wakeupRinseFail()
                                MAKE_DRINK_FINISH -> SelfCheckManager.wakeupRinseSuccess()
                            }
                        }
                    }
                    rightRinse.firstOrNull()?.let {
                        MakeRightDrinksHandler.executeNow(it) { status ->
                            when (status) {
                                MAKE_DRINK_FAIL -> SelfCheckManager.wakeupRinseFail()
                                MAKE_DRINK_FINISH -> SelfCheckManager.wakeupRinseSuccess()
                            }
                        }
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
            formulaList.value.firstOrNull {
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

    private fun initMachineParams() {
        viewModelScope.launch(defaultDispatcher) {
            val frontColor = dataStore.getCoffeePreference(FRONT_LIGHT_COLOR, 0)
            CommandControlManager.sendFrontColor(frontColor)

            delay(2000)
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
            val balance = if (brewGroupLoadBalancing) 1 else 0
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
                    "ntcCorrectionSteamRight = $ntcCorrectionSteamRight")

            CommandControlManager.sendTestCommand(MACHINE_PARAM_COMMAND_ID,
                boilerTemp.toInt(), boilerTemp.toInt(),
                coldRinseQuantity, warmRinseQuantity, groundsDrawerQuantity,
                balance, brewGroupPreHeating, grinderPurgeFunction, 0,
                numberOfCyclesRinse, (steamBoilerPressure * 10).toInt(),
                ntcCorrectionSteamLeft.toInt(), ntcCorrectionSteamRight.toInt(), 0,
                0, 0)
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
