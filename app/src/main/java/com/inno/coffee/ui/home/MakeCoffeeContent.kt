package com.inno.coffee.ui.home

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.data.home.DrinksViewModel
import com.inno.coffee.data.home.LoginState
import com.inno.coffee.ui.settings.launchSettingActivity

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
                    launchSettingActivity(context)
//                    showDialog = true
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
                LoginContent(context, showDialog = showDialog, onDismiss = {
                    showDialog = false
                }, viewModel = viewModel)
            }

            val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
            DrinkList(modifier = modifier, drinksData = drinksData)
        }
    }

}

@Composable
fun LoginContent(
    context: Context,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    viewModel: DrinksViewModel,
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
                        label = { Text(text = "用户名") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = password, onValueChange = viewModel::updatePassword,
                        label = { Text(text = "密码") })
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
                    Button(onClick =
                    viewModel::authenticateUser
                    ) {
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
