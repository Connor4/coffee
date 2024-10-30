package com.inno.coffee.ui.settings.display.groupone

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
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
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayGroupOneLayout(
    viewModel: DisplayViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val language = viewModel.language.collectAsState()
    val time = viewModel.time.collectAsState()

    val radioOptions = mapOf(
        Pair(FIRST_INSTALL_KEY_ENGLISH, FIRST_INSTALL_VALUE_ENGLISH),
        Pair(FIRST_INSTALL_KEY_SIMPLIFIED_CHINESE, FIRST_INSTALL_VALUE_SIMPLIFIED_CHINESE),
        Pair(FIRST_INSTALL_KEY_TRADITIONAL_CHINESE, FIRST_INSTALL_VALUE_TRADITIONAL_CHINESE),
        Pair(FIRST_INSTALL_KEY_JAPANESE, FIRST_INSTALL_VALUE_JAPANESE),
        Pair(FIRST_INSTALL_KEY_KOREAN, FIRST_INSTALL_VALUE_KOREAN),
        Pair(FIRST_INSTALL_KEY_FRENCH, FIRST_INSTALL_VALUE_FRENCH)
    )

    LaunchedEffect(Unit) {
        viewModel.initGroupOne()
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.initGroupOne()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column {
        DisplayItemLayout(stringResource(R.string.display_language),
            radioOptions[language.value] ?: FIRST_INSTALL_VALUE_ENGLISH,
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
//        DisplayItemLayout(stringResource(R.string.display_screen_style), "Monochrome",
//            Color(0xFF191A1D)) {
//
//        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupOne() {
    DisplayGroupOneLayout()
}