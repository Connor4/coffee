package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.FormulaItem


@Composable
fun FormulaItem(
    backgroundColor: Color,
    selected: Boolean,
    description: String,
    value: Any?,
    onClick: () -> Unit = {},
) {
    if (value == "") {
        return
    }
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
                is FormulaItem.FormulaUnitValue -> {
                    textValue = value.value.toString()
                    Text(
                        text = value.unit, fontSize = 5.nsp(), color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 385.dp)
                    )
                }
                is FormulaItem.FormulaProductType -> {
                    textValue = value.type
                }
                is FormulaItem.FormulaProductName -> {
                    textValue = value.name
                }
                is FormulaItem.FormulaVatPosition -> {
                    textValue = if (value.position) stringResource(R.string.formula_font_vat)
                    else stringResource(R.string.formula_back_vat)
                }
                is FormulaItem.FormulaAmericanoSeq -> {
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