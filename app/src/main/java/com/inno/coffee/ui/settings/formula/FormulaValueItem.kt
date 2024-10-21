package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.ui.settings.formula.formulatype.FormulaAmericanoSeqLayout
import com.inno.coffee.ui.settings.formula.formulatype.FormulaBeanPositionLayout
import com.inno.coffee.ui.settings.formula.formulatype.FormulaChangeNameLayout
import com.inno.coffee.ui.settings.formula.formulatype.FormulaProductTypeLayout
import com.inno.coffee.utilities.FORMULA_PROPERTY_COFFEE_WATER
import com.inno.coffee.utilities.FORMULA_PROPERTY_POWDER_DOSAGE
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.utils.Logger
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

private val formulaPropertyNames = listOf(
    "productType",
    "productName",
    "vat",
    FORMULA_PROPERTY_COFFEE_WATER,
    FORMULA_PROPERTY_POWDER_DOSAGE,
    "pressWeight",
    "preMakeTime",
    "postPreMakeWaitTime",
    "secPressWeight",
    "hotWater",
    "waterSequence",
    "coffeeCycles",
    "bypassWater",
)
private val formulaStringKeys = listOf(
    R.string.formula_product_type,
    R.string.formula_product_name,
    R.string.formula_vat_position,
    R.string.formula_water_dosage,
    R.string.formula_powder_dosage,
    R.string.formula_press_weight,
    R.string.formula_pre_make_time,
    R.string.formula_pre_make_wait_time,
    R.string.formula_second_press_weight,
    R.string.formula_hot_water_dosage,
    R.string.formula_americano_seq,
    R.string.formula_coffee_cycles,
    R.string.formula_bypass_dosage,
)
private val formulaProperties = Formula::class.memberProperties

@Composable
fun FormulaValueItem(
    selectFormula: Formula?,
    onValueChange: () -> Unit,
    onProductTest: () -> Unit,
    onLearn: (Int) -> Unit,
) {
    var selectedValue by remember {
        mutableStateOf<Any?>(null)
    }
    var selectedName by remember {
        mutableStateOf("")
    }
    val formulaItemValue = remember {
        mutableStateListOf<Any>()
    }

    LaunchedEffect(selectFormula) {
        selectedValue = null
        formulaItemValue.clear()
        formulaItemValue.addAll(getFormulaValue(selectFormula))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FormulaFunctionButton(selectedName, { index ->
            onLearn(index)
        }, {
            onProductTest()
        })
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
                VerticalScrollList2(list = formulaItemValue, minimumSize = 9, placeHolder = "",
                    scrollBarWidth = 14, scrollTrackHeight = 300, listPaddingEnd = 48,
                    scrollBarPaddingEnd = 0, listItemHeight = 52f) { index, item ->
                    val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                    val labelResId = formulaStringKeys[index]
                    val label = stringResource(labelResId)
                    Logger.d("FormulaValueItem() called with: index = $index, label = $label " +
                            "item = $item ")

                    FormulaItem(backgroundColor = color,
                        selected = selectedValue == item,
                        description = label, value = item) {
                        selectedValue = item
                        selectedName = formulaPropertyNames[index]
                    }
                }
            }
        }

        selectFormula?.let {
            when (val value = selectedValue) {
                is FormulaItem.FormulaUnitValue -> {
                    key(value) {
                        UnitValueScrollBar(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopEnd)
                                .padding(top = 250.dp, end = 90.dp),
                            unitValue = value) { changeValue ->
                            onValueChange()
                            formulaItemValue.clear()
                            formulaItemValue.addAll(getFormulaValue(selectFormula))
                        }
                    }
                }
                is FormulaItem.FormulaProductType -> {
                    FormulaProductTypeLayout(value, { changeValue ->
                        onValueChange()
                        formulaItemValue.clear()
                        formulaItemValue.addAll(getFormulaValue(selectFormula))
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
                is FormulaItem.FormulaProductName -> {
                    FormulaChangeNameLayout(value, { changeValue ->
                        onValueChange()
                        formulaItemValue.clear()
                        formulaItemValue.addAll(getFormulaValue(selectFormula))
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
                is FormulaItem.FormulaVatPosition -> {
                    FormulaBeanPositionLayout(value, { changeValue ->
                        onValueChange()
                        formulaItemValue.clear()
                        formulaItemValue.addAll(getFormulaValue(selectFormula))
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
                is FormulaItem.FormulaAmericanoSeq -> {
                    FormulaAmericanoSeqLayout(value, { changeValue ->
                        onValueChange()
                        formulaItemValue.clear()
                        formulaItemValue.addAll(getFormulaValue(selectFormula))
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
            }
        }
    }
}

private fun <T : Any, V> updateFormulaValue(
    formula: T,
    property: KProperty1<T, V>?,
    newValue: V
) {
    Logger.d("FormulaViewModel", "updateFormulaValue() property: $property new Value: $newValue")
    if (property is KMutableProperty1<T, V>) {
        property.set(formula, newValue)  // Update the property value
    } else {
        println("The property is not mutable!")
    }
}

private fun getFormulaValue(formula: Formula?): MutableList<Any> {
    val list = mutableListOf<Any>()
    formula?.let {
        formulaPropertyNames.forEach { propertyName ->
            val property = formulaProperties.find { property ->
                property.name == propertyName
            }
            val propertyValue = property?.get(formula) ?: ""
            if (propertyValue != "") {
                list.add(propertyValue)
            }
        }
    }
    return list
}