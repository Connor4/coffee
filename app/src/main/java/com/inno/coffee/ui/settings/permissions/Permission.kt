package com.inno.coffee.ui.settings.permissions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.R
import com.inno.coffee.data.settings.permissions.UserViewModel
import com.inno.coffee.utilities.DEFAULT_PERMISSION_MODULE
import com.inno.common.db.entity.User

@Composable
fun PermissionPage(modifier: Modifier = Modifier, viewModel: UserViewModel = hiltViewModel()) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var roleValue by remember {
        mutableIntStateOf(0)
    }
    var permissionValue by remember {
        mutableIntStateOf(DEFAULT_PERMISSION_MODULE)
    }
    val context = LocalContext.current
    val userList by viewModel.userList.collectAsStateWithLifecycle()

    Surface(modifier = modifier
        .width(300.dp)
        .fillMaxHeight()) {
        Column {
            Column {
                Row(modifier = Modifier.padding(start = 10.dp, top = 20.dp)) {
                    TextField(value = username, onValueChange = { username = it }, placeholder = {
                        Text(text = "username")
                    }, modifier = Modifier.width(200.dp))
                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(value = password, onValueChange = { password = it }, placeholder = {
                        Text(text = "password")
                    }, modifier = Modifier.width(200.dp))
                    Spacer(modifier = Modifier.width(10.dp))

                }
                RoleCheckBox {
                    roleValue = it
                }
                ModuleCheckBox(permissionValue) {
                    permissionValue = it
                }
                Button(onClick = {
                    viewModel.registerUser(username, password, roleValue, permissionValue)
                }, modifier = Modifier.padding(start = 10.dp)) {
                    Text(text = stringResource(id = R.string.permission_insert_user))
                }

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
                        viewModel.updateUser()
                    }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = stringResource(id = R.string.permission_update_user))
                    }
                    Button(onClick = {
                        viewModel.deleteUser()
                    }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = stringResource(id = R.string.permission_delete_user))
                    }
                }

                Text(text = "当前用户信息")
                Spacer(modifier = Modifier.height(10.dp))
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
        Text(text = "hash", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "${user.roleId}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "${user.permissionId}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
    }
}

@Composable
fun RoleCheckBox(onValueChanged: (Int) -> Unit) {
    val options = listOf(
        stringResource(id = R.string.permission_role_employee),
        stringResource(id = R.string.permission_role_manager),
    )
    var selectedOptionIndex by remember {
        mutableIntStateOf(0)
    }
    options.forEachIndexed { index, option ->
        Row(
            Modifier
                .selectable(
                    selected = (index == selectedOptionIndex),
                    onClick = { onValueChanged(selectedOptionIndex) },
                    role = Role.RadioButton,
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = (index == selectedOptionIndex),
                onClick = {
                    selectedOptionIndex = index
                    onValueChanged(selectedOptionIndex)
                }
            )
            Text(text = option, style = MaterialTheme.typography.bodyLarge, modifier = Modifier
                .padding(start = 6.dp))
        }
    }
}

@Composable
fun ModuleCheckBox(defaultCheckedValue: Int, onValueChanged: (Int) -> Unit) {
    val options = listOf(
        stringResource(id = R.string.common_statistic),
        stringResource(id = R.string.common_formula),
        stringResource(id = R.string.common_display),
        stringResource(id = R.string.common_machine_setting),
        stringResource(id = R.string.common_machine_operation),
        stringResource(id = R.string.common_vat_and_grind),
        stringResource(id = R.string.common_wash_machine),
        stringResource(id = R.string.common_permission),
        stringResource(id = R.string.common_maintenance),
        stringResource(id = R.string.common_serial_test),
    )
    val size = options.size
    val binaryString = defaultCheckedValue.toString(2).padStart(size, '0')
    val checkBoxStates = remember {
        Array(size) { index ->
            mutableStateOf(binaryString[index] == '1')
        }
    }
    var currentCheckedValue by remember {
        mutableIntStateOf(defaultCheckedValue)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        for (i in 0 until size) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkBoxStates[i].value,
                    onCheckedChange = {
                        checkBoxStates[i].value = it
                        currentCheckedValue = checkBoxStatesToInt(checkBoxStates, size)
                        onValueChanged(currentCheckedValue)
                    }
                )
                Text(text = options[i])
            }
        }
    }
}

fun checkBoxStatesToInt(checkBoxStates: Array<MutableState<Boolean>>, size: Int): Int {
    var result = 0
    for (i in checkBoxStates.indices) {
        if (checkBoxStates[i].value) {
            result = result or (1 shl (size - 1 - i))
        }
    }
    return result
}

@Preview
@Composable
fun PreviewPermissionPage() {
    PermissionPage()
}