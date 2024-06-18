package com.inno.coffee.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp

@Composable
fun rememberRecompositionCount(): Int {
    var recompositionCount by remember {
        mutableIntStateOf(0)
    }
    DisposableEffect(Unit) {
        recompositionCount++
        onDispose { }
    }
    return recompositionCount
}

@Composable
fun FloatCountComposable() {
    val recompositionCount = rememberRecompositionCount()
    Text(text = "recomposition count: $recompositionCount", fontSize = 20.sp)
}