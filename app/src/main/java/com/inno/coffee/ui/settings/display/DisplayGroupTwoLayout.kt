package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun DisplayGroupTwoLayout(
    backToFirstPage: Boolean,
    numberOfProductPerPage: Int,
    frontLightColor: Int,
    frontLightBrightness: Int,
    screenBrightness: Int,
    onClickListSelect: (String, Int, String, Map<String, Any>) -> Unit,
    onClickScroll: (Int, Int) -> Unit,
) {

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val red = stringResource(R.string.display_color_red)
    val green = stringResource(R.string.display_color_green)
    val blue = stringResource(R.string.display_color_blue)
    val mix = stringResource(R.string.display_color_mix)
    val backToFirstString = stringResource(R.string.display_auto_back_to_first_page)
    val numberOfProductString = stringResource(R.string.display_number_of_product_per_page)
    val frontColorString = stringResource(R.string.display_front_light_color)

    val backToFirstPageValue = if (backToFirstPage) on else off
    val lightColorValue = when (frontLightColor) {
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
            DisplayItemLayout(backToFirstString,
                backToFirstPageValue,
                Color(0xFF191A1D)
            ) {
                val defaultValue = if (backToFirstPage) on else off
                val map = mapOf(Pair(on, true), Pair(off, false))
                onClickListSelect(backToFirstString, INDEX_AUTO_BACK_TO_FIRST_PAGE, defaultValue,
                    map)
            }
            DisplayItemLayout(numberOfProductString,
                "$numberOfProductPerPage",
                Color(0xFF2A2B2D)
            ) {
                onClickListSelect(numberOfProductString, INDEX_NUMBER_OF_PRODUCT_PER_PAGE,
                    "$numberOfProductPerPage",
                    mapOf(
                        Pair("$DISPLAY_PER_PAGE_COUNT_12", DISPLAY_PER_PAGE_COUNT_12),
                        Pair("$DISPLAY_PER_PAGE_COUNT_15", DISPLAY_PER_PAGE_COUNT_15)
                    )
                )
            }
            DisplayItemLayout(frontColorString,
                lightColorValue,
                Color(0xFF191A1D)
            ) {
                onClickListSelect(frontColorString, INDEX_FRONT_LIGHT_COLOR, lightColorValue,
                    mapOf(
                        Pair(mix, DISPLAY_COLOR_MIX),
                        Pair(red, DISPLAY_COLOR_RED),
                        Pair(green, DISPLAY_COLOR_GREEN),
                        Pair(blue, DISPLAY_COLOR_BLUE),
                    )
                )
            }
            DisplayItemLayout(stringResource(R.string.display_front_light_brightness),
                "$frontLightBrightness",
                Color(0xFF2A2B2D)
            ) {
                onClickScroll(INDEX_FRONT_LIGHT_BRIGHTNESS, frontLightBrightness)
            }
            DisplayItemLayout(stringResource(R.string.display_screen_brightness),
                "$screenBrightness",
                Color(0xFF191A1D)
            ) {
                onClickScroll(INDEX_SCREEN_BRIGHTNESS, screenBrightness)
            }
        }


    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupTwo() {
//    DisplayGroupTwoLayout()
}