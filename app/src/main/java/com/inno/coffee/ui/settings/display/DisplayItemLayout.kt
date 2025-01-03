package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp

@Composable
fun DisplayItemLayout(
    key: String,
    value: String,
    backgroundColor: Color = Color(0xFF191A1D),
    unit: String = "",
    enable: Boolean = true,
    onClick: () -> Unit = {},
) {
    val textColor = if (enable) Color.White else Color(0x4DFFFFFF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = backgroundColor)
                .debouncedClickable({ onClick() }, enabled = enable),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = key, color = textColor,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 29.dp)
            )
            Text(
                text = value, color = textColor,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 420.dp)
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = unit, color = textColor,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 810.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayItem() {
    DisplayItemLayout("Language", "English")
}