package com.inno.coffee.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.inno.coffee.utilities.nsp

@Composable
fun TextWithValue(
    description: String = "",
    value: String = "",
    unit: String = "",
    textColor: Color = Color.White,
    textSize: TextUnit = 5.nsp(),
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = description, color = textColor, fontSize = textSize)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = value, color = textColor, fontSize = textSize)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = unit, color = textColor, fontSize = textSize)
    }
}

@Preview
@Composable
private fun PreviewTextWithValue() {
    TextWithValue(description = "右侧咖啡锅炉温度", value = "0.0", unit = "tick/s")
}