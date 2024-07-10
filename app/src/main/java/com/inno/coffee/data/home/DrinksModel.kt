package com.inno.coffee.data.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class DrinksModel(
    val price: Int,
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int
)

sealed class LoginState {
    data object Idle : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}