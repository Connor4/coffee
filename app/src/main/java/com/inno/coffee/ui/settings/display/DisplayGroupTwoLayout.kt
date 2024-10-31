package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.utilities.DISPLAY_COLOR_BLUE
import com.inno.coffee.utilities.DISPLAY_COLOR_GREEN
import com.inno.coffee.utilities.DISPLAY_COLOR_MIX
import com.inno.coffee.utilities.DISPLAY_COLOR_RED
import com.inno.coffee.utilities.DISPLAY_PER_PAGE_COUNT_12
import com.inno.coffee.utilities.DISPLAY_PER_PAGE_COUNT_15
import com.inno.coffee.utilities.INDEX_AUTO_BACK_TO_FIRST_PAGE
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_COLOR
import com.inno.coffee.utilities.INDEX_NUMBER_OF_PRODUCT_PER_PAGE
import com.inno.coffee.utilities.INDEX_SCREEN_BRIGHTNESS
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayGroupTwoLayout(
    viewModel: DisplayViewModel = hiltViewModel(),
    onClickListSelect: (Int, String, Map<String, Any>) -> Unit,
) {
    val backToFirstPage = viewModel.backToFirstPage.collectAsState()
    val numberOfProductPerPage = viewModel.numberOfProductPerPage.collectAsState()
    val frontLightColor = viewModel.frontLightColor.collectAsState()
    val frontLightBrightness = viewModel.frontLightBrightness.collectAsState()
    val screenBrightness = viewModel.screenBrightness.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initGroupTwo()
    }
    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val red = stringResource(R.string.display_color_red)
    val green = stringResource(R.string.display_color_green)
    val blue = stringResource(R.string.display_color_blue)
    val mix = stringResource(R.string.display_color_mix)

    val backToFirstPageValue = if (backToFirstPage.value) on else off
    val lightColorValue = when (frontLightColor.value) {
        DISPLAY_COLOR_RED -> {
            red
        }
        DISPLAY_COLOR_GREEN -> {
            green
        }
        DISPLAY_COLOR_BLUE -> {
            blue
        }
        DISPLAY_COLOR_MIX -> {
            mix
        }
        else -> {
            mix
        }
    }

    Box {
        Column {
            DisplayItemLayout(stringResource(R.string.display_auto_back_to_first_page),
                backToFirstPageValue,
                Color(0xFF191A1D)) {
                val defaultValue = if (backToFirstPage.value) on else off
                val map = mapOf(Pair(on, true), Pair(off, false))
                onClickListSelect(INDEX_AUTO_BACK_TO_FIRST_PAGE, defaultValue, map)
            }
            DisplayItemLayout(stringResource(R.string.display_number_of_product_per_page),
                "${numberOfProductPerPage.value}",
                Color(0xFF2A2B2D)) {
                onClickListSelect(INDEX_NUMBER_OF_PRODUCT_PER_PAGE,
                    "${numberOfProductPerPage.value}",
                    mapOf(
                        Pair("$DISPLAY_PER_PAGE_COUNT_12", DISPLAY_PER_PAGE_COUNT_12),
                        Pair("$DISPLAY_PER_PAGE_COUNT_15", DISPLAY_PER_PAGE_COUNT_15)
                    ))
            }
            DisplayItemLayout(stringResource(R.string.display_front_light_color),
                lightColorValue,
                Color(0xFF191A1D)) {
                onClickListSelect(INDEX_FRONT_LIGHT_COLOR,
                    lightColorValue,
                    mapOf(
                        Pair(mix, DISPLAY_COLOR_MIX),
                        Pair(red, DISPLAY_COLOR_RED),
                        Pair(green, DISPLAY_COLOR_GREEN),
                        Pair(blue, DISPLAY_COLOR_BLUE),
                    ))
            }
            DisplayItemLayout(stringResource(R.string.display_front_light_brightness),
                "${frontLightBrightness.value}",
                Color(0xFF2A2B2D)) {
                onClickListSelect(INDEX_FRONT_LIGHT_BRIGHTNESS, "${frontLightBrightness.value}",
                    mapOf(
                        Pair("10", 10), Pair("20", 20), Pair("30", 30), Pair("40", 40),
                        Pair("50", 50), Pair("60", 60), Pair("70", 70), Pair("80", 80),
                        Pair("90", 90), Pair("100", 100),
                    )
                )
            }
            DisplayItemLayout(stringResource(R.string.display_screen_brightness),
                "${screenBrightness.value}",
                Color(0xFF191A1D)) {
                onClickListSelect(INDEX_SCREEN_BRIGHTNESS, "${screenBrightness.value}",
                    mapOf(
                        Pair("10", 10), Pair("20", 20), Pair("30", 30), Pair("40", 40),
                        Pair("50", 50), Pair("60", 60), Pair("70", 70), Pair("80", 80),
                        Pair("90", 90), Pair("100", 100),
                    )
                )
            }
        }


    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupTwo() {
//    DisplayGroupTwoLayout()
}