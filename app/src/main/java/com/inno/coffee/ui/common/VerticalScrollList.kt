package com.inno.coffee.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.utils.DimenUtils
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun VerticalScrollList(
    modifier: Modifier = Modifier,
    formula: Formula?,
    singularItemColor: Color = Color(0xFF191A1D),
    evenItemColor: Color = Color(0xFF2A2B2D),
) {
    if (formula == null) {
        return
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    val itemCount = 13
    val scrollBarHeight = 190f

    Box(
        modifier = modifier
            .width(543.dp)
            .height(293.dp),
    ) {
        LazyColumn(
            modifier = modifier
                .width(492.dp)
                .height(286.dp)
                .align(Alignment.CenterStart),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_product_type,
                    valueString = formula.productType) {
                }
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_product_name, valueString = formula.productName)
            }
            item {
                val vat = if (formula.vat) stringResource(id = R.string.formula_font_vat)
                else stringResource(id = R.string.formula_back_vat)
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_vat_position, valueString = vat)
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_water_dosage,
                    value = formula.coffeeWater.toFloat(),
                    unit = "[tick]")
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_powder_dosage,
                    value = formula.coffeeWater.toFloat(),
                    unit = "[mm]")
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_press_weight,
                    value = formula.pressWeight.toFloat(),
                    unit = "[kg]")
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_pre_make_time,
                    value = formula.preMakeTime.toFloat(),
                    unit = "[s]")
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_pre_make_wait_time,
                    value = formula.postPreMakeWaitTime.toFloat(),
                    unit = "[s]")
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_second_press_weight,
                    value = formula.secPressWeight.toFloat(),
                    unit = "[kg]")
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_hot_water_dosage,
                    value = formula.hotWater.toFloat(),
                    unit = "[tick]")
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_americano_seq,
                    valueString = "${formula.waterSequence}")
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_coffee_cycles,
                    valueString = "${formula.coffeeCycles}")
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_bypass_dosage,
                    valueString = "${formula.bypassWater}")
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(14.dp)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        dragOffset += dragAmount
                        val scrollOffset = (dragOffset / size.height * lazyListState.layoutInfo
                            .totalItemsCount).roundToInt()
                        coroutineScope.launch {
                            lazyListState.scrollToItem(scrollOffset.coerceIn(0, lazyListState
                                .layoutInfo.totalItemsCount - 1))
                        }
                    }
                }
                .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
        ) {
            val scrollProgress = lazyListState.firstVisibleItemIndex.toFloat() / (itemCount - 1)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scrollBarHeight.dp)
                    .offset {
                        val den = DimenUtils.dp2px(context, 293f)
                        IntOffset(0, (scrollProgress * den).toInt())
                    }
                    .background(Color(0xFF00DE93), RoundedCornerShape(10.dp))
            )
        }
    }
}


@Composable
private fun FormulaItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF191A1D),
    @StringRes description: Int,
    valueString: String = "",
    value: Float = 0f,
    unit: String = "",
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
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Box(
            modifier = modifier
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
            if (valueString.isNotEmpty()) {
                Text(
                    text = valueString, fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 255.dp)
                )
            } else {
                Text(
                    text = "$value", fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 255.dp)
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit, fontSize = 5.nsp(), color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 385.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewVerticalScrollList() {
    val formula = Formula(
        productId = 3, productType = "coffee", productName = "意式",
        vat = true,
        coffeeWater = 20, powderDosage = 50, pressWeight = 20,
        preMakeTime = 29, postPreMakeWaitTime = 30, secPressWeight = 40,
        hotWater = 20, waterSequence = 30, coffeeCycles = 1,
        bypassWater = 1
    )
    VerticalScrollList(formula = formula)
}