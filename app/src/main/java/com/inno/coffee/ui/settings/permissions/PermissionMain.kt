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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.home.LoginState
import com.inno.coffee.data.settings.permissions.UserViewModel

@Composable
fun PermissionEntrance(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    when (loginState) {
        is LoginState.Success -> {
            PermissionPage()
        }
        is LoginState.Error -> {
            viewModel.resetLoginState()
            Toast.makeText(LocalContext.current, (loginState as LoginState.Error).message, Toast
                .LENGTH_SHORT).show()
        }
        else -> {}
    }
    Column(
        modifier = modifier
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

@Preview
@Composable
fun PreviewPermission() {
    PermissionEntrance()
}