package com.inno.coffee.ui.settings.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.utilities.nsp

@Composable
fun InfoMarginTextWithValue(
    key: String = "",
    value: String = "",
) {
    Box(
        modifier = Modifier
            .width(576.dp)
            .wrapContentHeight()
    ) {
        Text(text = key, color = Color.White, fontSize = 5.nsp(),
            modifier = Modifier.align(Alignment.CenterStart))
        Text(text = value, color = Color.White, fontSize = 5.nsp(),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 347.dp))
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewInfoMarginTextWithValue() {
    InfoMarginTextWithValue("Board Type", "HUMAI")
}