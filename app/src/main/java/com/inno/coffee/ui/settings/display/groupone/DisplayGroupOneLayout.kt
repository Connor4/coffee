package com.inno.coffee.ui.settings.display.groupone

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.inno.coffee.R
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.DISPLAY_SETTING_KEY
import com.inno.coffee.utilities.DISPLAY_SETTING_LANGUAGE
import com.inno.coffee.utilities.DISPLAY_SETTING_TIME
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_ENGLISH
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_FRENCH
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_JAPANESE
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_KOREAN
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_SIMPLIFIED_CHINESE
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_TRADITIONAL_CHINESE
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_ENGLISH
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_FRENCH
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_JAPANESE
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_KOREAN
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_SIMPLIFIED_CHINESE
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_TRADITIONAL_CHINESE

@Composable
fun DisplayGroupOneLayout(
    language: String,
    time: String,
    onClickItem: (String, String) -> Unit,
) {
    val radioOptions = mapOf(
        Pair(FIRST_INSTALL_KEY_ENGLISH, FIRST_INSTALL_VALUE_ENGLISH),
        Pair(FIRST_INSTALL_KEY_SIMPLIFIED_CHINESE, FIRST_INSTALL_VALUE_SIMPLIFIED_CHINESE),
        Pair(FIRST_INSTALL_KEY_TRADITIONAL_CHINESE, FIRST_INSTALL_VALUE_TRADITIONAL_CHINESE),
        Pair(FIRST_INSTALL_KEY_JAPANESE, FIRST_INSTALL_VALUE_JAPANESE),
        Pair(FIRST_INSTALL_KEY_KOREAN, FIRST_INSTALL_VALUE_KOREAN),
        Pair(FIRST_INSTALL_KEY_FRENCH, FIRST_INSTALL_VALUE_FRENCH)
    )

    Column {
        DisplayItemLayout(stringResource(R.string.display_language),
            radioOptions[language] ?: FIRST_INSTALL_VALUE_ENGLISH,
            Color(0xFF191A1D)
        ) {
            onClickItem(DISPLAY_SETTING_KEY, DISPLAY_SETTING_LANGUAGE)
        }
        DisplayItemLayout(stringResource(R.string.display_date_and_time), time,
            Color(0xFF2A2B2D)
        ) {
            onClickItem(DISPLAY_SETTING_KEY, DISPLAY_SETTING_TIME)
        }
//        DisplayItemLayout(stringResource(R.string.display_screen_style), "Monochrome",
//            Color(0xFF191A1D)) {
//
//        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupOne() {
    DisplayGroupOneLayout(FIRST_INSTALL_VALUE_ENGLISH, "12:00") { _, _ ->
    }
}