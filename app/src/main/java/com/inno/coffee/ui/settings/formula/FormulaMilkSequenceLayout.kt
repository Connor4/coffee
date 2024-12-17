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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
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
    val copyValue by remember { mutableStateOf(value.copy()) }

    val quantity = stringResource(R.string.formula_milk_quantity)
    val temperature = stringResource(R.string.formula_milk_temperature)
    val texture = stringResource(R.string.formula_milk_foam_texture)

    val itemCount = when {
        copyValue.milkQuantity1 != INVALID_INT && copyValue.milkQuantity2 != INVALID_INT -> 2
        copyValue.milkQuantity1 == INVALID_INT && copyValue.milkQuantity2 == INVALID_INT -> 0
        else -> 1
    }

    Box(
        modifier = modifier
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

        Column(
            modifier = Modifier.padding(end = 50.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(bottom = 2.dp)
                    .background(color = bgColor2)
                    .debouncedClickable({
                    }),
            ) {
                Text(text = "#1", fontSize = 7.nsp(), color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 19.dp)
                )
                Image(
                    painter = painterResource(R.drawable.temp_add_ic),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 60.dp)
                        .size(40.dp)
                        .fastclick {
                            if (itemCount == 0) {
                                copyValue.milkQuantity1 = copyValue.defaultMilkQuantity
                                copyValue.milkTemperature1 = copyValue.defaultMilkTemperature
                                copyValue.foamTexture1 = copyValue.defaultFoamTexture
                            } else {
                                copyValue.milkQuantity2 = copyValue.defaultMilkQuantity
                                copyValue.milkTemperature2 = copyValue.defaultMilkTemperature
                                copyValue.foamTexture2 = copyValue.defaultFoamTexture
                            }
                            onValueChange(copyValue)
                        }
                )
                Image(
                    painter = painterResource(R.drawable.temp_minus_ic),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                        .size(40.dp)
                        .fastclick {
                            if (itemCount == 2) {
                                copyValue.milkQuantity2 = -1
                                copyValue.milkTemperature2 = -1
                                copyValue.foamTexture2 = -1
                            } else {
                                copyValue.milkQuantity1 = -1
                                copyValue.milkTemperature1 = -1
                                copyValue.foamTexture1 = -1
                            }
                            onValueChange(copyValue)
                        }
                )
            }
            if (itemCount != 0) {
                Column {
                    Item(false, bgColor1, quantity, copyValue.milkQuantity1.toString(), "[s]")
                    Item(false, bgColor2, temperature, copyValue.milkTemperature1.toString(), "")
                    Item(false, bgColor1, texture, copyValue.foamTexture1.toString(), "")
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
                        .debouncedClickable({
                        }),
                ) {
                    Text(
                        text = "#2", fontSize = 7.nsp(), color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 19.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.temp_minus_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .size(40.dp)
                            .fastclick {
                                copyValue.milkQuantity2 = -1
                                copyValue.milkTemperature2 = -1
                                copyValue.foamTexture2 = -1
                                onValueChange(copyValue)
                            }
                    )
                }
                Column {
                    Item(false, bgColor1, quantity, copyValue.milkQuantity2.toString(), "[s]")
                    Item(false, bgColor2, temperature, copyValue.milkTemperature2.toString(), "")
                    Item(false, bgColor1, texture, copyValue.foamTexture2.toString(), "")
                }
            }
        }
    }
}

@Composable
private fun Item(
    selected: Boolean,
    bgColor: Color,
    description: String,
    copyValue: String,
    unit: String,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(bottom = 2.dp)
            .background(color = selectedColor)
            .debouncedClickable({
            }),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = description, fontSize = 5.nsp(), color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 19.dp)
        )
        Text(
            text = copyValue, fontSize = 5.nsp(),
            color = textColor, textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 255.dp)
        )
        Text(
            text = unit, fontSize = 5.nsp(), color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 385.dp)
        )
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFormulaMilkSequenceLayout() {
    FormulaMilkSequenceLayout(modifier = Modifier
        .width(543.dp)
        .height(293.dp), FormulaItem.FormulaMilkSequence(defaultMilkQuantity = 1,
        defaultMilkTemperature = 1, defaultFoamTexture = 1,
        milkQuantity1 = 1, milkQuantity2 = 1, milkTemperature1 = 1, milkTemperature2 = 2,
        foamTexture1 = 1, foamTexture2 = 2))
}