package com.inno.coffee.ui.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.R
import com.inno.coffee.data.LoginState
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.common.fastclickWithoutRipple
import com.inno.coffee.ui.notice.GlobalDialogLeftManager
import com.inno.coffee.ui.settings.SettingActivity
import com.inno.coffee.viewmodel.home.HomeViewModel
import com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

@Composable
fun MakeCoffeeContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
    val mainScreen = ScreenDisplayManager.isMainDisplay(context)
    val size by if (mainScreen) {
        MakeLeftDrinksHandler.size.collectAsState()
    } else {
        MakeRightDrinksHandler.size.collectAsState()
    }
    val status by if (mainScreen) {
        MakeLeftDrinksHandler.status.collectAsState()
    } else {
        MakeRightDrinksHandler.status.collectAsState()
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            Row(
                modifier = modifier
                    .wrapContentSize(),
            ) {
                Functions(context, viewModel)
                Spacer(modifier = Modifier.weight(1f))
                if (size > 0) {
//                    QueueText(number = size.toString(), second = second, viewModel = viewModel)
                    SingleDrinkText(status = status)
                }
            }
//            DrinkList(modifier = modifier, drinksData = drinksData, enableMask = size > 0,
            DrinkList(modifier = modifier, drinksData = drinksData, enableMask = false,
                viewModel = viewModel) {
                viewModel.startMakeDrink(it, mainScreen, false)
            }
        }
        BottomInfo(modifier = Modifier.align(Alignment.BottomCenter), viewModel = viewModel)
    }
}

@Composable
private fun SingleDrinkText(status: MakeDrinkStatusEnum) {
    val text = when (status) {
        MakeDrinkStatusEnum.LEFT_BREWING, MakeDrinkStatusEnum.RIGHT_BREWING -> {
            stringResource(id = R.string.home_process_status_brewing)
        }
        MakeDrinkStatusEnum.LEFT_BREW_COMPLETED, MakeDrinkStatusEnum.RIGHT_BREW_COMPLETED -> {
            stringResource(id = R.string.home_process_status_brew_completed)
        }
        MakeDrinkStatusEnum.LEFT_FINISHED, MakeDrinkStatusEnum.RIGHT_FINISHED -> {
            stringResource(id = R.string.home_process_status_finished)
        }
        else -> {
            ""
        }
    }
    Text(text = text, style = MaterialTheme.typography.headlineMedium)
}

@Composable
private fun QueueText(number: String, second: Boolean,
    viewModel: HomeViewModel) {

    var showQueueDialog by remember {
        mutableStateOf(false)
    }
    Row {
        Text(
            text = "*队列弹窗",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fastclick { showQueueDialog = true }
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(Color.Red),
        ) {
            Row {
                Text(
                    text = number,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    QueueDialog(showQueueDialog, second, viewModel) {
        showQueueDialog = false
    }
}

@Composable
private fun Functions(
    context: Context,
    viewModel: HomeViewModel
) {
    var showLoginDialog by remember {
        mutableStateOf(false)
    }
    var showCleanDialog by remember {
        mutableStateOf(false)
    }
    var showInfoDialog by remember {
        mutableStateOf(false)
    }
    var showStandByModeDialog by remember {
        mutableStateOf(false)
    }
    Button(
        onClick =
        composeClick {
            ScreenDisplayManager.autoRoute(context, SettingActivity::class.java)
//                    showLoginDialog = true
        }) {
        Text(text = stringResource(id = R.string.home_open_setting))
    }
    Button(
        onClick =
        composeClick {
//                    showCleanDialog = true
        },
    ) {
        Text(text = stringResource(id = R.string.home_clean_screen))
    }
    Button(
        onClick =
        composeClick {
//                    showInfoDialog = true
        },
    ) {
        Text(text = stringResource(id = R.string.home_machine_info))
    }
    Button(
        onClick =
        composeClick {
//                    showStandByModeDialog = true
        },
    ) {
        Text(text = stringResource(id = R.string.home_standby_mode))
    }

    LoginContent(
        context,
        showDialog = showLoginDialog,
        onDismiss = { showLoginDialog = false },
        viewModel = viewModel,
    )
    LockCleanDialog(
        showDialog = showCleanDialog,
        onDismiss = { showCleanDialog = false },
        viewModel = viewModel,
    )
    MachineInfoDialog(showDialog = showInfoDialog) {
        showInfoDialog = false
    }
    StandByModeDialog(
        showDialog = showStandByModeDialog,
        onDismiss = { showStandByModeDialog = false },
    )
}

@Composable
private fun BottomInfo(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.startRecycleTemp()
    }
    val leftTemp = viewModel.leftBoilerTemp.collectAsState()
    val rightTemp = viewModel.rightBoilerTemp.collectAsState()
    val steamTemp = viewModel.steamBoilerTemp.collectAsState()
    val steamPressure = viewModel.steamBoilerPressure.collectAsState()
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 200.dp)
        ) {

            Column {
                Text(
                    text = stringResource(id = R.string.home_left_boiler_temperature,
                        leftTemp.value),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = TextStyle(fontSize = 19.sp),
                )
                Text(
                    text = stringResource(id = R.string.home_right_boiler_temperature,
                        rightTemp.value),
                    style = TextStyle(fontSize = 19.sp),
                )
            }
            Column(
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.home_steam_boiler_temperature,
                        steamTemp.value),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = TextStyle(fontSize = 19.sp),
                )
                Text(
                    text = stringResource(id = R.string.home_steam_boiler_pressure,
                        steamPressure.value),
                    style = TextStyle(fontSize = 19.sp),
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.main_alert_ic),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .align(alignment = Alignment.Center)
                .fastclickWithoutRipple {
                    GlobalDialogLeftManager
                        .getInstance()
                        .showDialog()
                }
        )
    }
}

@Composable
private fun LoginContent(
    context: Context,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (loginState) {
                        is LoginState.Success -> {
                            LaunchedEffect(Unit) {
                                viewModel.resetLoginState()
                                onDismiss()
                                ScreenDisplayManager.autoRoute(context,
                                    SettingActivity::class.java)
                            }
                        }
                        is LoginState.Error -> {
                            val errorMessage = (loginState as LoginState.Error).message
                            Text(text = stringResource(errorMessage))
                        }
                        else -> {}
                    }
                    OutlinedTextField(
                        value = username,
                        onValueChange = viewModel::updateUsername,
                        label = { Text(text = stringResource(id = R.string.home_login_username)) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = viewModel::updatePassword,
                        label = { Text(text = stringResource(id = R.string.home_login_password)) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier =
                        Modifier
                            .align(Alignment.End)
                            .padding(end = 120.dp),
                    ) {
                        Button(
                            onClick = { onDismiss() },
                        ) {
                            Text(text = stringResource(id = R.string.common_button_cancel))
                        }
                        Button(
                            onClick = viewModel::authenticateUser,
                        ) {
                            Text(text = stringResource(id = R.string.home_login_confirm))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LockCleanDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    viewModel: HomeViewModel,
) {
    val state = viewModel.countdown.collectAsState()
    LaunchedEffect(showDialog) {
        if (showDialog) {
            viewModel.startCountDown()
            onDismiss()
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier =
                Modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.home_lock_clean_count_tip),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 30.dp, top = 30.dp),
                    )
                    Text(
                        text = state.value.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    )
                }
            }
        }
    }
}

@Composable
private fun MachineInfoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier =
                Modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column {
                    Text(text = "机器信息")
                    Text(text = "机器信息")
                    Text(text = "机器信息")
                    Button(
                        onClick = { onDismiss() },
                    ) {
                        Text(text = stringResource(id = R.string.common_button_confirm))
                    }
                }
            }
        }
    }
}

@Composable
private fun StandByModeDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier =
                Modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column {
                    Text(text = "确认进去待机模式？")
                    Button(
                        onClick = { onDismiss() },
                    ) {
                        Text(text = stringResource(id = R.string.common_button_confirm))
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewLock() {
    MachineInfoDialog(showDialog = true) {
    }
}
