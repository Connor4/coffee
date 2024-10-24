package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp
import com.inno.coffee.utilities.previewFormula
import com.inno.common.db.entity.Formula
import com.inno.common.utils.Logger


@Composable
fun FormulaCupSelectorLayout(
    selectedFormula: Formula?,
    onTimeClick: (value: Int) -> Unit
) {
    if (selectedFormula == null) {
        return
    }
    val repeatTimes =
        if (selectedFormula.cups == null) 1 else if (selectedFormula.cups?.double != -1) 2 else 1
    val selectedIndex =
        if (selectedFormula.cups == null) 1 else selectedFormula.cups?.current
    Logger.d("FormulaCupSelectorLayout() called with: selectedFormula = $selectedFormula " +
            "repeatTimes = $repeatTimes selectedIndex = $selectedIndex")

    Box {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 89.dp, top = 188.dp)
        ) {
            repeat(repeatTimes) {
                val value = it + 1
                TimesItem(text = "$value" + "x", selected = value == selectedIndex) {
                    if (selectedIndex == value) {
                        return@TimesItem
                    }
                    onTimeClick(value)
                }
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
private fun PreviewFormulaCupSelector() {
    FormulaCupSelectorLayout(previewFormula, {})
}