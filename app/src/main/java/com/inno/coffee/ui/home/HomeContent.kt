package com.inno.coffee.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeContent() {
    var showOverlay by remember { mutableStateOf(false) }
    var hideOverlay by remember { mutableStateOf(false) }

    Box {
        Column {
            HomeTopBar(showOverlay) {
                if (it) {
                    showOverlay = true
                } else {
                    hideOverlay = true
                }
            }
            HomeDrinksLayout()
            HomeBottomBar {

            }
        }
        if (showOverlay) {
            Box(
                modifier = Modifier
                    .padding(top = 60.dp)
            ) {
                HomeSettingEntrance(
                    show = showOverlay xor hideOverlay,
                    onMenuClick = {
                        when (it) {
                            0 -> {}
                            1 -> {}
                            2 -> {}
                            3 -> {}
                        }
                    },
                    onCloseFinished = {
                        if (hideOverlay) {
                            showOverlay = false
                            hideOverlay = false
                        } else {
                            hideOverlay = true
                        }
                    }
                )
            }
        }
    }

}