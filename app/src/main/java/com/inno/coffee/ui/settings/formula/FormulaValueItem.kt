package com.inno.coffee.ui.settings.formula

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaUnitValue
import kotlin.reflect.KProperty1

@Composable
fun FormulaValueItem(
    placeHolder: Any,
    keys: List<Int>,
    selectFormula: Formula?,
    formulaProperties: Collection<KProperty1<Formula, *>>,
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        UnitValueScrollBar(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 250.dp, end = 90.dp),
            unitValue = FormulaUnitValue(50, 0f, 1000f, "[tick]")) {

        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 331.dp, end = 38.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(543.dp)
                    .height(293.dp),
            ) {
                val valueList = mutableListOf<Any>()
                selectFormula?.let { formula ->
                    keys.forEachIndexed { index, _ ->
                        val property = formulaProperties.elementAt(index).get(formula)
                        valueList.add(property ?: "")
                    }
                }
                VerticalScrollList2(list = valueList, minimumSize = 9,
                    placeHolder = placeHolder, scrollBarWidth = 14,
                    scrollTrackHeight = 300, listPaddingEnd = 48,
                    scrollBarPaddingEnd = 0, listItemHeight = 52f) { index, item ->

                    val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                    FormulaItem(backgroundColor = color, description = keys[index], value = item) {

                    }
                }
            }
        }
    }
}


@Composable
private fun FormulaItem(
    backgroundColor: Color,
    @StringRes description: Int,
    value: Any,
    onClick: () -> Unit = {},
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    val bgColor: Color?
    val textColor: Color?
    if (isPressed) {
        bgColor = Color(0xFF00DE93)
        textColor = Color.Black
    } else {
        bgColor = backgroundColor
        textColor = Color.White
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = bgColor)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    })
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = description), fontSize = 5.nsp(), color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 19.dp)
            )
            if (value is FormulaUnitValue) {
                Text(
                    text = "${value.value}", fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 255.dp)
                )
                Text(
                    text = value.unit, fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 385.dp)
                )
            } else {
                Text(
                    text = "$value", fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 255.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}