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
import com.inno.coffee.utilities.DISPLAY_COLOR_OFF
import com.inno.coffee.utilities.DISPLAY_COLOR_ORANGE
import com.inno.coffee.utilities.DISPLAY_COLOR_PURPLE
import com.inno.coffee.utilities.DISPLAY_COLOR_RED
import com.inno.coffee.utilities.DISPLAY_COLOR_WHITE
import com.inno.coffee.utilities.DISPLAY_COLOR_YELLOW
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

    val no = stringResource(R.string.display_value_no)
    val yes = stringResource(R.string.display_value_yes)
    val red = stringResource(R.string.display_color_red)
    val green = stringResource(R.string.display_color_green)
    val blue = stringResource(R.string.display_color_blue)
    val mix = stringResource(R.string.display_color_mix)
    val white = stringResource(R.string.display_color_white)
    val yellow = stringResource(R.string.display_color_yellow)
    val orange = stringResource(R.string.display_color_orange)
    val purple = stringResource(R.string.display_color_purple)
    val colorOff = stringResource(R.string.display_color_off)
    val backToFirstString = stringResource(R.string.display_auto_back_to_first_page)
    val numberOfProductString = stringResource(R.string.display_number_of_product_per_page)
    val frontColorString = stringResource(R.string.display_front_light_color)

    val backToFirstPageValue = if (backToFirstPage) yes else no
    val lightColorValue = when (frontLightColor) {
        DISPLAY_COLOR_OFF -> {
            colorOff
        }
        DISPLAY_COLOR_WHITE -> {
            white
        }
        DISPLAY_COLOR_YELLOW -> {
            yellow
        }
        DISPLAY_COLOR_ORANGE -> {
            orange
        }
        DISPLAY_COLOR_PURPLE -> {
            purple
        }
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
                val map = mapOf(Pair(yes, true), Pair(no, false))
                onClickListSelect(backToFirstString, INDEX_AUTO_BACK_TO_FIRST_PAGE,
                    backToFirstPageValue, map)
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
                        Pair(colorOff, DISPLAY_COLOR_OFF),
                        Pair(white, DISPLAY_COLOR_WHITE),
                        Pair(purple, DISPLAY_COLOR_PURPLE),
                        Pair(blue, DISPLAY_COLOR_BLUE),
                        Pair(green, DISPLAY_COLOR_GREEN),
                        Pair(yellow, DISPLAY_COLOR_YELLOW),
                        Pair(orange, DISPLAY_COLOR_ORANGE),
                        Pair(red, DISPLAY_COLOR_RED),
                        Pair(mix, DISPLAY_COLOR_MIX),
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