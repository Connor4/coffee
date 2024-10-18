package com.inno.coffee.data

import androidx.annotation.StringRes

sealed class LoginState {
    data object Idle : LoginState()
    data object Success : LoginState()
    data class Error(@StringRes val message: Int) : LoginState()
}