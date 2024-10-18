package com.inno.coffee.ui.settings.permissions

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.PERMISSION_KEY_USERNAME
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.permissions.UserViewModel
import com.inno.common.annotations.MANAGER
import com.inno.common.annotations.OPERATOR
import com.inno.common.annotations.TECHNICIAN
import com.inno.common.db.entity.User

@Composable
fun PermissionUserListLayout(
    viewModel: UserViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val userList by viewModel.userList.collectAsState()
//    val userList = mutableListOf<User>()
    val placeHolder = User("", "", TECHNICIAN, 0, "")
    val scrollBarWidth = 14
    val scrollTrackHeight = 480

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.permission_user_list_title),
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
                .padding(start = 50.dp, top = 188.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = stringResource(R.string.permission_main_username_title), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 29.dp)
            )
//            Text(
//                text = stringResource(R.string.permission_main_password_title), color = Color.White,
//                fontSize = 5.nsp(),
//                modifier = Modifier.padding(start = 225.dp)
//            )
            Text(
                text = stringResource(R.string.permission_main_role_title), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 260.dp)
            )
            Text(
                text = stringResource(R.string.permission_main_remarks_title), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 460.dp)
            )
            Text(
                text = stringResource(R.string.permission_user_list_operate_title),
                color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 660.dp)
            )
        }

        Box(
            modifier = Modifier.padding(top = 208.dp)
        ) {
            VerticalScrollList2(list = userList, minimumSize = 9,
                placeHolder = placeHolder, scrollBarWidth = scrollBarWidth,
                scrollTrackHeight = scrollTrackHeight, listPaddingStart = 50, listPaddingTop = 14,
                listPaddingEnd = 95, listItemHeight = 52f) { index, item ->

                val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                UserItem(user = item as User, backgroundColor = color,
                    onModifyClick = {
                        ScreenDisplayManager.autoRoute(context,
                            PermissionEditActivity::class.java,
                            Bundle().apply {
                                putString(PERMISSION_KEY_USERNAME, item.username)
                            }
                        )
                    },
                    onDeleteClick = {
                        viewModel.deleteSelectUser(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun UserItem(
    user: User,
    backgroundColor: Color = Color(0xFF191A1D),
    onModifyClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = backgroundColor),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = user.username, color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 29.dp)
            )
//            Text(
//                text = "user.passwordHash", color = Color.White,
//                fontSize = 5.nsp(),
//                modifier = Modifier.padding(start = 225.dp)
//            )
            Text(
                text = when (user.role) {
                    MANAGER -> stringResource(
                        id = R.string.permission_role_manager)
                    OPERATOR -> stringResource(
                        id = R.string.permission_role_operator)
                    else -> {
                        ""
                    }
                },
                color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 260.dp)
            )
            Text(
                text = user.remark, color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 460.dp)
            )
            if (user.username.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(start = 660.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(6.dp))
                            .debouncedClickable({ onModifyClick() }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(
                            id = R.string.permission_user_list_operate_modify),
                            color = Color(0xFF00DE93), fontSize = 4.nsp())
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .border(2.dp, Color(0xFFE02020), RoundedCornerShape(6.dp))
                            .debouncedClickable({ onDeleteClick() }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(
                            id = R.string.permission_user_list_operate_delete),
                            color = Color(0xFFE02020), fontSize = 4.nsp())
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewPermissionUserList() {
    PermissionUserListLayout()
}