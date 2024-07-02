package com.inno.coffee.data.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.R
import com.inno.coffee.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
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
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val countdownTime = 15
    private val _drinksTypes = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksTypes: StateFlow<List<DrinksModel>> = _drinksTypes.asStateFlow()
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _countdown = MutableStateFlow(countdownTime)
    val countdown: StateFlow<Int> = _countdown

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
            if (_username.value.length < 3 || _password.value.length < 6) {
                _loginState.value =
                    LoginState.Error(context.getString(R.string.home_login_input_invalid))
                return@launch
            }
            val isAuthenticated = repository.authenticateUser(_username.value, _password.value)
            if (!isAuthenticated) {
                _loginState.value =
                    LoginState.Error(context.getString(R.string.home_login_authenticate_fail))
                return@launch
            }

            _loginState.value = LoginState.Success
        }
    }

    suspend fun countdown() {
        _countdown.value = countdownTime
        while (_countdown.value > 0) {
            delay(1000)
            _countdown.value -= 1
        }
    }

}
