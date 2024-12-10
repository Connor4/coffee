package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.inno.coffee.R
import com.inno.coffee.utilities.DISPLAY_NO_GRINDER
import com.inno.coffee.utilities.DISPLAY_PROTECT_GRINDER
import com.inno.coffee.utilities.DISPLAY_YSE_GRINDER
import com.inno.coffee.utilities.INDEX_SHOW_EXTRACTION_TIME
import com.inno.coffee.utilities.INDEX_SHOW_GRINDER_BUTTON
import com.inno.coffee.utilities.INDEX_SHOW_PRODUCT_PRICE

@Composable
fun DisplayGroupThreeLayout(
    showExtractionTime: Boolean,
    showProductName: Boolean,
    showProductPrice: Boolean,
    showGrinderButton: Int,
    onClickListSelect: (String, Int, String, Map<String, Any>) -> Unit,
) {
    val no = stringResource(R.string.display_value_no)
    val yes = stringResource(R.string.display_value_yes)
    val protectedString = stringResource(R.string.display_show_protected)
    val extractionNameString = stringResource(R.string.display_show_extract_time)
    val showProductPriceString = stringResource(R.string.display_show_product_price)
//    val showProductNameString = stringResource(R.string.display_show_product_name)
    val showGrinderString = stringResource(R.string.display_show_grinder_button)
    val showExtractionTimeValue = if (showExtractionTime) yes else no
    val showProductPriceValue = if (showProductPrice) yes else no
//    val showProductNameValue = if (showProductName) on else off
    val showGrinderButtonValue = when (showGrinderButton) {
        DISPLAY_NO_GRINDER -> {
            no
        }
        DISPLAY_YSE_GRINDER -> {
            yes
        }
        DISPLAY_PROTECT_GRINDER -> {
            protectedString
        }
        else -> {
            no
        }
    }

    Column {
        DisplayItemLayout(extractionNameString,
            showExtractionTimeValue,
            Color(0xFF191A1D)
        ) {
            val defaultValue = if (showExtractionTime) yes else no
            val map = mapOf(Pair(yes, true), Pair(no, false))
            onClickListSelect(extractionNameString, INDEX_SHOW_EXTRACTION_TIME, defaultValue, map)
        }
        DisplayItemLayout(showProductPriceString,
            showProductPriceValue,
            Color(0xFF2A2B2D)
        ) {
            val defaultValue = if (showProductPrice) yes else no
            val map = mapOf(Pair(yes, true), Pair(no, false))
            onClickListSelect(showProductPriceString, INDEX_SHOW_PRODUCT_PRICE, defaultValue, map)
        }
        DisplayItemLayout(showGrinderString,
            showGrinderButtonValue,
            Color(0xFF191A1D)
        ) {
            val map = mapOf(Pair(no, DISPLAY_NO_GRINDER), Pair(yes, DISPLAY_YSE_GRINDER),
                Pair(protectedString, DISPLAY_PROTECT_GRINDER))
            onClickListSelect(showGrinderString, INDEX_SHOW_GRINDER_BUTTON, showGrinderButtonValue,
                map)
        }
//        DisplayItemLayout(showProductNameString,
//            showProductNameValue,
//            Color(0xFF191A1D)
//        ) {
//            val defaultValue = if (showProductName) on else off
//            val map = mapOf(Pair(on, true), Pair(off, false))
//            onClickListSelect(showProductNameString, INDEX_SHOW_PRODUCT_NAME, defaultValue, map)
//        }
    }
}
