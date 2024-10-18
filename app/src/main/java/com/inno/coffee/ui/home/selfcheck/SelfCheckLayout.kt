package com.inno.coffee.ui.home.selfcheck

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import kotlinx.coroutines.delay

@Composable
fun SelfCheckLayout() {
    val rotation = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        while (true) {
            rotation.animateTo(
                targetValue = 180f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
            delay(300)
            rotation.snapTo(0f)
            delay(200)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF191A1D))
            .clickable(enabled = false) { },
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 300.dp)
                .size(60.dp)
                .rotate(rotation.value),
            painter = painterResource(id = R.drawable.home_entrance_countdown_ic),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 415.dp),
            text = stringResource(id = R.string.home_self_check_in_progress),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 472.dp),
            text = stringResource(id = R.string.home_self_check_time_description),
            fontSize = 6.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewSelfCheck() {
    SelfCheckLayout()
}