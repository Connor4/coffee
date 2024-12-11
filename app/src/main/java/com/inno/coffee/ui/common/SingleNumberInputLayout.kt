package com.inno.coffee.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.getImageResId
import com.inno.coffee.utilities.nsp


@Composable
fun SingleNumberInputLayout(
    defaultInput: String = "",
    title: String = "",
    tips: String = "",
    maxInputLimitSize: Int = 3,
    onEnterClick: (Float) -> Unit = {},
    onCloseClick: () -> Unit,
) {
    var input by rememberSaveable {
        mutableStateOf(defaultInput)
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
                        .height(52.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Text(text = input,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color(0xFF00DE93),
                    textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(692.dp)
                        .wrapContentHeight())
            }


            Box(
                modifier = Modifier.padding(top = 226.dp)
            ) {
                NumberLayout(onKeyClick = {
                    if (input.length < maxInputLimitSize) {
                        input += it
                    }
                }, onDot = {
                    if (input.length < maxInputLimitSize) {
                        input += "."
                    }
                }, onDelete = {
                    if (input.isNotEmpty()) {
                        input = input.dropLast(1)
                    }
                }, onEnter = {
                    onEnterClick(input.toFloat())
                })
            }
        }
    }
}

@Composable
private fun NumberLayout(
    onKeyClick: (keyName: String) -> Unit,
    onDot: () -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
    ) {
        KeyboardRow1(listOf("1", "2", "3")) {
            onKeyClick(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow1(listOf("4", "5", "6")) {
            onKeyClick(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow1(listOf("7", "8", "9")) {
            onKeyClick(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow3(listOf("0", "o", "delete"), {
            onKeyClick(it)
        }, {
            onDot()
        }, {
            onDelete()
        })
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow4(listOf("enter"), {
            onEnter()
        })
    }
}

@Composable
private fun KeyboardRow1(keys: List<String>, onKeyClick: (keyName: String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        for (key in keys) {
            KeyboardKey(key) {
                onKeyClick(it)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun KeyboardRow3(
    keys: List<String>,
    onKeyClick: (keyName: String) -> Unit,
    onDot: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        KeyboardKey(keys[0]) {
            onKeyClick(it)
        }
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKey(keys[1]) {
            onDot()
        }
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKey(keys[2], width = 95) {
            onDelete()
        }
    }
}


@Composable
private fun KeyboardRow4(keys: List<String>, onEnter: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKeyPressWithDelay(keys[0], width = 165) {
            onEnter()
        }
    }
}

@Composable
private fun KeyboardKeyPressWithDelay(
    keyName: String, width: Int = 60, height: Int = 55,
    onClick: (keyName: String) -> Unit,
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    var lastClickTime by mutableStateOf(0L)
    val imageResId = if (isPressed) "keyboard_${keyName}_press_ic"
    else "keyboard_${keyName}_normal_ic"
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > 3000) {
                        lastClickTime = currentTime
                        onClick(keyName)
                    }
                })
            },
    ) {
        val resId = getImageResId(imageResId)
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun KeyboardKey(
    keyName: String, width: Int = 60, height: Int = 55,
    onClick: (keyName: String) -> Unit,
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    val imageResId = if (isPressed) "keyboard_${keyName}_press_ic"
    else "keyboard_${keyName}_normal_ic"
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                    onClick(keyName)
                })
            },
    ) {
        val resId = getImageResId(imageResId)
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewNumberInputLayout() {
    SingleNumberInputLayout("1.92", "title", "tips", 3, {}, {})
//    NumberLayout({}, {}, {})
}