package com.inno.coffee.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.theme.mainColor
import com.inno.coffee.utilities.nsp
import kotlin.math.roundToInt

const val ACCURACY_1 = 1
const val ACCURACY_2 = 2
const val ACCURACY_3 = 3

@Composable
fun UnitValueScrollBar(
    modifier: Modifier = Modifier,
    value: Float = 0f,
    rangeStart: Float = 0f,
    rangeEnd: Float = 0f,
    unit: String = "",
    accuracy: Int = ACCURACY_1,
    onValueChange: (newValue: Float) -> Unit,
) {
    val progressBarWidth = 230.dp
    val progressBarHeight = 30.dp
    val defaultProgress = (value - rangeStart) / (rangeEnd - rangeStart)
    var progress by remember {
        mutableFloatStateOf(defaultProgress)
    }
    var currentValue by remember {
        mutableFloatStateOf(value)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 381.dp)
        ) {
            Text(text = "$currentValue", fontSize = 6.nsp(), color = Color.White)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = unit, fontSize = 6.nsp(), color = Color.White)
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd),
        ) {
            StateImage(
                normalImage = painterResource(id = R.drawable.formula_minus_normal_ic),
                pressedImage = painterResource(id = R.drawable.formula_minus_press_ic),
                modifier = Modifier.size(40.dp)
            ) {
                currentValue = when (accuracy) {
                    ACCURACY_1 -> {
                        currentValue.minus(1).coerceIn(rangeStart, rangeEnd)
                    }
                    ACCURACY_2 -> {
                        (((currentValue - 0.1f) * 10).roundToInt() / 10f).coerceIn(rangeStart,
                            rangeEnd)
                    }
                    ACCURACY_3 -> {
                        (((currentValue - 0.01f) * 100).roundToInt() / 100f).coerceIn(rangeStart,
                            rangeEnd)
                    }
                    else -> {
                        currentValue.minus(1).coerceIn(rangeStart, rangeEnd)
                    }
                }
                progress = (currentValue - rangeStart) / (rangeEnd - rangeStart)
                onValueChange(currentValue)
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
                            awaitEachGesture {
                                val downEvent = awaitFirstDown()
                                offset = downEvent.position
                                progress = (offset.x / progressBarWidth.toPx()).coerceIn(0f, 1f)
                                currentValue = when (accuracy) {
                                    ACCURACY_1 -> {
                                        (rangeStart + (rangeEnd - rangeStart) * progress)
                                            .roundToInt()
                                            .toFloat()
                                    }
                                    ACCURACY_2 -> {
                                        ((rangeStart + (rangeEnd - rangeStart) * progress) *
                                                10).roundToInt() / 10f
                                    }
                                    ACCURACY_3 -> {
                                        ((rangeStart + (rangeEnd - rangeStart) * progress) *
                                                100).roundToInt() / 100f
                                    }
                                    else -> {
                                        (rangeStart + (rangeEnd - rangeStart) * progress)
                                            .roundToInt()
                                            .toFloat()
                                    }
                                }
                                onValueChange(currentValue)
                                drag(downEvent.id) {
                                    offset += it.positionChange()
                                    progress = (offset.x / progressBarWidth.toPx()).coerceIn(0f, 1f)
                                    currentValue = when (accuracy) {
                                        ACCURACY_1 -> {
                                            (rangeStart + (rangeEnd - rangeStart) * progress)
                                                .roundToInt()
                                                .toFloat()
                                        }
                                        ACCURACY_2 -> {
                                            ((rangeStart + (rangeEnd - rangeStart) * progress) *
                                                    10).roundToInt() / 10f
                                        }
                                        ACCURACY_3 -> {
                                            ((rangeStart + (rangeEnd - rangeStart) * progress) *
                                                    100).roundToInt() / 100f
                                        }
                                        else -> {
                                            (rangeStart + (rangeEnd - rangeStart) * progress)
                                                .roundToInt()
                                                .toFloat()
                                        }
                                    }
                                    onValueChange(currentValue)
                                }
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
                    Text(text = "$rangeStart", fontSize = 5.nsp(), color = Color.White)
                    Text(text = "$rangeEnd", fontSize = 5.nsp(), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            StateImage(
                normalImage = painterResource(id = R.drawable.formula_add_normal_ic),
                pressedImage = painterResource(id = R.drawable.formula_add_press_ic),
                modifier = Modifier.size(40.dp)
            ) {
                currentValue = when (accuracy) {
                    ACCURACY_1 -> {
                        currentValue.plus(1).coerceIn(rangeStart, rangeEnd)
                    }
                    ACCURACY_2 -> {
                        (((currentValue + 0.1f) * 10).roundToInt() / 10f).coerceIn(rangeStart,
                            rangeEnd)
                    }
                    ACCURACY_3 -> {
                        (((currentValue + 0.01f) * 100).roundToInt() / 100f).coerceIn(rangeStart,
                            rangeEnd)
                    }
                    else -> {
                        currentValue.plus(1).coerceIn(rangeStart, rangeEnd)
                    }
                }
                progress = (currentValue - rangeStart) / (rangeEnd - rangeStart)
                onValueChange(currentValue)
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewUnitValueScrollBar() {
    UnitValueScrollBar(value = 20f, rangeStart = 0f, rangeEnd = 100f, unit = "[tick]") {}
}