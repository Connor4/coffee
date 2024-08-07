package com.inno.coffee.ui.settings.permissions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R
import com.inno.coffee.data.LoginState
import com.inno.coffee.viewmodel.settings.permissions.UserViewModel
import com.inno.common.utils.Logger

private const val HERE = "here"
private const val THERE = "there"

@Composable
fun PermissionEntrance(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    val navHostController = rememberNavController()
    Logger.d("type: ${loginState.javaClass}")
    LaunchedEffect(key1 = loginState) {
        when (loginState) {
            is LoginState.Success -> {
                Logger.d("Navigating to PermissionPage")
                navHostController.navigate(THERE) {
                    popUpTo(HERE) { inclusive = true }
                }
            }
            is LoginState.Error -> {
                viewModel.resetLoginState()
                Toast.makeText(context, (loginState as LoginState.Error).message, Toast
                    .LENGTH_SHORT).show()
            }
            else -> {
                Logger.d("Navigating to else")
            }
        }
    }

    NavHost(navController = navHostController, startDestination = HERE) {
        composable(HERE) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, top = 50.dp)
            ) {
                Row(
                    modifier = Modifier.wrapContentSize()
                ) {
                    TextField(value = username, onValueChange = { viewModel.updateUsername(it) },
                        placeholder = {
                            Text(text = stringResource(id = R.string.permission_hint_username))
                        })
                    Spacer(modifier = Modifier.width(10.dp))
                    TextField(value = password, onValueChange = { viewModel.updatePassword(it) },
                        placeholder = {
                            Text(text = stringResource(id = R.string.permission_hint_password))
                        })
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = viewModel::authenticateUser) {
                    Text(text = stringResource(id = R.string.permission_login_user))
                }
            }
        }
        composable(THERE) {
            PermissionPage()
        }
    }
}


@Preview
@Composable
private fun PreviewPermission() {
    PermissionEntrance()
}