package com.inno.coffee.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import kotlinx.coroutines.delay

@Composable
fun CleanLockLayout(
    count: Int = 10,
) {
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
//            .fillMaxSize()
            .fillMaxWidth()
            .height(800.dp)
            .padding(top = 60.dp, bottom = 70.dp)
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_entrance_dialog_bg),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(834.dp)
                .height(394.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(340.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF191A1D))
        ) {
            Text(
                text = stringResource(id = R.string.home_lock_clean_count_tip),
                fontSize = 7.nsp(), color = Color.White, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 47.dp, top = 37.dp)
            )
            Image(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 141.dp)
                    .size(50.dp)
                    .rotate(rotation.value),
                painter = painterResource(id = R.drawable.home_entrance_countdown_ic),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 229.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = "$count", fontSize = 13.nsp(), color = Color.White,
                    modifier = Modifier.alignByBaseline())
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "s", fontSize = 6.nsp(), color = Color.White,
                    modifier = Modifier.alignByBaseline())
            }

        }
    }

}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewEntrance() {
    CleanLockLayout()
}