package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
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

    val showExtractionTimeValue =
        if (showExtractionTime.value) stringResource(R.string.display_value_on)
        else stringResource(R.string.display_value_off)
    val showProductNameValue = if (showProductName.value) stringResource(R.string.display_value_on)
    else stringResource(R.string.display_value_off)

    Column {
        DisplayItemLayout(stringResource(R.string.display_show_extract_time),
            showExtractionTimeValue,
            Color(0xFF191A1D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_show_product_name),
            showProductNameValue,
            Color(0xFF2A2B2D)) {
        }
    }
}
