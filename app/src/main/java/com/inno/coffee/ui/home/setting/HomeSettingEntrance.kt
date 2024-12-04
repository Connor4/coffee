package com.inno.coffee.ui.home.setting

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.debouncedClickableWithoutRipple
import com.inno.coffee.utilities.nsp
import kotlinx.coroutines.launch

private const val LAYOUT_HEIGHT = 448F
private const val ANIMATION_TIME = 375

@Composable
fun HomeSettingEntrance(
    show: Boolean,
    onMenuClick: (index: Int) -> Unit,
    onCloseFinished: () -> Unit,
) {
    val entrance = mapOf(
        Pair(R.drawable.home_entrance_psw_ic, R.string.home_open_setting),
        Pair(R.drawable.home_entrance_clean_ic, R.string.home_clean_screen),
        Pair(R.drawable.home_entrance_standby_ic, R.string.home_standby_mode),
        Pair(R.drawable.home_entrance_information_ic, R.string.home_machine_info),
    )
    val rotation = remember {
        Animatable(if (show) 0f else 180f)
    }
    LaunchedEffect(show) {
        if (show) {
            launch {
                rotation.animateTo(
                    targetValue = 180f,
                    animationSpec = tween(durationMillis = ANIMATION_TIME, delayMillis = 250,
                        easing = LinearEasing)
                )
            }
        } else {
            launch {
                rotation.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = ANIMATION_TIME, easing = LinearEasing)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LAYOUT_HEIGHT.dp)
                .clickable(enabled = false) {}
        ) {
            Image(
                painterResource(id = R.drawable.home_entrance_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .padding(start = 40.dp, top = 110.dp, end = 40.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                entrance.entries.forEachIndexed { index, entry ->
                    EntranceItem(imageRes = entry.key, title = entry.value) {
                        onMenuClick(index)
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.home_entrance_arraw_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 67.dp)
                    .width(37.dp)
                    .height(20.dp)
                    .rotate(-rotation.value)
                    .debouncedClickable({ onCloseFinished() }),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(352.dp)
                .background(Color.Transparent)
                .debouncedClickableWithoutRipple({ onCloseFinished() })
        )
    }
}

@Composable
private fun EntranceItem(
    @DrawableRes imageRes: Int,
    @StringRes title: Int,
    onItemClick: () -> Unit,
) {
    var isPressed by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onItemClick()
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                })
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isPressed) {
            Image(painter = painterResource(id = R.drawable.home_item_select_bg),
                contentDescription = null)
        }

        Box(
            modifier = Modifier
                .width(276.dp)
                .height(174.dp)
                .border(1.dp, Color(0xFF484848), RoundedCornerShape(17.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(color = Color(0xFF191A1D)),
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 41.dp),
            )
            Text(
                text = stringResource(id = title),
                fontSize = 6.nsp(),
                color = Color.White,
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(y = 124.dp)
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewHomeSettingEntrance() {
    HomeSettingEntrance(show = true, onMenuClick = {},
        onCloseFinished = {})
}