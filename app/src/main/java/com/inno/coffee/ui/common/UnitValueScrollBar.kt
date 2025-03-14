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
    // 校验 value、rangeStart 和 rangeEnd 的合法性
    val safeValue = if (value.isNaN() || value.isInfinite()) 0f else value
    val safeRangeStart = if (rangeStart.isNaN() || rangeStart.isInfinite()) 0f else rangeStart
    val safeRangeEnd = if (rangeEnd.isNaN() || rangeEnd.isInfinite()) 0f else rangeEnd

    // 确保 rangeStart < rangeEnd
    val validRangeStart = minOf(safeRangeStart, safeRangeEnd)
    val validRangeEnd = maxOf(safeRangeStart, safeRangeEnd)

    // 计算 progress
    val defaultProgress = if (validRangeEnd == validRangeStart) {
        0f // 避免除以零
    } else {
        ((safeValue - validRangeStart) / (validRangeEnd - validRangeStart)).coerceIn(0f, 1f)
    }
    var currentValue by remember {
        mutableFloatStateOf(safeValue.coerceIn(validRangeStart, validRangeEnd))
    }
    var progress by remember {
        mutableFloatStateOf(defaultProgress)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 381.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val realShowCurrentValue = when (accuracy) {
                ACCURACY_1 -> {
                    "${currentValue.toInt()}"
                }
                ACCURACY_2 -> {
                    "${(currentValue * 10).roundToInt() / 10f}"
                }
                ACCURACY_3 -> {
                    "${(currentValue * 100).roundToInt() / 100f}"
                }
                else -> {
                    "${currentValue.toInt()}"
                }
            }
            Text(text = realShowCurrentValue, fontSize = 6.nsp(), color = Color.White)
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
                        currentValue.minus(1).coerceIn(validRangeStart, validRangeEnd)
                    }
                    ACCURACY_2 -> {
                        (((currentValue - 0.1f) * 10).roundToInt() / 10f).coerceIn(validRangeStart,
                            validRangeEnd)
                    }
                    ACCURACY_3 -> {
                        (((currentValue - 0.01f) * 100).roundToInt() / 100f).coerceIn(
                            validRangeStart,
                            validRangeEnd)
                    }
                    else -> {
                        currentValue.minus(1).coerceIn(validRangeStart, validRangeEnd)
                    }
                }
                progress = (currentValue - validRangeStart) / (validRangeEnd - validRangeStart)
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
                                        (validRangeStart + (validRangeEnd - validRangeStart) * progress)
                                            .roundToInt()
                                            .toFloat()
                                    }
                                    ACCURACY_2 -> {
                                        ((validRangeStart + (validRangeEnd - validRangeStart) * progress) *
                                                10).roundToInt() / 10f
                                    }
                                    ACCURACY_3 -> {
                                        ((validRangeStart + (validRangeEnd - validRangeStart) * progress) *
                                                100).roundToInt() / 100f
                                    }
                                    else -> {
                                        (validRangeStart + (validRangeEnd - validRangeStart) * progress)
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
                                            (validRangeStart + (validRangeEnd - validRangeStart) * progress)
                                                .roundToInt()
                                                .toFloat()
                                        }
                                        ACCURACY_2 -> {
                                            ((validRangeStart + (validRangeEnd - validRangeStart) * progress) *
                                                    10).roundToInt() / 10f
                                        }
                                        ACCURACY_3 -> {
                                            ((validRangeStart + (validRangeEnd - validRangeStart) * progress) *
                                                    100).roundToInt() / 100f
                                        }
                                        else -> {
                                            (validRangeStart + (validRangeEnd - validRangeStart) * progress)
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
                    val realShowRangeStart = when (accuracy) {
                        ACCURACY_1 -> {
                            "${validRangeStart.toInt()}"
                        }
                        ACCURACY_2 -> {
                            "${(validRangeStart * 10).roundToInt() / 10f}"
                        }
                        ACCURACY_3 -> {
                            "${(validRangeStart * 100).roundToInt() / 100f}"
                        }
                        else -> {
                            "${validRangeStart.toInt()}"
                        }
                    }
                    val realShowRangeEnd = when (accuracy) {
                        ACCURACY_1 -> {
                            "${validRangeEnd.toInt()}"
                        }
                        ACCURACY_2 -> {
                            "${(validRangeEnd * 10).roundToInt() / 10f}"
                        }
                        ACCURACY_3 -> {
                            "${(validRangeEnd * 100).roundToInt() / 100f}"
                        }
                        else -> {
                            "${validRangeEnd.toInt()}"
                        }
                    }
                    Text(text = realShowRangeStart, fontSize = 5.nsp(), color = Color.White)
                    Text(text = realShowRangeEnd, fontSize = 5.nsp(), color = Color.White)
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
                        currentValue.plus(1).coerceIn(validRangeStart, validRangeEnd)
                    }
                    ACCURACY_2 -> {
                        (((currentValue + 0.1f) * 10).roundToInt() / 10f).coerceIn(validRangeStart,
                            validRangeEnd)
                    }
                    ACCURACY_3 -> {
                        (((currentValue + 0.01f) * 100).roundToInt() / 100f).coerceIn(
                            validRangeStart, validRangeEnd)
                    }
                    else -> {
                        currentValue.plus(1).coerceIn(validRangeStart, validRangeEnd)
                    }
                }
                progress = (currentValue - validRangeStart) / (validRangeEnd - validRangeStart)
                onValueChange(currentValue)
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewUnitValueScrollBar() {
    UnitValueScrollBar(value = 20f, rangeStart = 0f, rangeEnd = 100f, unit = "[月]") {}
}