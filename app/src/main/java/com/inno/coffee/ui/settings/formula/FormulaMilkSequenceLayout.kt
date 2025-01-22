package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.StateImage
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.FormulaItem

@Composable
fun FormulaMilkSequenceLayout(
    modifier: Modifier = Modifier,
    value: FormulaItem.FormulaMilkSequence,
    onCloseClick: () -> Unit = {},
    onValueChange: (FormulaItem.FormulaMilkSequence) -> Unit = {},
) {
    val bgColor1 = Color(0xFF2A2B2D)
    val bgColor2 = Color(0xFF191A1D)

    val quantity = stringResource(R.string.formula_milk_quantity)
    val temperature = stringResource(R.string.formula_milk_temperature)
    val texture = stringResource(R.string.formula_milk_foam_texture)
    val cold = stringResource(R.string.formula_milk_temperature_cold)
    val warm = stringResource(R.string.formula_milk_temperature_warm)

    val copyValue by remember { mutableStateOf(value.copy()) }
    var itemCount by remember { mutableStateOf(0) }
    var selectedIndex by remember { mutableStateOf(INVALID_INT) }
    var selectedValue by remember { mutableStateOf(0f) }
    var listDefaultValue by remember { mutableStateOf("") }
    var rangeStart by remember { mutableStateOf(0f) }
    var rangeEnd by remember { mutableStateOf(1f) }
    var unit by remember { mutableStateOf("") }

    var milkQuantity1 by remember { mutableStateOf(INVALID_INT) }
    var milkQuantity2 by remember { mutableStateOf(INVALID_INT) }
    var milkTemperature1 by remember { mutableStateOf(INVALID_INT) }
    var milkTemperature2 by remember { mutableStateOf(INVALID_INT) }
    var foamTexture1 by remember { mutableStateOf(INVALID_INT) }
    var foamTexture2 by remember { mutableStateOf(INVALID_INT) }

    LaunchedEffect(copyValue) {
        itemCount = when {
            copyValue.milkQuantity1 != INVALID_INT && copyValue.milkQuantity2 != INVALID_INT -> 2
            copyValue.milkQuantity1 == INVALID_INT && copyValue.milkQuantity2 == INVALID_INT -> 0
            else -> 1
        }
        milkQuantity1 = copyValue.milkQuantity1
        milkQuantity2 = copyValue.milkQuantity2
        milkTemperature1 = copyValue.milkTemperature1
        milkTemperature2 = copyValue.milkTemperature2
        foamTexture1 = copyValue.foamTexture1
        foamTexture2 = copyValue.foamTexture2
    }

    Box(
        modifier = modifier
            .padding(top = 81.dp)
            .background(Color.Black)
            .clickable(enabled = false) {}
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_entrance_close_ic),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 10.dp)
                .size(30.dp)
                .fastclick { onCloseClick() },
        )

        Column(modifier = Modifier.padding(end = 50.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(bottom = 2.dp)
                    .background(color = bgColor2)
                    .debouncedClickable({}),
            ) {
                Text(text = "#1", fontSize = 7.nsp(), color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 19.dp))
                if (itemCount != 2) {
                    StateImage(
                        normalImage = painterResource(R.drawable.formula_milk_add_normal_ic),
                        pressedImage = painterResource(R.drawable.formula_milk_add_press_ic),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 80.dp)
                            .size(36.dp)
                    ) {
                        if (itemCount == 0) {
                            copyValue.milkQuantity1 = copyValue.defaultMilkQuantity
                            copyValue.milkTemperature1 =
                                copyValue.defaultMilkTemperature
                            copyValue.foamTexture1 = copyValue.defaultFoamTexture
                            milkQuantity1 = copyValue.defaultMilkQuantity
                            milkTemperature1 = copyValue.defaultMilkTemperature
                            foamTexture1 = copyValue.defaultFoamTexture
                        } else {
                            copyValue.milkQuantity2 = copyValue.defaultMilkQuantity
                            copyValue.milkTemperature2 =
                                copyValue.defaultMilkTemperature
                            copyValue.foamTexture2 = copyValue.defaultFoamTexture
                            milkQuantity2 = copyValue.defaultMilkQuantity
                            milkTemperature2 = copyValue.defaultMilkTemperature
                            foamTexture2 = copyValue.defaultFoamTexture
                        }
                        itemCount++
                        selectedIndex = INVALID_INT
                        onValueChange(copyValue)
                    }
                }
                if (itemCount == 1) {
                    StateImage(
                        normalImage = painterResource(R.drawable.formula_milk_minus_normal_ic),
                        pressedImage = painterResource(R.drawable.formula_milk_minus_press_ic),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .size(36.dp)
                    ) {
                        if (itemCount == 2) {
                            copyValue.milkQuantity2 = -1
                            copyValue.milkTemperature2 = -1
                            copyValue.foamTexture2 = -1
                        } else {
                            copyValue.milkQuantity1 = -1
                            copyValue.milkTemperature1 = -1
                            copyValue.foamTexture1 = -1
                        }
                        itemCount--
                        selectedIndex = INVALID_INT
                        onValueChange(copyValue)
                    }
                }
            }
            if (itemCount != 0) {
                Column {
                    Item(selectedIndex == 0, bgColor1, quantity, milkQuantity1.toString(), "[s]") {
                        selectedValue = copyValue.milkQuantity1.toFloat()
                        rangeStart = 0f
                        rangeEnd = 100f
                        unit = "[s]"
                        selectedIndex = 0
                    }
                    Item(selectedIndex == 1, bgColor2, temperature,
                        if (milkTemperature1 == 0) cold else warm, "") {
                        listDefaultValue = if (milkTemperature1 == 0) cold else warm
                        selectedIndex = 1
                    }
                    Item(selectedIndex == 2, bgColor1, texture, foamTexture1.toString(), "") {
                        selectedValue = copyValue.foamTexture1.toFloat()
                        rangeStart = 0f
                        rangeEnd = 100f
                        unit = ""
                        selectedIndex = 2
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (itemCount == 2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(bottom = 2.dp)
                        .background(color = bgColor2)
                        .debouncedClickable({}),
                ) {
                    Text(text = "#2", fontSize = 7.nsp(), color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 19.dp))
                    StateImage(
                        normalImage = painterResource(R.drawable.formula_milk_minus_normal_ic),
                        pressedImage = painterResource(R.drawable.formula_milk_minus_press_ic),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .size(36.dp)
                    ) {
                        copyValue.foamTexture2 = -1
                        itemCount--
                        selectedIndex = INVALID_INT
                        onValueChange(copyValue)
                    }
                }
                Column {
                    Item(selectedIndex == 3, bgColor1, quantity, milkQuantity2.toString(), "[s]") {
                        selectedValue = copyValue.milkQuantity2.toFloat()
                        rangeStart = 0f
                        rangeEnd = 100f
                        unit = "[s]"
                        selectedIndex = 3
                    }
                    Item(selectedIndex == 4, bgColor2, temperature,
                        if (milkTemperature2 == 0) cold else warm, "") {
                        listDefaultValue = if (milkTemperature2 == 0) cold else warm
                        selectedIndex = 4
                    }
                    Item(selectedIndex == 5, bgColor1, texture, foamTexture2.toString(), "") {
                        selectedValue = copyValue.foamTexture2.toFloat()
                        rangeStart = 0f
                        rangeEnd = 100f
                        unit = ""
                        selectedIndex = 5
                    }
                }
            }
        }
    }

    if (selectedIndex != INVALID_INT) {
        if (selectedIndex != 1 && selectedIndex != 4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                key(selectedValue) {
                    UnitValueScrollBar(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 250.dp, end = 90.dp)
                            .wrapContentSize(),
                        value = selectedValue, rangeStart = rangeStart,
                        rangeEnd = rangeEnd, unit = unit) { changeValue ->
                        when (selectedIndex) {
                            0 -> {
                                copyValue.milkQuantity1 = changeValue.toInt()
                                milkQuantity1 = changeValue.toInt()
                            }
                            2 -> {
                                copyValue.foamTexture1 = changeValue.toInt()
                                foamTexture1 = changeValue.toInt()
                            }
                            3 -> {
                                copyValue.milkQuantity2 = changeValue.toInt()
                                milkQuantity2 = changeValue.toInt()
                            }
                            5 -> {
                                copyValue.foamTexture2 = changeValue.toInt()
                                foamTexture2 = changeValue.toInt()
                            }
                        }
                        onValueChange(copyValue)
                    }
                }
            }
        } else {
            ListSelectLayout(temperature, listDefaultValue, mapOf(Pair(cold, 0), Pair(warm, 1)),
                { _, changeValue ->
                    when (selectedIndex) {
                        1 -> {
                            copyValue.milkTemperature1 = changeValue as Int
                            milkTemperature1 = changeValue
                        }
                        4 -> {
                            copyValue.milkTemperature2 = changeValue as Int
                            milkTemperature2 = changeValue
                        }
                    }
                    onValueChange(copyValue)
                    selectedIndex = INVALID_INT
                }, {
                    selectedIndex = INVALID_INT
                }
            )
        }
    }
}

@Composable
private fun Item(
    selected: Boolean,
    bgColor: Color,
    description: String,
    value: String,
    unit: String,
    onClick: () -> Unit,
) {
    val selectedColor: Color?
    val textColor: Color?
    if (selected) {
        selectedColor = Color(0xFF00DE93)
        textColor = Color.Black
    } else {
        selectedColor = bgColor
        textColor = Color.White
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(32.dp)
        .padding(bottom = 2.dp)
        .background(color = selectedColor)
        .debouncedClickable({ onClick() }),
        contentAlignment = Alignment.CenterStart) {
        Text(text = description, fontSize = 5.nsp(), color = textColor,
            textAlign = TextAlign.Center, modifier = Modifier.padding(start = 19.dp))
        Text(text = value, fontSize = 5.nsp(), color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 255.dp))
        Text(text = unit, fontSize = 5.nsp(), color = textColor, textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 385.dp))
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFormulaMilkSequenceLayout() {
    FormulaMilkSequenceLayout(modifier = Modifier
        .width(543.dp)
        .height(293.dp),
        FormulaItem.FormulaMilkSequence(defaultMilkQuantity = 1, defaultMilkTemperature = 1,
            defaultFoamTexture = 1, milkQuantity1 = 1, milkQuantity2 = 1, milkTemperature1 = 1,
            milkTemperature2 = 2, foamTexture1 = 1, foamTexture2 = 2))
}