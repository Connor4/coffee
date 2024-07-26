package com.inno.coffee.data.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.R
import com.inno.coffee.makedrinks.MakeDrinksHandler
import com.inno.coffee.utilities.HOME_LEFT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.HOME_RIGHT_COFFEE_BOILER_TEMP
import com.inno.coffee.utilities.LOCK_AND_CLEAN_TIME
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

    init {
        _drinksTypes.value = repository.drinksType
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

    fun startMakeDrink(model: DrinksModel) {
        // 1 多次点击需要可排队
        // 2 停止、冲水跟制作饮品不一样，不可以多次点击
        // 3 由于主副屏上的viewmodel独立，只能按照时间戳排队进去给manager处理队列
        // 4 完成制作通知去除队列任务
        MakeDrinksHandler.enqueueMessage(model.price)
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
                } else {
                    _leftBoilerTemp.value = HOME_LEFT_COFFEE_BOILER_TEMP
                    _rightBoilerTemp.value = HOME_RIGHT_COFFEE_BOILER_TEMP
                }
            }
        }
    }
}
