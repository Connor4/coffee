package com.inno.coffee.ui.settings.formula.formulatype

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.draw9Patch
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.FormulaItem

@Composable
fun FormulaPressWeightLayout(
    value: FormulaItem.FormulaPressWeight,
    onTypeChange: (FormulaItem.FormulaPressWeight) -> Unit,
    onCloseClick: () -> Unit,
) {
    val radioOptions = mapOf(
        Pair(1, 20),
        Pair(2, 40),
        Pair(3, 60),
    )

    val selectedKey = remember {
        val position = if (value.weight.toInt() == 20) 1
        else if (value.weight.toInt() == 40) 2
        else if (value.weight.toInt() == 60) 3
        else 1
        mutableStateOf(position)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.common_midsize_dialog_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(834.dp)
                .height(660.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(571.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF191A1D))
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 25.dp, end = 22.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 50.dp)
                    .background(color = Color.Transparent),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .width(450.dp)
                        .height(450.dp)
                        .verticalScroll(rememberScrollState())
                        .selectableGroup()
                ) {
                    radioOptions.forEach {
                        WeightRadioButton(text = "${it.value} [kg]",
                            isSelected = (it.key == selectedKey.value),
                            onClick = {
                                value.weight = it.value.toShort()
                                onTypeChange(value)
                            })
                    }
                }
            }
        }
    }
}


@Composable
private fun WeightRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .debouncedClickable({
                onClick()
            }),
    ) {
        if (isSelected) {
            Box(modifier = Modifier
                .fillMaxSize()
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
        }
        Text(
            text = text,
            fontSize = 7.nsp(),
            color = Color.White,
        )
    }
}
