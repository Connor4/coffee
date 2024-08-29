package com.inno.coffee.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.utilities.MaskBoxWithContent
import com.inno.coffee.utilities.draw9Patch
import com.inno.common.enums.ProductType

@Composable
private fun DrinkItem(
    model: DrinksModel,
    enableMask: Boolean = false,
    selected: Boolean = false,
    onDrinkClick: (model: DrinksModel) -> Unit = {},
) {
//    if (selected) {
    Box(modifier = Modifier
        .width(280.dp)
        .height(180.dp)
        .draw9Patch(LocalContext.current, R.drawable.common_select_bg))
//    }
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .background(color = Color(0xFF191A1D))
    ) {
        MaskBoxWithContent(enableMask = enableMask) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color(0xFF484848))
                    .clip(RoundedCornerShape(10.dp))
            ) {

            }
        }
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewDrink() {
    DrinkItem(model = DrinksModel(1, ProductType.COFFEE, R.string.home_item_foam,
        R.drawable.coffee1))
}