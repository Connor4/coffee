package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun FunctionHeader(
    text: String = "",
    onCloseClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = text,
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
fun FormulaTimesSelector(
    selectTimes: Int,
    onTimeClick: (value: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
//            .align(Alignment.TopStart)
            .padding(start = 89.dp, top = 188.dp)
    ) {
        repeat(2) {
            val value = it + 1
            TimesItem(text = "$value" + "x", selected = selectTimes == value) {
                onTimeClick(value)
            }
        }
    }
}

@Composable
private fun TimesItem(
    text: String,
    selected: Boolean,
    onTimeClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp)
            .debouncedClickable({ onTimeClick() }),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(
                painter = painterResource(
                    id = R.drawable.setting_statistic_formula_times_selected_bg),
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(
                    id = R.drawable.setting_statistic_formula_times_normal_bg),
                contentDescription = null,
                modifier = Modifier
                    .width(101.dp)
                    .height(84.dp)
            )
        }
        Text(text = text, fontSize = 10.nsp(), color = Color.White)
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFunctionHeader() {
//    FunctionHeader() {}
//    FormulaTimesSelector()
}