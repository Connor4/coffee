package com.inno.coffee.ui.settings.permissions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.R
import com.inno.coffee.data.settings.permissions.UserViewModel
import com.inno.common.db.entity.User
import com.inno.common.utils.UserSessionManager

@Composable
fun PermissionPage(modifier: Modifier = Modifier, viewModel: UserViewModel = hiltViewModel()) {
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
    val userList by viewModel.userList.collectAsStateWithLifecycle()

    Surface(modifier = modifier
        .width(300.dp)
        .fillMaxHeight()) {
        Column {
            Column {
                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Button(onClick = {
                        viewModel.authenticateUser(username, password) {
                            if (it) {
                                Toast.makeText(context,
                                    context.getString(R.string.permission_password_correct),
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context,
                                    context.getString(R.string.permission_password_error), Toast
                                        .LENGTH_SHORT).show()
                            }
                        }
                    }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = stringResource(id = R.string.permission_login_user))
                    }

                    Button(onClick = {
                        viewModel.registerUser(username, password, roleId.toInt(),
                            permissionId.toInt())
                    }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = stringResource(id = R.string.permission_insert_user))
                    }

                    Button(onClick = {
                        val user = UserSessionManager.getUser()
                        user?.let {
                            viewModel.updateUser(user)
                        }
                    }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = stringResource(id = R.string.permission_update_user))
                    }
                    Button(onClick = {
                        val user = UserSessionManager.getUser()
                        user?.let {
                            viewModel.deleteUser(it)
                        }
                    }) {
                        Text(text = stringResource(id = R.string.permission_delete_user))
                    }
                }
                Row(modifier = Modifier.padding(start = 10.dp, top = 20.dp)) {
                    TextField(value = username, onValueChange = { username = it }, placeholder = {
                        Text(text = "username")
                    }, modifier = Modifier.width(200.dp))
                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(value = password, onValueChange = { password = it }, placeholder = {
                        Text(text = "password")
                    }, modifier = Modifier.width(200.dp))
                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(value = roleId, onValueChange = { roleId = it }, placeholder = {
                        Text(text = "roleId")
                    }, modifier = Modifier.width(200.dp))
                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(value = permissionId, onValueChange = { permissionId = it },
                        placeholder = {
                            Text(text = "permissionId")
                        }, modifier = Modifier.width(200.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 10.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    items(userList) {
                        UserInfoItem(user = it)
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfoItem(user: User) {
    Row {
        Text(text = user.username, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = user.passwordHash, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "${user.roleId}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "${user.permissionId}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
    }
}

@Preview
@Composable
fun PreviewPermissionPage() {
}