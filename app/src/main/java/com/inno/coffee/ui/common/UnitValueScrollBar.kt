package com.inno.coffee.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.theme.mainColor
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.FormulaUnitValue
import com.inno.common.utils.Logger


@Composable
fun UnitValueScrollBar(
    modifier: Modifier = Modifier,
    unitValue: FormulaUnitValue,
    onValueChange: (value: Float) -> Unit,
) {
    val progressBarWidth = 230.dp
    val progressBarHeight = 30.dp
    val defaultProgress = unitValue.value / unitValue.rangeEnd
    var progress by remember {
        mutableFloatStateOf(defaultProgress)
    }
    var currentValue by remember {
        mutableFloatStateOf(unitValue.value.toFloat())
    }

    Row(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(text = "$currentValue", fontSize = 6.nsp(), color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = unitValue.unit, fontSize = 6.nsp(), color = Color.White)
        }
        Spacer(modifier = Modifier.width(30.dp))
        StateImage(
            normalImage = painterResource(id = R.drawable.formula_minus_normal_ic),
            pressedImage = painterResource(id = R.drawable.formula_minus_press_ic),
            modifier = Modifier.size(40.dp)
        ) {
            val newValue = (currentValue - 1).coerceIn(unitValue.rangeStart, unitValue.rangeEnd)
            currentValue = newValue
            progress = currentValue / unitValue.rangeEnd
            Logger.d(
                "new $newValue, progress $progress")
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier
                .width(progressBarWidth)
                .height(55.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(progressBarWidth)
                    .height(progressBarHeight)
                    .background(Color(0xFF191A1D))
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            val newProgress = (progress + dragAmount.x / progressBarWidth
                                .toPx()).coerceIn(0f, 1f)
                            currentValue = Math.round(unitValue.rangeEnd * newProgress) * 10 / 10f
                            progress = newProgress
                            onValueChange(progress)
                            Logger.d(
                                "new $newProgress, current $currentValue")
                        }
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(22.dp)
                        .padding(start = 4.dp, end = 4.dp)
                        .background(mainColor)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(text = "${unitValue.rangeStart}", fontSize = 5.nsp(), color = Color.White)
                Text(text = "${unitValue.rangeEnd}", fontSize = 5.nsp(), color = Color.White)
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        StateImage(
            normalImage = painterResource(id = R.drawable.formula_add_normal_ic),
            pressedImage = painterResource(id = R.drawable.formula_add_press_ic),
            modifier = Modifier.size(40.dp)
        ) {
            val newValue = (currentValue + 1).coerceIn(unitValue.rangeStart, unitValue.rangeEnd)
            currentValue = newValue
            progress = currentValue / unitValue.rangeEnd
            Logger.d(
                "new $newValue, progress $progress")
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewUnitValueScrollBar() {
    UnitValueScrollBar(unitValue = FormulaUnitValue(20, 0f, 1000f, "[tick]")) {}
}