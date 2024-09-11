package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.composeClick
import com.inno.coffee.utilities.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun StandbyModeLayout(
    onConfirmClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
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
                text = stringResource(id = R.string.home_standby_mode),
                fontSize = 7.nsp(), color = Color.White, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 47.dp, top = 37.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 37.dp, end = 37.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )
            Text(
                text = stringResource(id = R.string.home_entrance_enter_standby),
                fontSize = 6.nsp(), color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 250.dp)
            ) {
                Button(
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp),
                    colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C), Color.Green,
                        Color.Magenta),
                    shape = RoundedCornerShape(10.dp),
                    onClick = composeClick { onConfirmClick() },
                ) {
                    Text(text = stringResource(id = R.string.common_button_confirm),
                        fontSize = 5.nsp(), color = Color.White)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp),
                    colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C), Color.Green,
                        Color.Magenta),
                    shape = RoundedCornerShape(10.dp),
                    onClick = composeClick { onCloseClick() },
                ) {
                    Text(text = stringResource(id = R.string.common_button_cancel),
                        fontSize = 5.nsp(), color = Color.White)
                }
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewStandbyMode() {
    StandbyModeLayout({}, {})
}