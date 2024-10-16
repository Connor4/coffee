package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton

@Composable
fun FormulaFunctionButton(
    showText: Int,
    onLearn: () -> Unit,
    onProductTest: () -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 770.dp, top = 172.dp, end = 90.dp)
    ) {
        Row {
            if (showText != -1) {
                ChangeColorButton(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    text = if (showText == 1) stringResource(id = R.string.formula_learn_quantity)
                    else stringResource(id = R.string.formula_learn_quantity),
                ) {
                    onLearn()
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
    FormulaFunctionButton(1, {}, {})
}