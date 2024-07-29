package com.inno.coffee.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class DrinksModel(
    val productId: Int,
    val price: Int,
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int
)