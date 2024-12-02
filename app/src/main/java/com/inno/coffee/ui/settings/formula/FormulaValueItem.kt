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
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.ListSelectLayout2
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.utilities.FORMULA_PROPERTY_COFFEE_WATER
import com.inno.coffee.utilities.FORMULA_PROPERTY_POWDER_DOSAGE
import com.inno.coffee.utilities.FORMULA_PROPERTY_PRESS_WEIGHT
import com.inno.coffee.utilities.FORMULA_PROPERTY_PRODUCT_TYPE
import com.inno.coffee.utilities.FORMULA_PROPERTY_VAT
import com.inno.coffee.utilities.FORMULA_PROPERTY_WATER_SEQUENCE
import com.inno.coffee.utilities.formulaProductTypeMultilingual
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.utils.Logger
import kotlin.reflect.full.memberProperties

private val formulaProperties = Formula::class.memberProperties

private val formulaPropertyNames = listOf(
    FORMULA_PROPERTY_PRODUCT_TYPE,
    "productName",
    FORMULA_PROPERTY_VAT,
    FORMULA_PROPERTY_COFFEE_WATER,
    FORMULA_PROPERTY_POWDER_DOSAGE,
    FORMULA_PROPERTY_PRESS_WEIGHT,
    "preMakeTime",
    "postPreMakeWaitTime",
    "secPressWeight",
    "hotWater",
    FORMULA_PROPERTY_WATER_SEQUENCE,
    "coffeeCycles",
    "bypassWater",
    "foam",
    "milk",
)

private val formulaPropertyStringMapping = mapOf(
    FORMULA_PROPERTY_PRODUCT_TYPE to R.string.formula_product_type,
    "productName" to R.string.formula_product_name,
    FORMULA_PROPERTY_VAT to R.string.formula_bean_hopper_position,
    FORMULA_PROPERTY_COFFEE_WATER to R.string.formula_water_dosage,
    FORMULA_PROPERTY_POWDER_DOSAGE to R.string.formula_powder_dosage,
    FORMULA_PROPERTY_PRESS_WEIGHT to R.string.formula_press_weight,
    "preMakeTime" to R.string.formula_pre_make_time,
    "postPreMakeWaitTime" to R.string.formula_pre_make_wait_time,
    "secPressWeight" to R.string.formula_second_press_weight,
    "hotWater" to R.string.formula_hot_water_dosage,
    FORMULA_PROPERTY_WATER_SEQUENCE to R.string.formula_americano_seq,
    "coffeeCycles" to R.string.formula_coffee_cycles,
    "bypassWater" to R.string.formula_bypass_dosage,
    "foam" to R.string.formula_foam,
    "milk" to R.string.formula_milk,
)

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
    val formulaItemNames = remember {
        mutableStateListOf<String>()
    }
    val formulaItemValues = remember {
        mutableStateListOf<Any>()
    }

    LaunchedEffect(selectFormula) {
        selectedValue = null
        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
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
                .align(Alignment.TopEnd)
                .padding(top = 331.dp, end = 38.dp)
                .width(543.dp)
                .height(293.dp),
        ) {
            VerticalScrollList2(list = formulaItemValues, minimumSize = 9, placeHolder = "",
                scrollBarWidth = 14, scrollTrackHeight = 300, listPaddingEnd = 48,
                scrollBarPaddingEnd = 0, listItemHeight = 52f) { index, item ->
                // IS THIS A GOOD WAY TO PREVENT EMPTY LIST?
                if (formulaItemValues.size < 1) {
                    return@VerticalScrollList2
                }
                val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)

                if (formulaItemValues.size <= index) {
                    FormulaItem(backgroundColor = color, selected = false,
                        description = "", value = "")
                } else {
                    val labelResId = formulaPropertyStringMapping[formulaItemNames[index]]
                    val label = stringResource(labelResId!!)

                    FormulaItem(backgroundColor = color, selected = selectedValue == item,
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
                            value = value.value.toFloat(),
                            rangeStart = value.rangeStart,
                            rangeEnd = value.rangeEnd,
                            unit = value.unit
                        ) { changeValue ->
                            value.value = changeValue.toInt().toShort()
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                        }
                    }
                }
                is FormulaItem.FormulaProductType -> {
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_PRODUCT_TYPE]
                    ListSelectLayout2(stringResource(title!!), value.type,
                        formulaProductTypeMultilingual, { key, _ ->
                            value.type = key
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
                is FormulaItem.FormulaProductName -> {
                    FormulaChangeNameLayout(value, { changeValue ->
                        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                        onValueChange()
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
                is FormulaItem.FormulaVatPosition -> {
                    val front = stringResource(R.string.formula_front_hopper)
                    val back = stringResource(R.string.formula_rear_hopper)
                    val default = if (value.position) front else back
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_VAT]
                    ListSelectLayout(
                        stringResource(title!!), default, mapOf(Pair(front, true), Pair(back,
                            false)),
                        { _, changeValue ->
                            value.position = changeValue as Boolean
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
                is FormulaItem.FormulaAmericanoSeq -> {
                    val cw = stringResource(R.string.formula_americano_seq_c_w)
                    val wc = stringResource(R.string.formula_americano_seq_w_c)
                    val default = if (value.sequence) cw else wc
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_WATER_SEQUENCE]
                    ListSelectLayout(
                        stringResource(title!!), default, mapOf(Pair(cw, true), Pair(wc, false)),
                        { _, changeValue ->
                            value.sequence = changeValue as Boolean
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
                is FormulaItem.FormulaPressWeight -> {
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_PRESS_WEIGHT]
                    ListSelectLayout(stringResource(title!!), "${value.weight} [kg]",
                        mapOf(Pair("20 [kg]", 20.toShort()), Pair("40 [kg]", 40.toShort()),
                            Pair("60 [kg]", 60.toShort())),
                        { _, changeValue ->
                            value.weight = changeValue as Short
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
            }
        }
    }
}

private fun getFormulaValue(
    formula: Formula?, nameList: MutableList<String>,
    valueList: MutableList<Any>,
) {
    formula?.let {
        nameList.clear()
        valueList.clear()
        formulaPropertyNames.forEach { propertyName ->
            val property = formulaProperties.find { property ->
                property.name == propertyName
            }
            val propertyValue = property?.get(formula) ?: ""
            if (propertyValue != "") {
                Logger.d("FormulaValueItem",
                    "getFormulaValue() called with: propertyName = $propertyName value " +
                            "$propertyValue")
                nameList.add(propertyName)
                valueList.add(propertyValue)
            }
        }
    }
}