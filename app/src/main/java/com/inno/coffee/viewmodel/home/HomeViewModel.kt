package com.inno.coffee.viewmodel.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.data.LoginState
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.function.statistic.StatisticManager
import com.inno.coffee.utilities.HOME_LEFT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.HOME_RIGHT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.LOCK_AND_CLEAN_TIME
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.statusenum.BoilerStatusEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _drinksTypes = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksTypes: StateFlow<List<DrinksModel>> = _drinksTypes.asStateFlow()
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
    private val specialItem = MutableStateFlow<List<Int>>(emptyList())
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        _drinksTypes.value = repository.drinksType
        specialItem.value = repository.specialItem
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        DataCenter.unsubscribe(ReceivedDataType.HEARTBEAT, subscriber)
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
                    LoginState.Error(context.getString(R.string.home_login_input_empty))
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
                    LoginState.Error(context.getString(R.string.home_login_authenticate_fail))
                return@launch
            }

            _loginState.value = LoginState.Success
        }
    }

    fun startMakeDrink(model: DrinksModel, second: Boolean) {
        if (isFunctionItem(model)) {
            if (second) {
                MakeRightDrinksHandler.executeNow(model)
            } else {
                MakeLeftDrinksHandler.executeNow(model)
            }
        } else {
            if (second) {
                MakeRightDrinksHandler.enqueueMessage(model)
            } else {
                MakeLeftDrinksHandler.enqueueMessage(model)
            }
        }
        StatisticManager.countProductType(model)
    }

    fun removeQueueDrink(index: Int, model: DrinksModel, second: Boolean) {
        if (second) {
            MakeRightDrinksHandler.discardAndClear(index, model)
        } else {
            MakeLeftDrinksHandler.discardAndClear(index, model)
        }
    }

    fun isFunctionItem(model: DrinksModel): Boolean {
        return specialItem.value.contains(model.productId)
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
        val temp = data as ReceivedData.HeartBeat
        temp.temperature?.let { reply ->
            when (reply.status) {
                BoilerStatusEnum.LEFT_BOILER_TEMPERATURE -> {
                    _leftBoilerTemp.value = reply.value
                }
                BoilerStatusEnum.RIGHT_BOILER_TEMPERATURE -> {
                    _rightBoilerTemp.value = reply.value
                }
                BoilerStatusEnum.STREAM_BOILER_TEMPERATURE -> {
                    _steamBoilerTemp.value = reply.value
                }
                else -> {}
            }
        }
    }

}
