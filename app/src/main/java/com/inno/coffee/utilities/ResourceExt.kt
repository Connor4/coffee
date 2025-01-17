package com.inno.coffee.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.inno.coffee.R

@Composable
fun getImageResId(imageName: String): Int {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.drink_item_empty_ic
}

@Composable
fun getStringResId(stringName: String): Int {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(stringName, "string", context.packageName)
    return if (resId != 0) resId else R.string.common_empty_string
}