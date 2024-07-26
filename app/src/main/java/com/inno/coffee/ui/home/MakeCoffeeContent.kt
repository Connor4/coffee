package com.inno.coffee.ui.home

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.R
import com.inno.coffee.data.bean.LoginState
import com.inno.coffee.data.home.HomeViewModel
import com.inno.coffee.ui.presentation.PresentationDisplayManager
import com.inno.coffee.ui.settings.SettingActivity
import com.inno.coffee.utilities.composeClick

@Composable
fun MakeCoffeeContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            Row(
                modifier = modifier.wrapContentHeight(),
            ) {
                val context = LocalContext.current
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
                        val display = (context as Activity).windowManager.defaultDisplay
                        PresentationDisplayManager.autoRoute(context, SettingActivity::class.java,
                            display)
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

            val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
            DrinkList(modifier = modifier, drinksData = drinksData) {
                viewModel.startMakeDrink(it)
            }
        }
        BottomInfo(modifier = Modifier.align(Alignment.BottomCenter), viewModel = viewModel)
    }
}

@Composable
fun BottomInfo(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.startRecycleTemp()
    }
    val leftTemp = viewModel.leftBoilerTemp.collectAsStateWithLifecycle()
    val rightTemp = viewModel.rightBoilerTemp.collectAsStateWithLifecycle()
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Column(
            modifier =
            Modifier
                .align(Alignment.CenterStart)
                .padding(start = 200.dp),
        ) {
            Text(
                text = stringResource(id = R.string.home_left_boiler_temperature, leftTemp.value),
                modifier = Modifier.padding(bottom = 8.dp),
                style = TextStyle(fontSize = 19.sp),
            )
            Text(
                text = stringResource(id = R.string.home_right_boiler_temperature, rightTemp.value),
                style = TextStyle(fontSize = 19.sp),
            )
        }
    }
}

@Composable
fun LoginContent(
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
                                val display = (context as Activity).windowManager.defaultDisplay
                                PresentationDisplayManager.autoRoute(context,
                                    SettingActivity::class.java,
                                    display)
                            }
                        }
                        is LoginState.Error -> {
                            val errorMessage = (loginState as LoginState.Error).message
                            Text(text = errorMessage)
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
fun LockCleanDialog(
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
fun MachineInfoDialog(
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
fun StandByModeDialog(
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
fun PreviewLock() {
    MachineInfoDialog(showDialog = true) {
    }
}
