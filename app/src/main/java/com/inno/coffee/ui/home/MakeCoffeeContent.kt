package com.inno.coffee.ui.home

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.home.HomeViewModel
import com.inno.coffee.data.home.LoginState
import com.inno.coffee.ui.composeClick
import com.inno.coffee.ui.settings.launchSettingActivity

@Composable
fun MakeCoffeeContent(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    Surface(color = Color.Transparent) {
        Column {
            Row {
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
                Button(onClick = {
                    launchSettingActivity(context)
//                    showDialog = true
                }) {
                    Text(text = stringResource(id = R.string.home_open_setting))
                }
                Button(onClick = composeClick {
                    showCleanDialog = true
                }) {
                    Text(text = stringResource(id = R.string.home_clean_screen))
                }
                Button(onClick = composeClick {
                    showInfoDialog = true
                }) {
                    Text(text = stringResource(id = R.string.home_machine_info))
                }
                Button(onClick = composeClick {
                    showStandByModeDialog = true
                }) {
                    Text(text = stringResource(id = R.string.home_standby_mode))
                }

                LoginContent(context, showDialog = showLoginDialog, onDismiss = {
                    showLoginDialog = false
                }, viewModel = viewModel)
                LockCleanDialog(showDialog = showCleanDialog,
                    onDismiss = { showCleanDialog = false },
                    viewModel = viewModel)
                MachineInfoDialog(showDialog = showInfoDialog) {
                    showInfoDialog = false
                }
            }

//            val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
//            DrinkList(modifier = modifier, drinksData = drinksData)
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
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(value = username, onValueChange = viewModel::updateUsername,
                        label = { Text(text = stringResource(id = R.string.home_login_username)) })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = password, onValueChange = viewModel::updatePassword,
                        label = { Text(text = stringResource(id = R.string.home_login_password)) })
                    Spacer(modifier = Modifier.height(16.dp))
                    when (loginState) {
                        is LoginState.Success -> {
                            LaunchedEffect(Unit) {
                                viewModel.resetLoginState()
                                onDismiss()
                                launchSettingActivity(context)
                            }
                        }
                        is LoginState.Error -> {
                            val errorMessage = (loginState as LoginState.Error).message
                            Text(text = errorMessage)
                        }
                        else -> {}
                    }
                    Button(
                        onClick = viewModel::authenticateUser
                    ) {
                        Text(text = stringResource(id = R.string.home_login_confirm))
                    }
                    Button(
                        onClick = { onDismiss() }
                    ) {
                        Text(text = stringResource(id = R.string.common_button_cancel))
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
        }
        onDismiss()
    }
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column {
                    Text(text = stringResource(id = R.string.home_lock_clean_count_tip),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 30.dp, top = 30.dp)
                    )
                    Text(text = state.value.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
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
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Surface(
                modifier = Modifier
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
                        onClick = { onDismiss() }
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
    viewModel: HomeViewModel
) {
    if (showDialog) {

    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewLock() {
    MachineInfoDialog(showDialog = true) {

    }
}
