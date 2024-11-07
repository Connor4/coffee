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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.enums.ProductType
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
                    value = formula.productType?.type ?: "") {
                }
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_product_name,
                    value = formula.productName?.name ?: "")
            }
            item {
                val vat = if (formula.vat?.position != false) stringResource(id = R.string
                    .formula_front_hopper)
                else stringResource(id = R.string.formula_rear_hopper)
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_bean_hopper_position, value = vat)
            }
            item {
                FormulaItem(
                    backgroundColor = evenItemColor,
                    description = R.string.formula_water_dosage,
                    value = formula.coffeeWater ?: 0,
                )
            }
            item {
                FormulaItem(
                    backgroundColor = singularItemColor,
                    description = R.string.formula_powder_dosage,
                    value = formula.coffeeWater ?: 0
                )
            }
            item {
                FormulaItem(
                    backgroundColor = evenItemColor,
                    description = R.string.formula_press_weight,
                    value = formula.pressWeight ?: 0,
                )
            }
            item {
                FormulaItem(
                    backgroundColor = singularItemColor,
                    description = R.string.formula_pre_make_time,
                    value = formula.preMakeTime ?: 0,
                )
            }
            item {
                FormulaItem(
                    backgroundColor = evenItemColor,
                    description = R.string.formula_pre_make_wait_time,
                    value = formula.postPreMakeWaitTime ?: 0,
                )
            }
            item {
                FormulaItem(
                    backgroundColor = singularItemColor,
                    description = R.string.formula_second_press_weight,
                    value = formula.secPressWeight ?: 0,
                )
            }
            item {
                FormulaItem(
                    backgroundColor = evenItemColor,
                    description = R.string.formula_hot_water_dosage,
                    value = formula.hotWater ?: 0,
                )
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_americano_seq,
                    value = "${formula.waterSequence}")
            }
            item {
                FormulaItem(backgroundColor = evenItemColor,
                    description = R.string.formula_coffee_cycles,
                    value = "${formula.coffeeCycles}")
            }
            item {
                FormulaItem(backgroundColor = singularItemColor,
                    description = R.string.formula_bypass_dosage,
                    value = "${formula.bypassWater}")
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
            if (value is FormulaItem.FormulaUnitValue) {
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

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewVerticalScrollList() {
    val formula = Formula(
        productId = 3, productType = FormulaItem.FormulaProductType(ProductType
            .COFFEE.value),
        productName = FormulaItem.FormulaProductName("意式", "home_item_espresso"),
        vat = FormulaItem.FormulaVatPosition(true),
        coffeeWater = FormulaItem.FormulaUnitValue(20,
            0f,
            100f,
            "[mm]"),
        powderDosage = FormulaItem.FormulaUnitValue(50,
            0f,
            1000f,
            "[tick]"), pressWeight = FormulaItem.FormulaPressWeight(20,
            0f,
            50f,
            "[kg]"),
        preMakeTime = FormulaItem.FormulaUnitValue(800,
            0f,
            1000f,
            "[s]"),
        postPreMakeWaitTime = FormulaItem.FormulaUnitValue(2000,
            0f,
            1000f,
            "[s]"),
        secPressWeight = FormulaItem.FormulaUnitValue(0,
            0f,
            1000f,
            "[mm]"),
        hotWater = FormulaItem.FormulaUnitValue(150,
            0f,
            1000f,
            "[tick]"),
        waterSequence = FormulaItem.FormulaAmericanoSeq(true),
        coffeeCycles = FormulaItem.FormulaUnitValue(
            value = 1,
            rangeStart = 0f,
            rangeEnd = 10f,
            unit = "[-]"
        ),
        bypassWater = FormulaItem.FormulaUnitValue(
            value = 0,
            rangeStart = 0f,
            rangeEnd = 10f,
            unit = "[%]"
        )
    )
    VerticalScrollList(formula = formula)
}