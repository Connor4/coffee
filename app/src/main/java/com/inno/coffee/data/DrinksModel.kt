package com.inno.coffee.data

import androidx.compose.runtime.Immutable

@Immutable
data class DrinksModel(
    val price: Int,
    val name: String,
    val imageRes: Int
)