package com.inno.coffee.data.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinksViewModel @Inject constructor(
    private val repository: DrinksRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _drinksTypes = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksTypes: StateFlow<List<DrinksModel>> = _drinksTypes.asStateFlow()
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        _drinksTypes.value = repository.drinksType
    }

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun authenticateUser() {
        viewModelScope.launch {
            if (_username.value.isBlank() || _password.value.isBlank()) {
                _loginState.value = LoginState.Error("用户名或密码未输入")
                return@launch
            }
            if (_username.value.length < 3 || _password.value.length < 6) {
                _loginState.value = LoginState.Error("用户名或密码长度不符")
                return@launch
            }
            val isAuthenticated = repository.authenticateUser(_username.value, _password.value)
            if (!isAuthenticated) {
                _loginState.value = LoginState.Error("验证失败")
                return@launch
            }

            _loginState.value = LoginState.Success
        }
    }

}
