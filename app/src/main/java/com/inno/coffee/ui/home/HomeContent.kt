package com.inno.coffee.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.LoginState
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.ui.common.ConfirmDialogLayout
import com.inno.coffee.ui.common.SingleInputPasswordLayout
import com.inno.coffee.ui.home.selfcheck.SelfCheckLayout
import com.inno.coffee.ui.home.setting.CleanLockLayout
import com.inno.coffee.ui.home.setting.HomeSettingEntrance
import com.inno.coffee.ui.home.setting.MachineInfoLayout
import com.inno.coffee.ui.settings.SettingActivity
import com.inno.coffee.utilities.HOME_CLEAN
import com.inno.coffee.utilities.HOME_INFO
import com.inno.coffee.utilities.HOME_LOGIN
import com.inno.coffee.utilities.HOME_STANDBY
import com.inno.coffee.viewmodel.home.HomeViewModel
import com.inno.common.utils.UserSessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    val ioCheck by SelfCheckManager.ioCheck.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val leftTemperature = viewModel.leftBoilerTemp.collectAsState()
    val rightTemperature = viewModel.rightBoilerTemp.collectAsState()
    val showExtractionTime = viewModel.showExtractionTime.collectAsState(initial = true)
    val mainScreen = ScreenDisplayManager.isMainDisplay(context)

    LaunchedEffect(Unit) {
        viewModel.selfCheckIoStatus()
    }
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                ScreenDisplayManager.autoRoute(context,
                    SettingActivity::class.java
                )
                coroutineScope.launch {
                    delay(1000)
                    showLoginDialog = false
                }
            }
            is LoginState.Error -> {
                val errorMessage = (loginState as LoginState.Error).message
                Toast.makeText(context, context.resources.getString(errorMessage),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
        viewModel.resetLoginState()
    }

    if (!ioCheck) {
        SelfCheckLayout()
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                HomeTopBar(open = showOverlay, viewModel = viewModel) {
                    if (it) {
                        showOverlay = true
                    } else {
                        hideOverlay = true
                    }
                }
                HomeDrinksLayout(viewModel = viewModel)
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                HomeBottomBar(leftTemp = leftTemperature.value, rightTemp = rightTemperature.value,
                    showExtractionTime = showExtractionTime.value
                ) {
                    viewModel.showWarningDialog(mainScreen)
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
                                HOME_LOGIN -> {
                                    if (UserSessionManager.isLoggedIn()) {
                                        UserSessionManager.increaseLoginCount()
                                        ScreenDisplayManager.autoRoute(context,
                                            SettingActivity::class.java
                                        )
                                    } else {
                                        showLoginDialog = true
                                    }
                                }
                                HOME_CLEAN -> {
                                    showCleanDialog = true
                                }
                                HOME_STANDBY -> {
                                    showStandByModeDialog = true
                                }
                                HOME_INFO -> {
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
            if (showLoginDialog) {
                SingleInputPasswordLayout(
                    title = stringResource(R.string.home_login_title),
                    tips = stringResource(R.string.permission_enter_password), { password ->
                        viewModel.authenticateUser(password)
                    }, {
                        showLoginDialog = false
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
            ConfirmDialogLayout(title = stringResource(id = R.string.home_standby_mode),
                description = stringResource(id = R.string.home_entrance_enter_standby),
                onConfirmClick = {
                    showStandByModeDialog = false
                }, onCloseClick = {
                    showStandByModeDialog = false
                })
        }
    }
}