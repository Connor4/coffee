package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.utilities.MaskBoxWithContent
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.draw9Patch
import com.inno.coffee.utilities.nsp
import com.inno.common.enums.ProductType

@Composable
private fun DrinkItem(
    model: DrinksModel,
    enableMask: Boolean = false,
    selected: Boolean = false,
    onDrinkClick: (model: DrinksModel) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .width(287.dp)
            .height(180.dp)
            .debouncedClickable({ onDrinkClick(model) }),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(modifier = Modifier
                .fillMaxSize()
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
        }
        Box(
            modifier = Modifier
                .width(262.dp)
                .height(154.dp)
                .border(1.dp, Color(0xFF484848), RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color(0xFF191A1D)),
        ) {
            MaskBoxWithContent(enableMask = enableMask) {
//                    AsyncImage(
//                        model = model.imageRes,
//                        contentDescription = null,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .size(120.dp),
//                    )
                Image(
                    painter = painterResource(id = model.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 35.dp),
                )
                Text(
                    text = stringResource(id = model.name),
                    fontSize = 6.nsp(),
                    color = Color.White,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(y = 105.dp)
                )
            }
        }
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewDrink() {
    DrinkItem(model = DrinksModel(1, ProductType.COFFEE, R.string.home_item_foam,
        R.drawable.drink_latte_ic))
}