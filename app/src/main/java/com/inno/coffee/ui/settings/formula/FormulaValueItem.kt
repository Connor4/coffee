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
import com.inno.coffee.ui.settings.formula.formulatype.FormulaPressWeightLayout
import com.inno.coffee.ui.settings.formula.formulatype.FormulaProductTypeLayout
import com.inno.coffee.utilities.FORMULA_PROPERTY_COFFEE_WATER
import com.inno.coffee.utilities.FORMULA_PROPERTY_POWDER_DOSAGE
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.utils.Logger
import kotlin.reflect.full.memberProperties

private val formulaProperties = Formula::class.memberProperties

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
    "foam",
    "milk",
)

private val formulaPropertyStringMapping = mapOf(
    "productType" to R.string.formula_product_type,
    "productName" to R.string.formula_product_name,
    "vat" to R.string.formula_vat_position,
    FORMULA_PROPERTY_COFFEE_WATER to R.string.formula_water_dosage,
    FORMULA_PROPERTY_POWDER_DOSAGE to R.string.formula_powder_dosage,
    "pressWeight" to R.string.formula_press_weight,
    "preMakeTime" to R.string.formula_pre_make_time,
    "postPreMakeWaitTime" to R.string.formula_pre_make_wait_time,
    "secPressWeight" to R.string.formula_second_press_weight,
    "hotWater" to R.string.formula_hot_water_dosage,
    "waterSequence" to R.string.formula_americano_seq,
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
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 331.dp, end = 38.dp)
        ) {
            Box(
                modifier = Modifier
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
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                        }
                    }
                }
                is FormulaItem.FormulaProductType -> {
                    FormulaProductTypeLayout(value, { changeValue ->
                        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                        onValueChange()
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
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
                    FormulaBeanPositionLayout(value, { changeValue ->
                        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                        onValueChange()
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
                is FormulaItem.FormulaAmericanoSeq -> {
                    FormulaAmericanoSeqLayout(value, { changeValue ->
                        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                        onValueChange()
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
                is FormulaItem.FormulaPressWeight -> {
                    FormulaPressWeightLayout(value, {
                        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                        onValueChange()
                        selectedValue = null
                    }, {
                        selectedValue = null
                    })
                }
            }
        }
    }
}

private fun getFormulaValue(formula: Formula?, nameList: MutableList<String>,
    valueList: MutableList<Any>
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