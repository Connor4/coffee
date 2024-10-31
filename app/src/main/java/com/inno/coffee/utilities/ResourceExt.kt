package com.inno.coffee.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getImageResId(imageName: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}

@Composable
fun getStringResId(stringName: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(stringName, "string", context.packageName)
}