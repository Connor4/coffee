package com.inno.coffee.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.ui.settings.SettingActivity
import com.inno.coffee.viewmodel.home.HomeViewModel

@Composable
fun HomeContent(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var showOverlay by remember { mutableStateOf(false) }
    var hideOverlay by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showCleanDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showStandByModeDialog by remember { mutableStateOf(false) }
    val ioCheck by SelfCheckManager.ioCheck.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.selfCheckIoStatus()
    }

    if (!ioCheck) {
        SelfCheckLayout()
    } else {
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
                    viewModel.showWarningDialog()
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
                                0 -> {
                                    showLoginDialog = true
                                    ScreenDisplayManager.autoRoute(context,
                                        SettingActivity::class.java)
                                }
                                1 -> {
                                    showCleanDialog = true
                                }
                                2 -> {
                                    showStandByModeDialog = true
                                }
                                3 -> {
                                    showInfoDialog = true
                                }
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
            if (showCleanDialog) {
                val state = viewModel.countdown.collectAsState()
                LaunchedEffect(Unit) {
                    if (showCleanDialog) {
                        viewModel.startCountDown()
                        showCleanDialog = false
                    }
                }
                CleanLockLayout(state.value)
            }
            if (showInfoDialog) {
                MachineInfoLayout() {
                    showInfoDialog = false
                }
            }
            if (showStandByModeDialog) {
                StandbyModeLayout(onConfirmClick = {
                    showStandByModeDialog = false
                }, onCloseClick = {
                    showStandByModeDialog = false
                })
            }
        }
    }
}