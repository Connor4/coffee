package com.inno.coffee.data.bean

sealed class LoginState {
    data object Idle : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}