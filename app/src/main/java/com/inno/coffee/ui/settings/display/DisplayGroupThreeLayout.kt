package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.inno.coffee.R
import com.inno.coffee.utilities.INDEX_SHOW_EXTRACTION_TIME
import com.inno.coffee.utilities.INDEX_SHOW_PRODUCT_NAME
import com.inno.coffee.utilities.INDEX_SHOW_PRODUCT_PRICE

@Composable
fun DisplayGroupThreeLayout(
    showExtractionTime: Boolean,
    showProductName: Boolean,
    showProductPrice: Boolean,
    onClickListSelect: (String, Int, String, Map<String, Any>) -> Unit,
) {
    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val extractionNameString = stringResource(R.string.display_show_extract_time)
    val showProductPriceString = stringResource(R.string.display_show_product_price)
    val showProductNameString = stringResource(R.string.display_show_product_name)
    val showExtractionTimeValue = if (showExtractionTime) on else off
    val showProductPriceValue = if (showProductPrice) on else off
    val showProductNameValue = if (showProductName) on else off

    Column {
        DisplayItemLayout(extractionNameString,
            showExtractionTimeValue,
            Color(0xFF191A1D)
        ) {
            val defaultValue = if (showExtractionTime) on else off
            val map = mapOf(Pair(on, true), Pair(off, false))
            onClickListSelect(extractionNameString, INDEX_SHOW_EXTRACTION_TIME, defaultValue, map)
        }
        DisplayItemLayout(showProductPriceString,
            showProductPriceValue,
            Color(0xFF2A2B2D)
        ) {
            val defaultValue = if (showProductPrice) on else off
            val map = mapOf(Pair(on, true), Pair(off, false))
            onClickListSelect(showProductPriceString, INDEX_SHOW_PRODUCT_PRICE, defaultValue, map)
        }
        DisplayItemLayout(showProductNameString,
            showProductNameValue,
            Color(0xFF191A1D)
        ) {
            val defaultValue = if (showProductName) on else off
            val map = mapOf(Pair(on, true), Pair(off, false))
            onClickListSelect(showProductNameString, INDEX_SHOW_PRODUCT_NAME, defaultValue, map)
        }
    }
}
