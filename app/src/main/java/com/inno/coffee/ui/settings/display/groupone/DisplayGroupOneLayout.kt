package com.inno.coffee.ui.settings.display.groupone

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.DISPLAY_SETTING_KEY
import com.inno.coffee.utilities.DISPLAY_SETTING_LANGUAGE
import com.inno.coffee.utilities.DISPLAY_SETTING_TIME
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayGroupOneLayout(
    viewModel: DisplayViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val language = viewModel.language.collectAsState()
    val time = viewModel.time.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initGroupOne()
    }

    Column {
        DisplayItemLayout(stringResource(R.string.display_language), language.value,
            Color(0xFF191A1D)) {
            ScreenDisplayManager.autoRoute(context, DisplaySettingActivity::class.java,
                Bundle().apply {
                    putString(DISPLAY_SETTING_KEY, DISPLAY_SETTING_LANGUAGE)
                })
        }
        DisplayItemLayout(stringResource(R.string.display_date_and_time), time.value,
            Color(0xFF2A2B2D)) {
            ScreenDisplayManager.autoRoute(context, DisplaySettingActivity::class.java,
                Bundle().apply {
                    putString(DISPLAY_SETTING_KEY, DISPLAY_SETTING_TIME)
                })
        }
        DisplayItemLayout(stringResource(R.string.display_screen_style), "Monochrome",
            Color(0xFF191A1D)) {

        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupOne() {
    DisplayGroupOneLayout()
}