package com.inno.coffee.ui.settings.clean

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.SwitchButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun StandbyOnOffTimeLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
    ) {
        Text(
            text = stringResource(id = R.string.clean_standby_time_settings),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )
    }

}

@Composable
private fun StandbyOnOffTimeItem(
    day: String = "Monday",
    isOn: Boolean = false,
    onToggle: (Boolean) -> Unit,
) {
    Row {
        Text(text = day, color = Color.White, fontSize = 6.nsp(),
            modifier = Modifier
                .width(200.dp)
                .height(40.dp)
                .background(Color(0xFF2A2B2D))
                .padding(start = 26.dp, top = 5.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        SwitchButton(isOn) { newState ->
            onToggle(newState)
        }
        Spacer(modifier = Modifier.width(14.dp))

    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewStandbyOnOffTime() {
    StandbyOnOffTimeLayout()
    StandbyOnOffTimeItem() {}
}