package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaAmericanoSeq
import com.inno.common.db.entity.FormulaProductName
import com.inno.common.db.entity.FormulaProductType
import com.inno.common.db.entity.FormulaUnitValue
import com.inno.common.db.entity.FormulaVatPosition
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

private val formulaPropertyNames = listOf(
    "productType",
    "productName",
    "vat",
    "coffeeWater",
    "powderDosage",
    "pressWeight",
    "preMakeTime",
    "postPreMakeWaitTime",
    "secPressWeight",
    "hotWater",
    "waterSequence",
    "coffeeCycles",
    "bypassWater",
)
private val formulaStringKeys = mapOf(
    "productType" to R.string.formula_product_type,
    "productName" to R.string.formula_product_name,
    "vat" to R.string.formula_vat_position,
    "coffeeWater" to R.string.formula_water_dosage,
    "powderDosage" to R.string.formula_powder_dosage,
    "pressWeight" to R.string.formula_press_weight,
    "preMakeTime" to R.string.formula_pre_make_time,
    "postPreMakeWaitTime" to R.string.formula_pre_make_wait_time,
    "secPressWeight" to R.string.formula_second_press_weight,
    "hotWater" to R.string.formula_hot_water_dosage,
    "waterSequence" to R.string.formula_americano_seq,
    "coffeeCycles" to R.string.formula_coffee_cycles,
    "bypassWater" to R.string.formula_bypass_dosage,
)
private val formulaProperties = Formula::class.memberProperties

@Composable
fun FormulaValueItem(
    selectFormula: Formula?,
    onValueChange: () -> Unit,
) {
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    var selectedValue by remember {
        mutableStateOf<Any?>(null)
    }
    var selectedName by remember {
        mutableStateOf("")
    }

    LaunchedEffect(selectFormula) {
        selectedIndex = -1
        selectedValue = null
        selectedName = ""
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        selectFormula?.let {
            when (val value = selectedValue) {
                is FormulaUnitValue -> {
                    key(value) {
                        UnitValueScrollBar(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopEnd)
                                .padding(top = 250.dp, end = 90.dp),
                            unitValue = value) { changeValue ->
                            val property = formulaProperties.find {
                                it.name == selectedName
                            }
                            updateFormulaValue(selectFormula, property, changeValue)
                            onValueChange()
                        }
                    }
                }
                is FormulaProductType -> {
                }
                is FormulaProductName -> {
                }
                is FormulaVatPosition -> {
                }
                is FormulaAmericanoSeq -> {
                }
            }
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
                VerticalScrollList2(list = formulaPropertyNames, minimumSize = 9, placeHolder = "",
                    scrollBarWidth = 14, scrollTrackHeight = 300, listPaddingEnd = 48,
                    scrollBarPaddingEnd = 0, listItemHeight = 52f) { index, item ->

                    selectFormula?.let { formula ->
                        val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                        val property = formulaProperties.find {
                            it.name == item
                        }
                        val propertyValue = property?.get(formula) ?: ""

                        val labelResId = formulaStringKeys[item] ?: -1
                        val label = stringResource(labelResId)

                        FormulaItem(backgroundColor = color,
                            selected = selectedIndex == index,
                            description = label, value = propertyValue) {
                            selectedIndex = index
                            selectedValue = propertyValue
                            selectedName = item.toString()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun FormulaItem(
    backgroundColor: Color,
    selected: Boolean,
    description: String,
    value: Any,
    onClick: () -> Unit = {},
) {
    val bgColor: Color?
    val textColor: Color?
    if (selected) {
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
                .debouncedClickable({
                    onClick()
                }),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = description, fontSize = 5.nsp(), color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 19.dp)
            )
            val textValue: String
            when (value) {
                is FormulaUnitValue -> {
                    textValue = value.value.toString()
                    Text(
                        text = value.unit, fontSize = 5.nsp(), color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 385.dp)
                    )
                }
                is FormulaProductType -> {
                    textValue = value.type
                }
                is FormulaProductName -> {
                    textValue = value.name
                }
                is FormulaVatPosition -> {
                    textValue = if (value.position) stringResource(R.string.formula_font_vat)
                    else stringResource(R.string.formula_back_vat)
                }
                is FormulaAmericanoSeq -> {
                    textValue =
                        if (value.sequence) stringResource(R.string.formula_americano_seq_c_w)
                        else stringResource(R.string.formula_americano_seq_w_c)
                }
                else -> {
                    textValue = value.toString()
                }
            }
            Text(
                text = textValue, fontSize = 5.nsp(), color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 255.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

private fun <T : Any, V> updateFormulaValue(
    formula: T,
    property: KProperty1<T, V>?,
    newValue: V
) {
    if (property is KMutableProperty1<T, V>) {
        property.set(formula, newValue)  // Update the property value
    } else {
        println("The property is not mutable!")
    }
}