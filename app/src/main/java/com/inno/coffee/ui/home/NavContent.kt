package com.inno.coffee.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.data.PreferencesManager
import com.inno.coffee.ui.theme.CoffeeTheme

const val INSTALL_SETTING = "install_setting"
const val DRINK_PAGE = "drink_page"

@Composable
fun NavContent() {
    val navController = rememberNavController()
    val preferencesManager = PreferencesManager.getInstance(LocalContext.current)
    var isFirstLaunch by remember {
        mutableStateOf(preferencesManager.isFirstLaunch)
    }

    if (isFirstLaunch) {
        NavHost(navController = navController, startDestination = INSTALL_SETTING) {
            composable(INSTALL_SETTING) {
                InstallSetting {
                    preferencesManager.isFirstLaunch = false
                    isFirstLaunch = false
                    navController.navigate(DRINK_PAGE) {
                        popUpTo(INSTALL_SETTING) { inclusive = true }
                    }
                }
            }
            composable(DRINK_PAGE) {
                DrinkPage()
            }
        }
    } else {
        NavHost(navController = navController, startDestination = DRINK_PAGE) {
            composable(DRINK_PAGE) {
                DrinkPage()
            }
        }
    }
}

@Composable
fun InstallSetting(onSetComplete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Settings Page")
        Button(onClick = onSetComplete) {
            Text(text = "Complete Settings")
        }
    }
}

@Composable
fun DrinkPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(style = TextStyle(fontSize = 20.sp), text = "Main Page")
        Button(onClick = { }) {
            Text(text = "Main Page")
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    CoffeeTheme {
        InstallSetting {

        }
    }
}