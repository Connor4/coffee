package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun MilkSettingPage5() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = stringResource(R.string.config_milk_add_milk), color = Color(0xFF32C5FF),
            fontSize = 6.nsp(), fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 54.dp, top = 20.dp))

        Text(text = stringResource(R.string.config_milk_add_milk_notice), color = Color.White,
            fontSize = 6.nsp(), textAlign = TextAlign.Center, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 320.dp)
                .width(660.dp)
                .wrapContentHeight())

        Image(
            painter = painterResource(id = R.drawable.drink_milk_ic),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(140.dp)
                .offset(y = 130.dp),
        )
    }
}
