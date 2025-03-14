package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.MaskBoxWithContent
import com.inno.coffee.ui.common.debouncedClickAndLongClickWithoutRipple
import com.inno.coffee.utilities.getImageResId
import com.inno.coffee.utilities.getStringResId
import com.inno.coffee.utilities.nsp
import com.inno.coffee.utilities.previewFormula
import com.inno.common.db.entity.Formula

@Composable
fun DrinkItem(
    modifier: Modifier = Modifier,
    formula: Formula,
    enableMask: Boolean = false,
    selected: Boolean = false,
    showProductName: Boolean = true,
    showProductPrice: Boolean = false,
    normalSize: Boolean = true,
    processing: Boolean = false,
    onDrinkClick: () -> Unit = {},
    onDrinkLongClick: () -> Unit = {},
) {
    val totalWidth: Int
    val totalHeight: Int
    val insideBoxWidth: Int
    val insideBoxHeight: Int
    val imageWidth: Int
    val imageHeight: Int
    val textSize: Int
    if (normalSize) {
        totalWidth = 300
        totalHeight = 200
        insideBoxWidth = 276
        insideBoxHeight = 174
        imageWidth = 80
        imageHeight = 60
        textSize = 6
    } else {
        totalWidth = 240
        totalHeight = 200
        insideBoxWidth = 220
        insideBoxHeight = 176
        imageWidth = 70
        imageHeight = 50
        textSize = 5
    }

    Box(
        modifier = modifier
            .width(totalWidth.dp)
            .height(totalHeight.dp)
            .debouncedClickAndLongClickWithoutRipple(
                enabled = !enableMask,
                onClick = {
                    onDrinkClick()
                },
                onLongClick = {
                    onDrinkLongClick()
                }),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(painter = painterResource(id = R.drawable.home_item_select_bg),
                contentScale = ContentScale.FillBounds,
                contentDescription = null)
        }

        Box(
            modifier = Modifier
                .width(insideBoxWidth.dp)
                .height(insideBoxHeight.dp)
                .border(1.dp, Color(0xFF484848), RoundedCornerShape(17.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(color = Color(0xFF191A1D)),
        ) {
            MaskBoxWithContent(enableMask = enableMask) {
                val drawableRes = formula.imageRes.ifEmpty { "drink_item_empty_ic" }
                val productPrice = formula.productPrice?.price ?: ""
                if (showProductPrice) {
                    Text(
                        text = "$productPrice",
                        fontSize = textSize.nsp(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .padding(top = 22.dp, end = 26.dp)
                    )
                }
                Image(
                    painter = painterResource(id = getImageResId(drawableRes)),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .width(imageWidth.dp)
                        .height(imageHeight.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 41.dp),
                )
                if (processing) {
                    Image(
                        painter = painterResource(R.drawable.drink_item_cancel_ic),
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .width(imageWidth.dp)
                            .height(imageHeight.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = 41.dp),
                    )
                }
                if (showProductName) {
                    val name = if (!formula.productName?.nameRes.isNullOrBlank()) {
                        stringResource(getStringResId(formula.productName?.nameRes!!))
                    } else if (!formula.productName?.name.isNullOrBlank()) {
                        formula.productName?.name
                    } else {
                        stringResource(R.string.common_empty_string)
                    }
                    Text(
                        text = name!!,
                        fontSize = textSize.nsp(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .offset(y = 124.dp)
                    )
                }
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDrink() {
    DrinkItem(formula = previewFormula)
}