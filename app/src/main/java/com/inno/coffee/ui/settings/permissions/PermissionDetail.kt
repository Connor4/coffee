package com.inno.coffee.ui.settings.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.utilities.DEFAULT_PERMISSION_MODULE
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.viewmodel.settings.permissions.UserViewModel
import com.inno.common.annotation.MANAGER
import com.inno.common.db.entity.User
import com.inno.common.utils.UserSessionManager

@Composable
fun PermissionPage(modifier: Modifier = Modifier, viewModel: UserViewModel = hiltViewModel()) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 20.dp, top = 50.dp, end = 20.dp)
    ) {
        LoginStateText()
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp))
        RegisterUser(viewModel = viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp))
        Spacer(modifier = Modifier.height(20.dp))
        UserList(viewModel = viewModel)
    }
}

@Composable
private fun Update(viewModel: UserViewModel) {
    Row(modifier = Modifier.padding(top = 10.dp)) {
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
}

@Composable
private fun RegisterUser(viewModel: UserViewModel) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var remark by rememberSaveable {
        mutableStateOf("")
    }
    var roleValue by rememberSaveable {
        mutableIntStateOf(0)
    }
    var permissionValue by rememberSaveable {
        mutableIntStateOf(DEFAULT_PERMISSION_MODULE)
    }
    Row(modifier = Modifier.padding(start = 10.dp, top = 20.dp)) {
        TextField(value = username, onValueChange = { username = it }, placeholder = {
            Text(text = stringResource(id = R.string.permission_hint_username))
        }, modifier = Modifier.width(200.dp))
        Spacer(modifier = Modifier.width(10.dp))

        TextField(value = password, onValueChange = { password = it }, placeholder = {
            Text(text = stringResource(id = R.string.permission_hint_password))
        }, modifier = Modifier.width(200.dp))
        Spacer(modifier = Modifier.width(10.dp))

        TextField(value = remark, onValueChange = { remark = it }, placeholder = {
            Text(text = stringResource(id = R.string.permission_hint_remark))
        }, modifier = Modifier.width(200.dp))
    }
    RoleCheckBox {
        roleValue = it + 1
    }
    ModuleCheckBox(permissionValue) {
        permissionValue = it
    }
    Button(onClick = {
        viewModel.registerUser(username, password, roleValue, permissionValue, remark)
    }, modifier = Modifier.padding(start = 10.dp)) {
        Text(text = stringResource(id = R.string.permission_insert_user))
    }
}

@Composable
private fun UserList(viewModel: UserViewModel) {
    val userList by viewModel.userList.collectAsState()
    Text(text = "当前用户列表")
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.padding(start = 30.dp)
    ) {
        Text(text = "用户名", style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(200.dp))
        Text(text = "密码", style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(200.dp))
        Text(text = "账号角色", style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(200.dp))
        Text(text = "账号权限", style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(200.dp))
        Text(text = "说明", style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(200.dp))
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

@Composable
private fun LoginStateText() {
    Row {
        val user = UserSessionManager.getUser()
        val loginState = user != null
        val state = if (loginState) "已登录" else "未登录"
        Text(text = "当前账号状态：$state")
        Spacer(modifier = Modifier.height(10.dp))
        user?.let {
            val role = if (it.role == MANAGER) "Manager" else "employee"
            Text(text = " 登录账号：${it.username} 角色：$role 权限：${it.permission} " +
                    "备注：${it.remark}")
        }
    }
}

@Composable
private fun UserInfoItem(user: User) {
    Row {
        Text(text = user.username, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "密码不显示", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "${user.role}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = "${user.permission}", style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
        Text(text = user.remark, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(200.dp))
    }
}

@Composable
private fun RoleCheckBox(onValueChanged: (Int) -> Unit) {
    val options = listOf(
        stringResource(id = R.string.permission_role_employee),
        stringResource(id = R.string.permission_role_manager),
    )
    var selectedOptionIndex by remember {
        mutableIntStateOf(0)
    }
    options.forEachIndexed { index, option ->
        Row(Modifier
            .selectable(
                selected = (index == selectedOptionIndex),
                onClick = {
                    selectedOptionIndex = index
                    onValueChanged(selectedOptionIndex)
                },
                role = Role.RadioButton,
            )
            .debouncedClickable(onClick = {
                selectedOptionIndex = index
                onValueChanged(selectedOptionIndex)
            })
            .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = (index == selectedOptionIndex), onClick = { })
            Text(text = option, style = MaterialTheme.typography.bodyLarge, modifier = Modifier
                .padding(start = 6.dp))
        }
    }
}

@Composable
private fun ModuleCheckBox(defaultCheckedValue: Int, onValueChanged: (Int) -> Unit) {
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

private fun checkBoxStatesToInt(checkBoxStates: Array<MutableState<Boolean>>, size: Int): Int {
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
private fun PreviewPermissionPage() {
    PermissionPage()
}