package com.inno.coffee.data.home

import androidx.compose.runtime.Immutable

@Immutable
data class DrinksModel(
    val price: Int,
    val name: String,
    val imageRes: Int
)

sealed class LoginState {
    data object Idle : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}