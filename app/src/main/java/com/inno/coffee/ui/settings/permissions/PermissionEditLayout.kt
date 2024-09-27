@file:OptIn(ExperimentalLayoutApi::class)

package com.inno.coffee.ui.settings.permissions

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.RegisterState
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.KeyboardLayout
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.DEFAULT_PERMISSION_MODULE
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.PERMISSION_EDIT_PASSWORD
import com.inno.coffee.utilities.PERMISSION_EDIT_REMARKS
import com.inno.coffee.utilities.PERMISSION_EDIT_USERNAME
import com.inno.coffee.utilities.PERMISSION_MAX_INPUT_SIZE
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.permissions.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun PermissionEditLayout(
    modifyUsername: String = "",
    viewModel: UserViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val registerResult by viewModel.registerResult.collectAsState()
    val modifyUser by viewModel.modifyUser.collectAsState()
    var isKeyboardVisible by remember {
        mutableStateOf(false)
    }
    var roleValue by rememberSaveable {
        mutableIntStateOf(0)
    }
    var permissionValue by remember {
        mutableIntStateOf(DEFAULT_PERMISSION_MODULE)
    }
    var keyboardSelectIndex by remember {
        mutableIntStateOf(INVALID_INT)
    }
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordStar by rememberSaveable {
        mutableStateOf("")
    }
    var remarks by rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    val scrollBarWidth = 14
    // 滑动列表可见区域等同于scroll bar track 500dp，不可见区域加起来大概等于150dp。
    // 不可见比例150/650= 0.22, 可见比例0.78*500 = 390
    val scrollBarHeight = 390
    val scrollTrackHeight = 500
    val registerSuccess = stringResource(id = R.string.permission_edit_register_success)
    val registerFailed = stringResource(id = R.string.permission_edit_register_failed)

    LaunchedEffect(key1 = registerResult) {
        when (registerResult) {
            RegisterState.Success -> {
                Toast.makeText(context, registerSuccess, Toast.LENGTH_SHORT).show()
            }
            RegisterState.Error -> {
                Toast.makeText(context, registerFailed, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getUserByUsername(modifyUsername)
    }
    LaunchedEffect(key1 = modifyUser) {
        modifyUser?.let {
            username = it.username
            roleValue = it.role
            remarks = it.remark
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(
                onClick = {
                    isKeyboardVisible = false
                    keyboardSelectIndex = INVALID_INT
                },
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            )
    ) {
        Text(
            text = if (modifyUser == null) stringResource(
                id = R.string.permission_main_register_account)
            else stringResource(id = R.string.permission_user_list_operate_modify),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 54.dp, top = 186.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
                    .verticalScroll(scrollState)
            ) {
                InputLayout(
                    username = username,
                    password = password,
                    passwordStar = passwordStar,
                    remarks = remarks,
                    selectIndex = keyboardSelectIndex,
                    onSelectInput = {
                        isKeyboardVisible = true
                        keyboardSelectIndex = it
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                RoleLayout {
                    roleValue = it + 1
                }
                Spacer(modifier = Modifier.height(40.dp))
                ModuleCheckBox(permissionValue) {
                    permissionValue = it
                }
                Spacer(modifier = Modifier.height(50.dp))
                ChangeColorButton(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                    text = if (modifyUser == null) stringResource(
                        id = R.string.permission_main_register_account)
                    else stringResource(id = R.string.permission_user_list_operate_modify),
                ) {
                    viewModel.registerUser(username, password, roleValue, permissionValue, remarks)
                }
            }

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(scrollTrackHeight.dp)
                    .align(Alignment.TopEnd)
                    .padding(end = 40.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            // 162为列表可滑动距离
                            dragOffset = (dragOffset + dragAmount).coerceIn(0f, 162f)
                            coroutineScope.launch {
                                scrollState.scrollTo(dragOffset.toInt())
                            }
                        }
                    }
                    .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
            ) {

                Box(
                    modifier = Modifier
                        .width(scrollBarWidth.dp)
                        .height(scrollBarHeight.dp)
                        .offset {
                            // 650为列表整个高度
                            val offset = scrollTrackHeight * scrollState.value / 650f
                            IntOffset(0, offset.toInt())
                        }
                        .background(Color(0xFF00DE93), RoundedCornerShape(10.dp))
                )
            }
        }

        if (isKeyboardVisible) {
            Box(
                modifier = Modifier
                    .width(760.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .background(Color.Black)
                    .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(20.dp))
                    .clickable(enabled = false) {}
            ) {
                KeyboardLayout(
                    onKeyClick = {
                        when (keyboardSelectIndex) {
                            PERMISSION_EDIT_USERNAME -> {
                                if (username.length < PERMISSION_MAX_INPUT_SIZE) {
                                    username += it
                                }
                            }
                            PERMISSION_EDIT_PASSWORD -> {
                                if (password.length < PERMISSION_MAX_INPUT_SIZE) {
                                    password += it
                                    passwordStar += "*"
                                }
                            }
                            PERMISSION_EDIT_REMARKS -> {
                                if (remarks.length < PERMISSION_MAX_INPUT_SIZE) {
                                    remarks += it
                                }
                            }
                        }
                    }, onDelete = {
                        when (keyboardSelectIndex) {
                            PERMISSION_EDIT_USERNAME -> {
                                if (username.isNotEmpty()) {
                                    username = username.dropLast(1)
                                }
                            }
                            PERMISSION_EDIT_PASSWORD -> {
                                if (password.isNotEmpty()) {
                                    password = password.dropLast(1)
                                    passwordStar = passwordStar.dropLast(1)
                                }
                            }
                            PERMISSION_EDIT_REMARKS -> {
                                if (remarks.isNotEmpty()) {
                                    remarks = remarks.dropLast(1)
                                }
                            }
                        }
                    }, onEnter = {
                        isKeyboardVisible = false
                        keyboardSelectIndex = INVALID_INT
                    }
                )
            }
        }
    }
}

@Composable
private fun InputLayout(
    username: String = "",
    password: String = "",
    passwordStar: String = "",
    remarks: String = "",
    selectIndex: Int,
    onSelectInput: (index: Int) -> Unit,
) {
    var pswVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Row {
        Column {
            Text(
                text = stringResource(id = R.string.permission_main_username_title),
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 6.nsp()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .debouncedClickable({
                        onSelectInput(PERMISSION_EDIT_USERNAME)
                    }),
                contentAlignment = Alignment.Center
            ) {
                if (selectIndex == PERMISSION_EDIT_USERNAME) {
                    Box(
                        modifier = Modifier
                            .width(320.dp)
                            .height(60.dp)
                            .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(60.dp)
                        .padding(2.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Text(
                    text = username,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color.White,
                    textAlign = TextAlign.Center, maxLines = 1,
                )
            }
        }

        Spacer(modifier = Modifier.width(50.dp))

        Column {
            Text(
                text = stringResource(id = R.string.permission_main_password_title),
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 6.nsp()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .debouncedClickable({
                        onSelectInput(PERMISSION_EDIT_PASSWORD)
                    }),
                contentAlignment = Alignment.Center
            ) {
                if (selectIndex == PERMISSION_EDIT_PASSWORD) {
                    Box(
                        modifier = Modifier
                            .width(320.dp)
                            .height(60.dp)
                            .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(60.dp)
                        .padding(2.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Image(
                    painter = if (pswVisible) painterResource(id = R.drawable
                        .permission_invisible_password_ic)
                    else painterResource(id = R.drawable.permission_visible_password_ic),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(36.dp)
                        .align(Alignment.CenterEnd)
                        .fastclick { pswVisible = !pswVisible }
                )
                Text(
                    text = if (pswVisible) password else passwordStar,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color.White,
                    textAlign = TextAlign.Center, maxLines = 1,
                )
            }
        }

        Spacer(modifier = Modifier.width(50.dp))

        Column {
            Text(
                text = stringResource(id = R.string.permission_main_remarks_title),
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 6.nsp()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .debouncedClickable({
                        onSelectInput(PERMISSION_EDIT_REMARKS)
                    }),
                contentAlignment = Alignment.Center
            ) {
                if (selectIndex == PERMISSION_EDIT_REMARKS) {
                    Box(
                        modifier = Modifier
                            .width(320.dp)
                            .height(60.dp)
                            .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(60.dp)
                        .padding(2.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Text(
                    text = remarks,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color.White,
                    textAlign = TextAlign.Center, maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun RoleLayout(onValueChanged: (Int) -> Unit) {
    val options = listOf(
        stringResource(id = R.string.permission_role_manager),
        stringResource(id = R.string.permission_role_employee),
    )
    var selectedOptionIndex by remember {
        mutableIntStateOf(0)
    }

    Column {
        Text(
            text = stringResource(id = R.string.permission_main_role_title),
            color = Color.White, fontWeight = FontWeight.Bold, fontSize = 6.nsp()
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            options.forEachIndexed { index, option ->
                Row(
                    Modifier
                        .selectable(
                            selected = (index == selectedOptionIndex),
                            onClick = {
                                selectedOptionIndex = index
                                onValueChanged(selectedOptionIndex)
                            },
                            role = Role.RadioButton,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter =
                        if (index == selectedOptionIndex) painterResource(
                            id = R.drawable.permission_role_check_ic)
                        else painterResource(id = R.drawable.permission_role_uncheck_ic),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = option, fontSize = 5.nsp(), color = Color.White)
                }
                Spacer(modifier = Modifier.width(117.dp))
            }
        }
    }
}


@Composable
private fun ModuleCheckBox(defaultCheckedValue: Int, onValueChanged: (Int) -> Unit) {
    val options = listOf(
        stringResource(id = R.string.common_statistic),
        stringResource(id = R.string.common_formula),
        stringResource(id = R.string.common_display),
        stringResource(id = R.string.common_machine_config),
        stringResource(id = R.string.common_machine_params),
        stringResource(id = R.string.common_beans_and_grinder),
        stringResource(id = R.string.common_machine_clean),
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
    Column {
        Text(
            text = stringResource(id = R.string.permission_main_jurisdiction_title),
            color = Color.White, fontWeight = FontWeight.Bold, fontSize = 6.nsp()
        )
        Spacer(modifier = Modifier.height(30.dp))
        FlowRow(
            modifier = Modifier
                .width(1050.dp)
                .wrapContentHeight(),
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            options.forEachIndexed { index, it ->
                CheckItem(check = checkBoxStates[index].value, text = it) {
                    checkBoxStates[index].value = it
                    currentCheckedValue = checkBoxStatesToInt(checkBoxStates, size)
                    onValueChanged(currentCheckedValue)
                }
            }
        }
    }
}

@Composable
private fun CheckItem(
    check: Boolean = false,
    text: String,
    onCheckedChange: (Boolean) -> Unit,
) {
    var isChecked by remember {
        mutableStateOf(check)
    }
    Row(
        modifier = Modifier.width(220.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (isChecked) painterResource(id = R.drawable.permission_module_check_ic)
            else painterResource(id = R.drawable.permission_module_uncheck_ic),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .fastclick {
                    isChecked = !isChecked
                    onCheckedChange(isChecked)
                }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = Color.White, fontSize = 5.nsp(), maxLines = 2)
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

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewPermissionEdit() {
    PermissionEditLayout()
}