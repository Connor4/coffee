package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.MaskBoxWithContent
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.getImageResId
import com.inno.coffee.utilities.getStringResId
import com.inno.coffee.utilities.nsp
import com.inno.coffee.utilities.previewFormula
import com.inno.common.db.entity.Formula

@Composable
fun DrinkItem(
    modifier: Modifier = Modifier,
    model: Formula,
    enableMask: Boolean = false,
    selected: Boolean = false,
    showProductName: Boolean = true,
    normalSize: Boolean = true,
    onDrinkClick: () -> Unit = {},
) {
    val totalWidth: Int
    val totalHeight: Int
    val insideBoxWidth: Int
    val insideBoxHeight: Int
    val imageWidth: Int
    val imageHeight: Int
    val textMarginTop: Int
    val textSize: Int
    if (normalSize) {
        totalWidth = 300
        totalHeight = 200
        insideBoxWidth = 274
        insideBoxHeight = 174
        imageWidth = 80
        imageHeight = 60
        textMarginTop = 124
        textSize = 6
    } else {
        totalWidth = 240
        totalHeight = 200
        insideBoxWidth = 214
        insideBoxHeight = 174
        imageWidth = 70
        imageHeight = 50
        textMarginTop = 120
        textSize = 5
    }

    Box(
        modifier = modifier
            .width(totalWidth.dp)
            .height(totalHeight.dp)
            .debouncedClickable({ onDrinkClick() }, enabled = !enableMask),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(painter = painterResource(id = R.drawable.home_item_select_bg),
                contentDescription = null)
        }

        Box(
            modifier = Modifier
                .width(insideBoxWidth.dp)
                .height(insideBoxHeight.dp)
                .border(1.dp, Color(0xFF484848), RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(color = Color(0xFF191A1D)),
        ) {
            MaskBoxWithContent(enableMask = enableMask) {
                val drawableRes = model.imageRes ?: "drink_item_empty_ic"
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
                if (showProductName) {
                    val name = if (!model.productName?.nameRes.isNullOrBlank()) {
                        stringResource(getStringResId(model.productName?.nameRes!!))
                    } else if (!model.productName?.name.isNullOrBlank()) {
                        model.productName?.name
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
                            .offset(y = textMarginTop.dp)
                    )
                }
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240", showBackground = true)
@Composable
private fun PreviewDrink() {
    DrinkItem(model = previewFormula)
}