package com.inno.coffee.ui.settings.permissions

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.permissions.UserViewModel
import com.inno.common.annotations.MANAGER
import com.inno.common.annotations.OPERATOR
import com.inno.common.annotations.TECHNICIAN
import com.inno.common.utils.UserSessionManager

@Composable
fun PermissionMainLayout(
    onCloseClick: () -> Unit = {},
    viewModel: UserViewModel = hiltViewModel(),
) {
    val names = arrayOf(
        Pair(OPERATOR, R.string.permission_role_operator),
        Pair(MANAGER, R.string.permission_role_manager),
        Pair(TECHNICIAN, R.string.permission_role_technician),
    )
    val user = UserSessionManager.getUser()
    val roleLevel = user?.role ?: TECHNICIAN
    val displayNames = names.take(roleLevel)
    val selectedIndex = remember {
        mutableStateOf(INVALID_INT)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.permission_password_title),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 221.dp)
        ) {
            displayNames.forEach { name ->
                MenuItem(title = name.second) {
                    selectedIndex.value = name.first
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        if (selectedIndex.value != INVALID_INT) {
            PermissionChangePswLayout({
                selectedIndex.value = INVALID_INT
            }, { password ->
                viewModel.updateUserPassword(password, selectedIndex.value)
                selectedIndex.value = INVALID_INT
            })
        }
    }
}

@Composable
private fun MenuItem(
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(73.dp)
            .border(1.dp, Color(0xFF484848), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF191A1D))
            .debouncedClickable({ onClick() })
    ) {
        Text(
            text = stringResource(id = title),
            fontSize = 5.nsp(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewPermissionMain() {
    PermissionMainLayout()
}