package com.inno.coffee.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.R
import com.inno.coffee.data.LoginState
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.function.statistic.StatisticManager
import com.inno.coffee.ui.notice.GlobalDialogLeftManager
import com.inno.coffee.ui.notice.GlobalDialogRightManager
import com.inno.coffee.utilities.HOME_LEFT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.HOME_RIGHT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.LOCK_AND_CLEAN_TIME
import com.inno.coffee.utilities.PRODUCT_RINSE
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.common.utils.TimeUtils
import com.inno.common.utils.UserSessionManager
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
    private val TAG = "HomeViewModel"
    val formulaList: StateFlow<List<Formula>> = repository.getAllFormulas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _countdown = MutableStateFlow(LOCK_AND_CLEAN_TIME)
    val countdown: StateFlow<Int> = _countdown
    private val _leftBoilerTemp = MutableStateFlow(HOME_LEFT_COFFEE_BOILER_TEMP)
    val leftBoilerTemp = _leftBoilerTemp
    private val _rightBoilerTemp = MutableStateFlow(HOME_RIGHT_COFFEE_BOILER_TEMP)
    val rightBoilerTemp = _rightBoilerTemp
    private val _steamBoilerTemp = MutableStateFlow(HOME_LEFT_COFFEE_BOILER_TEMP)
    val steamBoilerTemp = _steamBoilerTemp
    private val _steamBoilerPressure = MutableStateFlow(0)
    val steamBoilerPressure = _steamBoilerPressure
    private var specialItem = mutableListOf<Int>()

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()
    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date.asStateFlow()
    private val _selfCheck = MutableStateFlow(false)
    val selfCheck = _selfCheck.asStateFlow()

    val autoReturnEnabled: Flow<Boolean> = dataStore.getBackToFirstPageFlow()
    val showExtractionTime: Flow<Boolean> = dataStore.getShowExtractionTimeFlow()

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

    fun selfCheckReleaseSteam() {
        viewModelScope.launch {
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
            val systemLanguage = dataStore.getSystemLanguage()
            _time.value = TimeUtils.getNowTimeInHour()
            _date.value = TimeUtils.getNowDate(systemLanguage)
        }
    }

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
        _username.value = ""
        _password.value = ""
    }

    fun authenticateUser() {
        viewModelScope.launch {
            if (_username.value.isBlank() || _password.value.isBlank()) {
                _loginState.value =
                    LoginState.Error(R.string.home_login_input_empty)
                return@launch
            }
//            if (_username.value.length < 3 || _password.value.length < 6) {
//                _loginState.value =
//                    LoginState.Error(context.getString(R.string.home_login_input_invalid))
//                return@launch
//            }
            val isAuthenticated = repository.authenticateUser(_username.value, _password.value)
            if (!isAuthenticated) {
                _loginState.value =
                    LoginState.Error(R.string.home_login_authenticate_fail)
                return@launch
            }

            _loginState.value = LoginState.Success
        }
    }

    fun authenticateUser(password: String) {
        Logger.d(TAG, "authenticateUser() called with: password = $password")
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                if (!UserSessionManager.isLoggedIn()) {
                    if (password.isBlank()) {
                        _loginState.value = LoginState.Error(R.string.permission_valid_empty)
                        return@withContext
                    }
                    val isAuthenticated = repository.authenticateUserByPassword(password)
                    if (!isAuthenticated) {
                        _loginState.value =
                            LoginState.Error(R.string.home_login_authenticate_fail)
                        return@withContext
                    }
                }
                _loginState.value = LoginState.Success
            }
        }
    }

    fun startMakeDrink(model: Formula, main: Boolean, selfCheck: Boolean) {
        Logger.d(TAG,
            "startMakeDrink() called with: model = $model, main = $main, selfCheck = $selfCheck")
        if (model.productType?.type == ProductType.OPERATION.value
            || model.productType?.type == ProductType.FOAM.value) {
            if (selfCheck && model.productId == PRODUCT_RINSE) {
                viewModelScope.launch {
                    SelfCheckManager.operateRinse()
                }
                return
            }
            if (main) {
                MakeLeftDrinksHandler.executeNow(model)
            } else {
                MakeRightDrinksHandler.executeNow(model)
            }
        } else {
            if (main) {
                MakeLeftDrinksHandler.enqueueMessage(model)
            } else {
                MakeRightDrinksHandler.enqueueMessage(model)
            }
        }
        StatisticManager.countProductType(model)
    }

    fun removeQueueDrink(index: Int, model: Formula, second: Boolean) {
        if (second) {
            MakeRightDrinksHandler.discardAndClear(index, model)
        } else {
            MakeLeftDrinksHandler.discardAndClear(index, model)
        }
    }

    fun enableMask(making: Boolean, checking: Boolean, productId: Int): Boolean {
        Logger.d(TAG,
            "enableMask() called with: making = $making, checking = $checking, productId = $productId")
        if (checking) {
            return productId != PRODUCT_RINSE
        }
        if (making) {
            return !specialItem.contains(productId)
        }
        return false
    }

    suspend fun startCountDown() {
        while (_countdown.value >= 0) {
            delay(1000)
            _countdown.value--
        }
        _countdown.value = LOCK_AND_CLEAN_TIME
    }

    fun startRecycleTemp() {
        viewModelScope.launch {
            while (true) {
                delay(1500)
                if (_leftBoilerTemp.value < 92) {
                    _leftBoilerTemp.value++
                    _rightBoilerTemp.value++
                    _steamBoilerTemp.value++
                } else {
                    _leftBoilerTemp.value = HOME_LEFT_COFFEE_BOILER_TEMP
                    _rightBoilerTemp.value = HOME_RIGHT_COFFEE_BOILER_TEMP
                    _steamBoilerTemp.value = HOME_LEFT_COFFEE_BOILER_TEMP
                }
            }
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
