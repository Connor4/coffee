package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.utilities.FORMULA_PROPERTY_COFFEE_WATER
import com.inno.coffee.utilities.FORMULA_PROPERTY_POWDER_DOSAGE
import com.inno.coffee.utilities.FORMULA_SHOW_LEARN_WATER
import com.inno.coffee.utilities.FORMULA_SHOW_POWDER_TEST

@Composable
fun FormulaFunctionButton(
    selectedName: String,
    onLearn: (Int) -> Unit,
    onProductTest: () -> Unit,
) {
    var index = -1
    val showText = when (selectedName) {
        FORMULA_PROPERTY_COFFEE_WATER -> {
            index = FORMULA_SHOW_LEARN_WATER
            stringResource(id = R.string.formula_learn_quantity)
        }
        FORMULA_PROPERTY_POWDER_DOSAGE -> {
            index = FORMULA_SHOW_POWDER_TEST
            stringResource(id = R.string.formula_powder_test)
        }
        else -> ""
    }
    val text = remember {
        mutableStateOf(showText)
    }

    LaunchedEffect(showText) {
        text.value = showText
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 172.dp, end = 90.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row {
            if (text.value.isNotEmpty()) {
                ChangeColorButton(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    text = text.value
                ) {
                    onLearn(index)
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            ChangeColorButton(
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.formula_product_test),
            ) {
                onProductTest()
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFunctionButton() {
    FormulaFunctionButton("coffeeWater", {}, {})
}