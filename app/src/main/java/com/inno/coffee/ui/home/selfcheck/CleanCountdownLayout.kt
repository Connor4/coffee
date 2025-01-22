package com.inno.coffee.ui.home.selfcheck

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import kotlinx.coroutines.delay

@Composable
fun CleanCountdownLayout() {
    var time by remember { mutableStateOf(0) }
    val formattedTime = remember(time) {
        String.format("%02d:%02d", time / 60, time % 60)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            time++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(text = stringResource(R.string.home_self_check_countdown_text), color = Color.White,
            fontSize = 7.nsp(), modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 186.dp))
        Image(painter = painterResource(R.drawable.home_clean_machine_ic),
            contentDescription = null, modifier = Modifier
                .align(Alignment.Center)
                .size(180.dp))
        Text(text = formattedTime, color = Color.White,
            fontSize = 14.nsp(), fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 575.dp))
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewCleanCountdownLayout() {
    CleanCountdownLayout()
}