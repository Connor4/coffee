package com.inno.coffee.ui.settings.display.grouptwo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayGroupTwoLayout(
    viewModel: DisplayViewModel = hiltViewModel(),
) {
    val backToFirstPage = viewModel.backToFirstPage.collectAsState()
    val numberOfProductPerPage = viewModel.numberOfProductPerPage.collectAsState()
    val frontLightColor = viewModel.frontLightColor.collectAsState()
    val frontLightBrightness = viewModel.frontLightBrightness.collectAsState()
    val screenBrightness = viewModel.screenBrightness.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initGroupTwo()
    }

    val backToFirstPageValue = if (backToFirstPage.value) stringResource(R.string.display_value_on)
    else stringResource(R.string.display_value_off)

    Column {
        DisplayItemLayout(stringResource(R.string.display_auto_back_to_first_page),
            backToFirstPageValue,
            Color(0xFF191A1D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_number_of_product_per_page),
            "${numberOfProductPerPage.value}",
            Color(0xFF2A2B2D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_front_light_color),
            frontLightColor.value,
            Color(0xFF191A1D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_front_light_brightness),
            "${frontLightBrightness.value}",
            Color(0xFF2A2B2D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_screen_brightness),
            "${screenBrightness.value}",
            Color(0xFF191A1D)) {
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupTwo() {
    DisplayGroupTwoLayout()
}