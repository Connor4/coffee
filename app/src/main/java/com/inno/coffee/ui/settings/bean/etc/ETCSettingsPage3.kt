package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.nsp

@Composable
fun ETCSettingsPage3(
    currentValue: Float = 0f,
    rangeStart: Float = 0f,
    rangeEnd: Float = 0f,
    onValueChange: (Float) -> Unit = {},
) {
    val unit = "[s]"

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.bean_etc_settings_3_extraction_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 70.dp, top = 120.dp)
                .width(269.dp)
                .wrapContentHeight()
        )
        key(currentValue, rangeStart, rangeEnd) {
            UnitValueScrollBar(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.TopEnd)
                    .padding(top = 140.dp, end = 90.dp),
                value = currentValue,
                rangeStart = rangeStart,
                rangeEnd = rangeEnd,
                unit = unit,
            ) { changeValue ->
                onValueChange(changeValue)
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 80.dp, top = 270.dp, end = 80.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            DisplayItemLayout(
                stringResource(R.string.bean_etc_settings_3_extraction_time),
                currentValue.toString(), Color(0xFF2A2B2D), unit) {
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun ETCSetting3Preview() {
    ETCSettingsPage3()
}