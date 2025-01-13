package com.inno.coffee.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.inno.common.enums.ProductType

@Immutable
data class DrinksModel(
    val productId: Int,
    var type: ProductType,
    @StringRes val name: Int,
    @DrawableRes val imageRes: Int,
)