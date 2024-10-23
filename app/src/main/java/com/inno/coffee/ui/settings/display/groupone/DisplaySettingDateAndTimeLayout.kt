package com.inno.coffee.ui.settings.display.groupone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.utilities.DATE
import com.inno.coffee.utilities.DEFAULT_SYSTEM_TIME
import com.inno.coffee.utilities.TIME
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel


@Composable
fun DisplaySettingDateAndTimeLayout(
    viewModel: DisplayViewModel,
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    var selectedDateMillis by remember {
        mutableStateOf<Long?>(DEFAULT_SYSTEM_TIME)
    }
    var selectedHour by remember {
        mutableIntStateOf(0)
    }
    var selectedMin by remember {
        mutableIntStateOf(0)
    }

    NavHost(navController = navController, startDestination = DATE) {
        composable(DATE) {
            DisplaySettingDateLayout({
                selectedDateMillis = it
                navController.navigate(TIME)
            }, {
                onCloseClick()
            })
        }
        composable(TIME) {
            DisplaySettingTimeLayout({ hour, min ->
                selectedHour = hour
                selectedMin = min
                viewModel.setSystemTime(context, selectedDateMillis ?: DEFAULT_SYSTEM_TIME,
                    selectedHour, selectedMin)
                onCloseClick()
            }, {
                navController.popBackStack()
            }, {
                onCloseClick()
            })
        }
    }
}

@Preview
@Composable
private fun PreviewDisplaySettingDateAndTime() {
//    DisplaySettingDateAndTimeLayout()
}