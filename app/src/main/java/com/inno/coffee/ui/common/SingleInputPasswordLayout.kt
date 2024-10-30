package com.inno.coffee.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.PERMISSION_MAX_INPUT_SIZE
import com.inno.coffee.utilities.nsp

@Composable
fun SingleInputPasswordLayout(
    title: String = "",
    tips: String = "",
    onEnterClick: (String) -> Unit = {},
    onCloseClick: () -> Unit,
) {
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordStar by rememberSaveable {
        mutableStateOf("")
    }
    var pswVisible by rememberSaveable {
        mutableStateOf(false)
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
                .width(834.dp)
                .height(629.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(575.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold, fontSize = 7.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 43.dp, top = 30.dp)
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
                text = tips,
                fontSize = 5.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 43.dp, top = 92.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 136.dp)
                    .debouncedClickable({ }),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(692.dp)
                        .height(60.dp)
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


            Box(
                modifier = Modifier.padding(top = 226.dp)
            ) {
                KeyboardLayout(
                    onKeyClick = {
                        if (password.length < PERMISSION_MAX_INPUT_SIZE) {
                            password += it
                            passwordStar += "*"
                        }
                    }, onDelete = {
                        if (password.isNotEmpty()) {
                            password = password.dropLast(1)
                            passwordStar = passwordStar.dropLast(1)
                        }
                    }, onEnter = {
                        onEnterClick(password)
                    }
                )
            }
        }
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewSingleInputPassword() {
    SingleInputPasswordLayout() {}
}