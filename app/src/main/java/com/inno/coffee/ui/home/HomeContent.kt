package com.inno.coffee.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun HomeContent() {
    Column {
        HomeTopBar()
        HomeDrinksLayout()
        HomeBottomBar() {}
    }
}