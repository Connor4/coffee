package com.inno.coffee.ui.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.draw9Patch
import com.inno.coffee.utilities.nsp

@Composable
fun HomeCleanLayout(
    count: Int = 10,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(340.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF191A1D))
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg)
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
                    .graphicsLayer {
                        rotationZ = rotation
                    },
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
    HomeCleanLayout()
}