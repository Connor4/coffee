package com.inno.coffee.ui.settings.permissions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.data.settings.permissions.UserViewModel

@Composable
fun PermissionPage(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var roleId by remember {
        mutableStateOf("")
    }
    var permissionId by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Surface {
        Row {
            Column {
                TextField(value = username, onValueChange = { username = it }, placeholder = {
                    Text(text = "username")
                },
                    modifier = Modifier.width(200.dp))
                Spacer(modifier = Modifier.width(10.dp))

                TextField(value = password, onValueChange = { password = it }, placeholder = {
                    Text(text = "password")
                },
                    modifier = Modifier.width(200.dp))
                Spacer(modifier = Modifier.width(10.dp))

                TextField(value = roleId, onValueChange = { roleId = it }, placeholder = {
                    Text(text = "roleId")
                },
                    modifier = Modifier.width(200.dp))
                Spacer(modifier = Modifier.width(10.dp))

                TextField(value = permissionId, onValueChange = { permissionId = it },
                    placeholder = {
                        Text(text = "permissionId")
                    },
                    modifier = Modifier.width(200.dp))
                Spacer(modifier = Modifier.width(10.dp))
            }
            Column {
                Button(onClick = {
                    viewModel.authenticateUser(username, password) {
                        if (it) {
                            Toast.makeText(context, "密码正确", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text(text = "登录")
                }

                Button(onClick = {
                    viewModel.registerUser(username, password, roleId.toInt(),
                        permissionId.toLong())
                }) {
                    Text(text = "新增")
                }

                Button(onClick = {

                }) {
                    Text(text = "更新")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPermissionPage() {
    PermissionPage()
}