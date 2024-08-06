package com.inno.coffee.function.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment(context: Context, private val content: @Composable () -> Unit)
    : PresentationFragment(context) {
    @Composable
    override fun InflateCompose() {
        content()
    }
}