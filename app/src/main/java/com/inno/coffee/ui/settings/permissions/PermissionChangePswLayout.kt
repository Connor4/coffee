package com.inno.coffee.ui.settings.permissions

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.KeyboardLayout
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.PERMISSION_MAX_INPUT_SIZE
import com.inno.coffee.utilities.PERMISSION_PASSWORD
import com.inno.coffee.utilities.PERMISSION_PASSWORD_AGAIN
import com.inno.coffee.utilities.nsp

@Composable
fun PermissionChangePswLayout(
    onCloseClick: () -> Unit,
    onChangePsw: (String) -> Unit,
) {
    var select by rememberSaveable {
        mutableIntStateOf(PERMISSION_PASSWORD)
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordStar by rememberSaveable {
        mutableStateOf("")
    }
    var pswVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var passwordAgain by rememberSaveable {
        mutableStateOf("")
    }
    var passwordAgainStar by rememberSaveable {
        mutableStateOf("")
    }
    var pswAgainVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

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
                text = stringResource(id = R.string.permission_change_psw_title),
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
                text = stringResource(id = R.string.permission_enter_password),
                fontSize = 5.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 100.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 136.dp)
                    .debouncedClickable({ select = PERMISSION_PASSWORD }),
                contentAlignment = Alignment.Center
            ) {
                if (select == PERMISSION_PASSWORD) {
                    Box(
                        modifier = Modifier
                            .width(696.dp)
                            .height(52.dp)
                            .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .width(692.dp)
                        .height(48.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Image(
                    painter = if (pswVisible) painterResource(id = R.drawable
                        .permission_invisible_password_ic)
                    else painterResource(id = R.drawable.permission_visible_password_ic),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 60.dp)
                        .size(36.dp)
                        .align(Alignment.CenterEnd)
                        .fastclick { pswVisible = !pswVisible }
                )
                Text(text = if (pswVisible) password else passwordStar,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color(0xFF00DE93),
                    textAlign = TextAlign.Center, maxLines = 1)
            }

            Text(
                text = stringResource(id = R.string.permission_enter_password_again),
                fontSize = 5.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 210.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 246.dp)
                    .debouncedClickable({ select = PERMISSION_PASSWORD_AGAIN }),
                contentAlignment = Alignment.Center
            ) {
                if (select == PERMISSION_PASSWORD_AGAIN) {
                    Box(
                        modifier = Modifier
                            .width(696.dp)
                            .height(52.dp)
                            .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(4.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .width(692.dp)
                        .height(48.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Image(
                    painter = if (pswAgainVisible) painterResource(id = R.drawable
                        .permission_invisible_password_ic)
                    else painterResource(id = R.drawable.permission_visible_password_ic),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 60.dp)
                        .size(36.dp)
                        .align(Alignment.CenterEnd)
                        .fastclick { pswAgainVisible = !pswAgainVisible }
                )
                Text(
                    text = if (pswAgainVisible) passwordAgain else passwordAgainStar,
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
                            PERMISSION_PASSWORD -> {
                                if (password.length < PERMISSION_MAX_INPUT_SIZE) {
                                    password += it
                                    passwordStar += "*"
                                }
                            }
                            PERMISSION_PASSWORD_AGAIN -> {
                                if (passwordAgain.length < PERMISSION_MAX_INPUT_SIZE) {
                                    passwordAgain += it
                                    passwordAgainStar += "*"
                                }
                            }
                        }
                    }, onDelete = {
                        when (select) {
                            PERMISSION_PASSWORD -> {
                                if (password.isNotEmpty()) {
                                    password = password.dropLast(1)
                                    passwordStar = passwordStar.dropLast(1)
                                }
                            }
                            PERMISSION_PASSWORD_AGAIN -> {
                                if (passwordAgain.isNotEmpty()) {
                                    passwordAgain = passwordAgain.dropLast(1)
                                    passwordAgainStar = passwordAgainStar.dropLast(1)
                                }
                            }
                        }
                    }, onEnter = {
                        val valid = validPassword(psw = password, pswAgain = passwordAgain,
                            context = context)
                        if (valid) {
                            onChangePsw(password)
                        }
                    }
                )
            }
        }
    }
}

private fun validPassword(psw: String, pswAgain: String, context: Context): Boolean {
    if (psw.isEmpty() || pswAgain.isEmpty()) {
        val empty = context.resources.getString(R.string.permission_valid_empty)
        Toast.makeText(context, empty, Toast.LENGTH_SHORT).show()
        return false
    }
    if (psw != pswAgain) {
        val same = context.resources.getString(R.string.permission_valid_not_same)
        Toast.makeText(context, same, Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewPermissionChangePsw() {
    PermissionChangePswLayout({}, {})
}