package com.inno.coffee.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.data.home.DrinksViewModel
import com.inno.coffee.data.home.LoginState

@Composable
fun MakeCoffeeContent(modifier: Modifier = Modifier, viewModel: DrinksViewModel = hiltViewModel()) {
    Surface(color = Color.Transparent) {
        Column {
            Row {
                val context = LocalContext.current
                var showDialog by remember {
                    mutableStateOf(false)
                }

                Button(onClick = {
//                    launchSettingActivity(context)
                    showDialog = true
                }) {
                    Text(text = "打开设置")
                }
                Button(onClick = {

                }) {
                    Text(text = "锁屏擦拭")
                }
                Button(onClick = {

                }) {
                    Text(text = "机器信息")
                }

                val username by viewModel.username.collectAsState()
                val password by viewModel.password.collectAsState()
                val loginState by viewModel.loginState.collectAsState()
                when (loginState) {
                    is LoginState.Success -> {
                        LaunchedEffect(Unit) {
                            launchMakeCoffeeActivity(context)
                        }
                    }
                    is LoginState.Error -> {
                        val errorMessage = (loginState as LoginState.Error).message
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoginContent(showDialog = showDialog, username = username,
                                password = password,
                                onUsernameChange = viewModel::updateUsername,
                                onPasswordChange = viewModel::updatePassword,
                                onLoginClick = { viewModel::authenticateUser },
                                onDismiss = { showDialog = false })
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = errorMessage, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    else -> {
                        LoginContent(showDialog = showDialog, username = username,
                            password = password,
                            onUsernameChange = viewModel::updateUsername,
                            onPasswordChange = viewModel::updatePassword,
                            onLoginClick = { viewModel::authenticateUser },
                            onDismiss = { showDialog = false },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp))
                    }
                }
            }

            val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
            DrinkList(modifier = modifier, drinksData = drinksData)
        }
    }

}

@Composable
fun LoginContent(
    showDialog: Boolean,
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDialog) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Surface(
                modifier = modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
//                        Text(text = "账号", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = username, onValueChange = onUsernameChange,
                        label = { Text(text = "用户名") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = password, onValueChange = onPasswordChange,
                        label = { Text(text = "密码") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        onLoginClick()
                    }) {
                        Text(text = "登录")
                    }
                    Button(onClick = {
                        onDismiss()
                    }) {
                        Text(text = "取消")
                    }
                }
            }
        }
    }
}

@Composable
fun LoginDialog(showDialog: Boolean, viewModel: DrinksViewModel, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier
                    .width(700.dp)
                    .height(500.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                val username by viewModel.username.collectAsState()
                val password by viewModel.password.collectAsState()
                val validState by viewModel.loginState.collectAsState()
                Column {
                    Row {
                        Text(text = "账号", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(value = username, onValueChange = {
                            viewModel.updateUsername(it)
                        },
                            modifier = Modifier.width(200.dp))
                    }
                    Row {
                        Text(text = "密码", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(value = password, onValueChange = {
                            viewModel.updatePassword(it)
                        },
                            modifier = Modifier.width(200.dp),
                            visualTransformation = PasswordVisualTransformation())
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            viewModel.authenticateUser()
                        }) {
                            Text(text = "确认")
                        }
                        Button(onClick = { onDismiss() }) {
                            Text(text = "取消")
                        }
                    }
                }
            }
        }
    }

}
