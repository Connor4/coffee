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
import com.inno.coffee.ui.common.ACCURACY_1
import com.inno.coffee.ui.common.ACCURACY_2
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.ListSelectLayout2
import com.inno.coffee.ui.common.SingleNumberInputLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.utilities.FORMULA_PROPERTY_APPEARANCE
import com.inno.coffee.utilities.FORMULA_PROPERTY_AUTO_FOAM_TEMP
import com.inno.coffee.utilities.FORMULA_PROPERTY_BEAN_HOPPER
import com.inno.coffee.utilities.FORMULA_PROPERTY_COFFEE_WATER
import com.inno.coffee.utilities.FORMULA_PROPERTY_FOAM_MODE
import com.inno.coffee.utilities.FORMULA_PROPERTY_MANUAL_FOAM_TIME
import com.inno.coffee.utilities.FORMULA_PROPERTY_MILK_DELAY_TIME
import com.inno.coffee.utilities.FORMULA_PROPERTY_MILK_OUTPUT
import com.inno.coffee.utilities.FORMULA_PROPERTY_MILK_SEQUENCE
import com.inno.coffee.utilities.FORMULA_PROPERTY_POWDER_DOSAGE
import com.inno.coffee.utilities.FORMULA_PROPERTY_PRESS_WEIGHT
import com.inno.coffee.utilities.FORMULA_PROPERTY_PRODUCT_PRICE
import com.inno.coffee.utilities.FORMULA_PROPERTY_PRODUCT_TYPE
import com.inno.coffee.utilities.FORMULA_PROPERTY_STOP_TEMPERATURE
import com.inno.coffee.utilities.FORMULA_PROPERTY_STOP_TIME
import com.inno.coffee.utilities.FORMULA_PROPERTY_WATER_SEQUENCE
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.formulaProductTypeMultilingual
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import com.inno.common.utils.Logger
import kotlin.reflect.full.memberProperties

private val formulaProperties = Formula::class.memberProperties

private val formulaPropertyNames = listOf(
    FORMULA_PROPERTY_PRODUCT_TYPE,
    "productName",
    FORMULA_PROPERTY_BEAN_HOPPER,
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
    FORMULA_PROPERTY_MANUAL_FOAM_TIME,
    FORMULA_PROPERTY_AUTO_FOAM_TEMP,
    FORMULA_PROPERTY_FOAM_MODE,
    FORMULA_PROPERTY_STOP_TIME,
    FORMULA_PROPERTY_STOP_TEMPERATURE,
    "texture",
    FORMULA_PROPERTY_APPEARANCE,
    FORMULA_PROPERTY_MILK_DELAY_TIME,
    FORMULA_PROPERTY_MILK_OUTPUT,
    FORMULA_PROPERTY_MILK_SEQUENCE,
    FORMULA_PROPERTY_PRODUCT_PRICE,
)

private val formulaPropertyStringMapping = mapOf(
    FORMULA_PROPERTY_PRODUCT_TYPE to R.string.formula_product_type,
    "productName" to R.string.formula_product_name,
    FORMULA_PROPERTY_BEAN_HOPPER to R.string.formula_bean_hopper_position,
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
    FORMULA_PROPERTY_MANUAL_FOAM_TIME to R.string.formula_steam_manual_time,
    FORMULA_PROPERTY_AUTO_FOAM_TEMP to R.string.formula_steam_stop_temperature,
    FORMULA_PROPERTY_FOAM_MODE to R.string.formula_steam_foam_mode,
    FORMULA_PROPERTY_STOP_TIME to R.string.formula_steam_air_stop,
    FORMULA_PROPERTY_STOP_TEMPERATURE to R.string.formula_steam_air_stop,
    "texture" to R.string.formula_steam_foam_texture,
    FORMULA_PROPERTY_APPEARANCE to R.string.formula_milk_appearance,
    FORMULA_PROPERTY_MILK_DELAY_TIME to R.string.formula_milk_delay_time,
    FORMULA_PROPERTY_MILK_OUTPUT to R.string.formula_milk_output,
    FORMULA_PROPERTY_MILK_SEQUENCE to R.string.formula_milk_sequence,
    FORMULA_PROPERTY_PRODUCT_PRICE to R.string.formula_product_price,
)

private val etcSettingsDisableProperties = listOf(
    FORMULA_PROPERTY_PRODUCT_TYPE,
    "productName",
    FORMULA_PROPERTY_BEAN_HOPPER,
)

private val accuracyProperties = listOf(
    "preMakeTime",
    "postPreMakeWaitTime",
    "manualFoamTime",
    FORMULA_PROPERTY_STOP_TIME,
    FORMULA_PROPERTY_MILK_DELAY_TIME,
)

@Composable
fun FormulaValueItem(
    isFahrenheit: Boolean,
    selectFormula: Formula?,
    fromETCSetting: Boolean = false,
    onValueChange: () -> Unit,
    onProductTest: () -> Unit,
    onLearn: (Int) -> Unit,
    onChangeType: (type: String) -> Unit = {},
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
        selectedName = ""
        getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val extraPaddingTop = if (fromETCSetting) 10 else 160
        FormulaFunctionButton(selectedName, extraPaddingTop, { index ->
            onLearn(index)
        }, {
            onProductTest()
        })
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = (171 + extraPaddingTop).dp, end = 38.dp)
                .width(543.dp)
                .height(293.dp),
        ) {
            val extraCount = findExtraCount(selectFormula)

            VerticalScrollList2(list = formulaItemValues, minimumSize = 9, extraSize = extraCount,
                placeHolder = "", scrollBarWidth = 14, scrollTrackHeight = 300, listPaddingEnd = 48,
                scrollBarPaddingEnd = 0, listItemHeight = 52f) { index, item ->
                // IS THIS A GOOD WAY TO PREVENT EMPTY LIST?
                if (formulaItemValues.isEmpty()) {
                    return@VerticalScrollList2
                }

                val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)

                if (formulaItemValues.size <= index) {
                    FormulaItem(isFahrenheit = isFahrenheit, backgroundColor = color,
                        selected = false,
                        description = "", value = "")
                    return@VerticalScrollList2
                }

                val name = formulaItemNames[index]
                val labelResId = formulaPropertyStringMapping[name]
                val label = stringResource(labelResId!!)
                val enable = if (fromETCSetting) name !in etcSettingsDisableProperties else true

                FormulaItem(
                    isFahrenheit = isFahrenheit, backgroundColor = color,
                    selected = selectedValue == item,
                    description = label, value = item, enable = enable,
                    onClick = {
                        selectedValue = item
                        selectedName = name
                    },
                )
            }
        }

        selectFormula?.let {
            when (val value = selectedValue) {
                is FormulaItem.FormulaUnitValue -> {
                    key(value) {
                        val accuracy =
                            if (selectedName in accuracyProperties) ACCURACY_2 else ACCURACY_1
                        UnitValueScrollBar(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopEnd)
                                .padding(top = (90 + extraPaddingTop).dp, end = 90.dp),
                            value = value.value,
                            rangeStart = value.rangeStart,
                            rangeEnd = value.rangeEnd,
                            unit = value.unit,
                            accuracy = accuracy
                        ) { changeValue ->
                            value.value = changeValue
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                        }
                    }
                }
                is FormulaItem.FormulaTemperatureValue -> {
                    key(value) {
                        UnitValueScrollBar(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopEnd)
                                .padding(top = (90 + extraPaddingTop).dp, end = 90.dp),
                            value = temperatureConvertFloat(isFahrenheit,
                                value.celsiusValue.toInt()),
                            rangeStart = temperatureConvertFloat(isFahrenheit,
                                value.celsiusRangeStart),
                            rangeEnd = temperatureConvertFloat(isFahrenheit, value.celsiusRangeEnd),
                            unit = if (isFahrenheit) "°F" else "°C"
                        ) { changeValue ->
                            value.celsiusValue = temperatureRevert(isFahrenheit, changeValue)
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                        }
                    }
                }
                is FormulaItem.FormulaProductType -> {
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_PRODUCT_TYPE]
                    ListSelectLayout2(stringResource(title!!), value.type,
                        formulaProductTypeMultilingual, { key, _ ->
                            onChangeType(key)
//                            value.type = key
//                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
//                            onValueChange()
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
                is FormulaItem.FormulaProductPrice -> {
                    val default = String.format("%.2f", value.price)
                    SingleNumberInputLayout(defaultInput = default,
                        title = stringResource(R.string.formula_product_price),
                        tips = stringResource(R.string.formula_product_price_tips),
                        onEnterClick = { changeValue ->
                            value.price = changeValue
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, onCloseClick = {
                            selectedValue = null
                        })
                }
                is FormulaItem.FormulaBeanHopperPosition -> {
                    val front = stringResource(R.string.formula_front_hopper)
                    val back = stringResource(R.string.formula_rear_hopper)
                    val default = if (value.position) front else back
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_BEAN_HOPPER]
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
                is FormulaItem.FormulaFoamMode -> {
                    val temperature = stringResource(R.string.formula_steam_foam_mode_temperature)
                    val time = stringResource(R.string.formula_steam_foam_mode_time)
                    val default = if (value.everFoamMode) time else temperature
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_FOAM_MODE]
                    ListSelectLayout(
                        stringResource(title!!), default, mapOf(Pair(time, true),
                            Pair(temperature, false)),
                        { _, changeValue ->
                            value.everFoamMode = changeValue as Boolean
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
                is FormulaItem.FormulaAppearance -> {
                    val white = stringResource(R.string.formula_milk_white_on_top)
                    val brown = stringResource(R.string.formula_milk_brown_on_top)
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_APPEARANCE]
                    val default = if (value.appearance) brown else white
                    ListSelectLayout(stringResource(title!!), default, mapOf(Pair(brown, true),
                        Pair(white, false)),
                        { _, changeValue ->
                            value.appearance = changeValue as Boolean
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
                is FormulaItem.FormulaMilkOutput -> {
                    val coffee = stringResource(R.string.formula_milk_output_coffee_outlet)
                    val milk = stringResource(R.string.formula_milk_output_milk_arm)
                    val title = formulaPropertyStringMapping[FORMULA_PROPERTY_MILK_OUTPUT]
                    val default = if (value.output) milk else coffee
                    ListSelectLayout(stringResource(title!!), default, mapOf(Pair(milk, true),
                        Pair(coffee, false)),
                        { _, changeValue ->
                            value.output = changeValue as Boolean
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                            selectedValue = null
                        }, {
                            selectedValue = null
                        }
                    )
                }
                is FormulaItem.FormulaMilkSequence -> {
                    FormulaMilkSequenceLayout(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = (90 + extraPaddingTop).dp, end = 38.dp)
                            .width(543.dp)
                            .height(374.dp),
                        value = value,
                        onCloseClick = {
                            selectedValue = null
                        },
                        onValueChange = { changeValue ->
                            value.milkQuantity1 = changeValue.milkQuantity1
                            value.milkTemperature1 = changeValue.milkTemperature1
                            value.foamTexture1 = changeValue.foamTexture1
                            value.milkQuantity2 = changeValue.milkQuantity2
                            value.milkTemperature2 = changeValue.milkTemperature2
                            value.foamTexture2 = changeValue.foamTexture2
                            getFormulaValue(selectFormula, formulaItemNames, formulaItemValues)
                            onValueChange()
                        },
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
                Logger.d("FormulaValueItem", "getFormulaValue() called with:" +
                        " propertyName = $propertyName value $propertyValue")

                nameList.add(propertyName)
                valueList.add(propertyValue)
            }
        }
        removeFoamProperties(nameList, valueList)
    }
}

private fun removeFoamProperties(nameList: MutableList<String>, valueList: MutableList<Any>) {
    // 判断是否特色蒸汽
    val foamModeIndex = nameList.indexOf(FORMULA_PROPERTY_FOAM_MODE)
    if (foamModeIndex != -1) {
        val modeValue = valueList[foamModeIndex] as FormulaItem.FormulaFoamMode
        if (modeValue.everFoamMode) {
            val tempIndex = nameList.indexOf(FORMULA_PROPERTY_STOP_TEMPERATURE)
            nameList.removeAt(tempIndex)
            valueList.removeAt(tempIndex)
        } else {
            val timeIndex = nameList.indexOf(FORMULA_PROPERTY_STOP_TIME)
            nameList.removeAt(timeIndex)
            valueList.removeAt(timeIndex)
        }
    }
}

private fun findExtraCount(
    formula: Formula?,
): Int {
    return formula?.let {
        formulaProperties.find { property ->
            property.name == FORMULA_PROPERTY_MILK_SEQUENCE
        }?.get(formula)?.let { propertyValue ->
            val value = propertyValue as FormulaItem.FormulaMilkSequence
            when {
                value.foamTexture1 != INVALID_INT && value.foamTexture2 != INVALID_INT -> 2
                value.foamTexture1 == INVALID_INT && value.foamTexture2 == INVALID_INT -> 0
                else -> 1
            }
        } ?: 0
    } ?: 0
}

private fun temperatureConvertFloat(isFahrenheit: Boolean, celsius: Int): Float {
    return if (isFahrenheit) {
        ((celsius * 9 / 5) + 32).toFloat()
    } else {
        celsius.toFloat()
    }
}

private fun temperatureRevert(isFahrenheit: Boolean, value: Float): Short {
    val result = if (isFahrenheit) {
        Math.round((value - 32) * 5 / 9)
    } else {
        Math.round(value)
    }
    return result.toShort()
}