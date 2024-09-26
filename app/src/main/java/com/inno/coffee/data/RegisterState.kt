package com.inno.coffee.data

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Success : RegisterState()
    data object Error : RegisterState()
}