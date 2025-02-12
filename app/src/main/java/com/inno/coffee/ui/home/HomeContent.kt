package com.inno.coffee.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.LoginState
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_IO_CHECK_START
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_LACK_PILL_START
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_RELEASE_STEAM_READY
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_RELEASE_STEAM_START
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_STEAM_HEATING_END
import com.inno.coffee.function.selfcheck.SelfCheckManager.STEP_WASH_MACHINE_START
import com.inno.coffee.ui.common.ConfirmDialogLayout
import com.inno.coffee.ui.common.SingleInputPasswordLayout
import com.inno.coffee.ui.home.selfcheck.CleanCountdownLayout
import com.inno.coffee.ui.home.selfcheck.ReleaseSteamLayout
import com.inno.coffee.ui.home.selfcheck.SelfCheckLayout
import com.inno.coffee.ui.home.setting.CleanLockLayout
import com.inno.coffee.ui.home.setting.HomeSettingEntrance
import com.inno.coffee.ui.home.setting.MachineInfoLayout
import com.inno.coffee.ui.settings.SettingActivity
import com.inno.coffee.ui.settings.bean.GrinderAdjustmentActivity
import com.inno.coffee.utilities.DISPLAY_NO_GRINDER
import com.inno.coffee.utilities.DISPLAY_YSE_GRINDER
import com.inno.coffee.utilities.HOME_CLEAN
import com.inno.coffee.utilities.HOME_GRINDER_FLAG
import com.inno.coffee.utilities.HOME_INFO
import com.inno.coffee.utilities.HOME_LOGIN
import com.inno.coffee.utilities.HOME_SETTING_FLAG
import com.inno.coffee.utilities.HOME_STANDBY
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.viewmodel.home.HomeViewModel
import com.inno.common.utils.UserSessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val mainScreen = ScreenDisplayManager.isMainDisplay(context)
    var overlayVisible by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(INVALID_INT) }
    var showCleanDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showStandByModeDialog by remember { mutableStateOf(false) }
    var showRinseDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val loginState by viewModel.loginState.collectAsState()
    val leftTemperature by viewModel.leftBoilerTemp.collectAsState()
    val rightTemperature by viewModel.rightBoilerTemp.collectAsState()
    val showExtractionTime by viewModel.showExtractionTime.collectAsState(initial = true)
    val showStandByMode by viewModel.standbyButton.collectAsState(initial = true)
    val showGrinderButton by viewModel.showGrinderButton.collectAsState(initial = 0)
    val extractionTime by if (mainScreen) {
        viewModel.leftExtractionTime.collectAsState()
    } else {
        viewModel.rightExtractionTime.collectAsState()
    }
    val checkStep by SelfCheckManager.step.collectAsState()
    val rightLack by SelfCheckManager.rightLackPill.collectAsState()
    val leftLack by SelfCheckManager.leftLackPill.collectAsState()

    LaunchedEffect(Unit) {
        if (mainScreen) {
            viewModel.selfCheckIoStatus()
            viewModel.initPresetParams()
        }
    }
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                if (showLoginDialog == HOME_SETTING_FLAG) {
                    ScreenDisplayManager.autoRoute(context,
                        SettingActivity::class.java)
                } else if (showLoginDialog == HOME_GRINDER_FLAG) {
                    ScreenDisplayManager.autoRoute(context,
                        GrinderAdjustmentActivity::class.java)
                }
                coroutineScope.launch {
                    delay(1000)
                    showLoginDialog = INVALID_INT
                    overlayVisible = false
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            HomeTopBar(open = overlayVisible, viewModel = viewModel) {
                overlayVisible = it
            }
            HomeDrinksLayout(viewModel = viewModel, onShowRinseDialog = {
                showRinseDialog = true
            })
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            HomeBottomBar(extractionTime = extractionTime, leftTemp = leftTemperature,
                rightTemp = rightTemperature, showExtractionTime = showExtractionTime,
                showGrinderButton = showGrinderButton != DISPLAY_NO_GRINDER,
                onReleaseSteam = {
                    viewModel.cleanWandSteam(mainScreen)
                }, onClickWarning = {
                    viewModel.showWarningDialog(mainScreen)
                }, onClickStop = {
                    viewModel.stopMaking(mainScreen)
                }, onClickGrinder = {
                    if (showGrinderButton == DISPLAY_YSE_GRINDER) {
                        ScreenDisplayManager.autoRoute(context,
                            GrinderAdjustmentActivity::class.java)
                    } else {
                        if (UserSessionManager.isLoggedIn()) {
                            ScreenDisplayManager.autoRoute(context,
                                GrinderAdjustmentActivity::class.java)
                        } else {
                            showLoginDialog = HOME_GRINDER_FLAG
                        }
                    }
                }
            )
        }

        AnimatedVisibility(
            visible = overlayVisible,
            enter = slideInVertically(initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 600, easing = LinearEasing)),
            exit = slideOutVertically(targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 800, easing = LinearEasing)),
        ) {
            HomeSettingEntrance(
                show = overlayVisible,
                showStandByMode = showStandByMode,
                onMenuClick = {
                    when (it) {
                        HOME_LOGIN -> {
                            if (UserSessionManager.isLoggedIn()) {
                                UserSessionManager.increaseLoginCount()
                                ScreenDisplayManager.autoRoute(context,
                                    SettingActivity::class.java
                                )
                                overlayVisible = false
                            } else {
                                showLoginDialog = HOME_SETTING_FLAG
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
                    overlayVisible = false
                }
            )
        }
    }

    when (checkStep) {
        STEP_IO_CHECK_START -> {
            SelfCheckLayout()
        }
        STEP_STEAM_HEATING_END -> {
            ConfirmDialogLayout(
                title = stringResource(id = R.string.home_wash_machine_title),
                description = stringResource(id = R.string.home_wash_machine_content), {
                    viewModel.selfCheckWashMachine()
                }, {}, showCancelButton = false
            )
        }
        STEP_WASH_MACHINE_START -> {
            CleanCountdownLayout()
        }
        STEP_LACK_PILL_START -> {
            val displayText = when {
                leftLack && rightLack -> stringResource(id = R.string.home_lack_wash_pill_content)
                leftLack -> stringResource(id = R.string.home_lack_left_wash_pill_content)
                rightLack -> stringResource(id = R.string.home_lack_right_wash_pill_content)
                else -> stringResource(id = R.string.home_lack_wash_pill_content)
            }
            ConfirmDialogLayout(
                title = stringResource(id = R.string.home_lack_wash_pill_title),
                description = displayText, {
                    viewModel.selfCheckPutWashPill()
                }, {}, showCancelButton = false
            )
        }
        in STEP_RELEASE_STEAM_READY..STEP_RELEASE_STEAM_START -> {
            ReleaseSteamLayout(checkStep = checkStep) {
                viewModel.selfCheckReleaseSteam()
            }
        }
    }

    if (showLoginDialog != INVALID_INT) {
        SingleInputPasswordLayout(
            title = stringResource(R.string.home_login_title),
            tips = stringResource(R.string.permission_enter_password), { password ->
                if (showLoginDialog == HOME_SETTING_FLAG) {
                    viewModel.authenticateUser(password)
                } else if (showLoginDialog == HOME_GRINDER_FLAG) {
                    viewModel.authenticateGrinder(password)
                }
            }, {
                showLoginDialog = INVALID_INT
            }
        )
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
    if (showRinseDialog) {
        ConfirmDialogLayout(
            title = stringResource(R.string.home_item_rinse),
            description = stringResource(R.string.home_confirm_rinse_description),
            onConfirmClick = {
                // TODO rinse
                showRinseDialog = false
            },
            onCloseClick = {
                showRinseDialog = false
            }
        )
    }

}