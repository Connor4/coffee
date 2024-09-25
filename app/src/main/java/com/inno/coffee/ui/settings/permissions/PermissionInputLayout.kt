package com.inno.coffee.ui.settings.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.KeyboardLayout
import com.inno.coffee.utilities.PERMISSION_MAX_INPUT_SIZE
import com.inno.coffee.utilities.PERMISSION_PASSWORD
import com.inno.coffee.utilities.PERMISSION_USERNAME
import com.inno.coffee.utilities.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun PermissionInputLayout(
    onCloseClick: () -> Unit,
    onEnterClick: () -> Unit,
) {
    var select by rememberSaveable {
        mutableIntStateOf(PERMISSION_USERNAME)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.permission_input_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(824.dp)
                .height(738.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(675.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(
                text = stringResource(id = R.string.permission_title_login),
                fontWeight = FontWeight.Bold, fontSize = 7.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 40.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 20.dp, end = 22.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )

            Text(
                text = stringResource(id = R.string.permission_enter_username),
                fontSize = 5.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 100.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 136.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.permission_text_input_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .width(694.dp)
                        .height(50.dp)
                        .fastclick { select = PERMISSION_USERNAME }
                )
                Text(text = username,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color(0xFF00DE93),
                    textAlign = TextAlign.Center, maxLines = 1)
            }

            Text(
                text = stringResource(id = R.string.permission_enter_password),
                fontSize = 5.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 210.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 246.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.permission_text_input_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .width(694.dp)
                        .height(50.dp)
                        .fastclick { select = PERMISSION_PASSWORD }
                )
                Text(
                    text = passwordStar,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color(0xFF00DE93),
                    textAlign = TextAlign.Center, maxLines = 1,
                )
            }

            Box(
                modifier = Modifier.padding(top = 326.dp)
            ) {
                KeyboardLayout(
                    onKeyClick = {
                        when (select) {
                            PERMISSION_USERNAME -> {
                                if (username.length < PERMISSION_MAX_INPUT_SIZE) {
                                    username += it
                                }
                            }
                            PERMISSION_PASSWORD -> {
                                if (password.length < PERMISSION_MAX_INPUT_SIZE) {
                                    password += it
                                    passwordStar += "*"
                                }
                            }
                        }
                    }, onDelete = {
                        when (select) {
                            PERMISSION_USERNAME -> {
                                if (username.isNotEmpty()) {
                                    username = username.dropLast(1)
                                }
                            }
                            PERMISSION_PASSWORD -> {
                                if (password.isNotEmpty()) {
                                    password = password.dropLast(1)
                                    passwordStar = passwordStar.dropLast(1)
                                }
                            }
                        }
                    }, onEnter = {

                    }
                )
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewPermissionInput() {
    PermissionInputLayout({}, {})
}