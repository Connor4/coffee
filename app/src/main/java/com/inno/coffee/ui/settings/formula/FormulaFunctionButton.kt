package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton

@Composable
fun FormulaFunctionButton(

) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(start = 450.dp, top = 330.dp)
        ) {
            ChangeColorButton(
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.formula_assimilation_key),
            )
        }


        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 172.dp, end = 90.dp)
        ) {
            Row {
                ChangeColorButton(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    text = stringResource(id = R.string.formula_product_test),
                ) {
//                    viewModel.loadFromSdCard(context)
                }
                Spacer(modifier = Modifier.width(20.dp))
                ChangeColorButton(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    text = stringResource(id = R.string.formula_learn_quantity),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFunctionButton() {
    FormulaFunctionButton()
}