package com.inno.coffee.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.utilities.nsp


@Composable
fun ItemWithImageLayout(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    @StringRes stringRes: Int,
    width: Int = 320,
    height: Int = 180,
    imageSize: Int = 50,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    // 使用 MutableState 管理按压状态
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { press ->
                        // 按压时设置为 true
                        isPressed = true
                        try {
                            tryAwaitRelease() // 等待释放
                        } finally {
                            isPressed = false // 释放时设置为 false
                        }
                    },
                    onTap = {
                        onClick() // 触发点击事件
                    },
                    onLongPress = {
                        onLongClick() // 触发长按事件
                    }
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(width.dp)
                .height(height.dp)
                .border(
                    2.dp,
                    if (isPressed) Color(0xFF00DE93) else Color(0xFF484848), // 根据按压状态设置边框颜色
                    RoundedCornerShape(17.dp)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(color = Color(0xFF191A1D)),
        ) {
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(imageSize.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 41.dp),
            )
            Text(
                text = stringResource(stringRes), color = Color.White, textAlign = TextAlign.Center,
                fontSize = 5.nsp(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = (60 + imageSize).dp),
            )
        }
    }
}