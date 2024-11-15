package com.inno.coffee.ui.settings.info

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun InfoOnOffText(
    description: String = "",
    on: Boolean = false,
    textColor: Color = Color.White,
) {
    val status = if (on) {
        stringResource(R.string.info_status_on)
    } else {
        stringResource(R.string.info_status_off)
    }
    Row {
        Text(text = description, color = textColor, fontSize = 5.nsp(),
            modifier = Modifier.width(310.dp))
        Text(text = status, color = textColor, fontSize = 5.nsp())
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewInfoOnOffText() {
    InfoOnOffText("Valve Brew Left:", true)
}