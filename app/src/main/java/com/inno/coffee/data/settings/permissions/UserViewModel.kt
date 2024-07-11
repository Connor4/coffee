package com.inno.coffee.data.settings.permissions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.R
import com.inno.coffee.data.home.LoginState
import com.inno.common.db.entity.User
import com.inno.common.utils.Logger
import com.inno.common.utils.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _registerResult = MutableStateFlow<Boolean?>(false)
    val registerResult: StateFlow<Boolean?> = _registerResult
    private val _authenticateResult = MutableStateFlow<Boolean?>(false)
    val authenticateResult = _authenticateResult
    private val _updateResult = MutableStateFlow<Boolean?>(false)
    val updateResult = _updateResult
    private val _deleteResult = MutableStateFlow<Boolean?>(false)
    val deleteResult = _deleteResult

    val userList: StateFlow<List<User>> = repository.getAllUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun registerUser(username: String, password: String, role: Int, permission: Int,
        remark: String = "-") {
        viewModelScope.launch {
            val register = repository.registerUser(username, password, role, permission, remark)
            _registerResult.value = register
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun authenticateUser() {
        viewModelScope.launch {
            Logger.d("name ${_username.value}, password ${_password.value}")
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

    fun updateUser() {
        viewModelScope.launch {
            val user = UserSessionManager.getUser()
            user?.let {
                val update = repository.updateUser(it)
                _updateResult.value = update
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            val user = UserSessionManager.getUser()
            user?.let {
                val deleteUser = repository.deleteUser(user)
                _deleteResult.value = deleteUser
            }
        }
    }

}