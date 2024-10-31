package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.utilities.INDEX_SHOW_EXTRACTION_TIME
import com.inno.coffee.utilities.INDEX_SHOW_PRODUCT_NAME
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayGroupThreeLayout(
    viewModel: DisplayViewModel = hiltViewModel(),
    onClickListSelect: (Int, String, Map<String, Any>) -> Unit,
) {
    val showExtractionTime = viewModel.showExtractionTime.collectAsState()
    val showProductName = viewModel.showProductName.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initGroupThree()
    }
    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val showExtractionTimeValue = if (showExtractionTime.value) on else off
    val showProductNameValue = if (showProductName.value) on else off

    Column {
        DisplayItemLayout(stringResource(R.string.display_show_extract_time),
            showExtractionTimeValue,
            Color(0xFF191A1D)) {
            val defaultValue = if (showExtractionTime.value) on else off
            val map = mapOf(Pair(on, true), Pair(off, false))
            onClickListSelect(INDEX_SHOW_EXTRACTION_TIME, defaultValue, map)
        }
        DisplayItemLayout(stringResource(R.string.display_show_product_name),
            showProductNameValue,
            Color(0xFF2A2B2D)) {
            val defaultValue = if (showProductName.value) on else off
            val map = mapOf(Pair(on, true), Pair(off, false))
            onClickListSelect(INDEX_SHOW_PRODUCT_NAME, defaultValue, map)
        }
    }
}
