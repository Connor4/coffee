package com.inno.coffee.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

/**
 * 饮品制作页
 */
class MakeCoffeeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeTheme {
                NavPage()
            }
        }
    }

}

const val FIRST_SETTING = "first_launch"
const val MAIN_SCREEN = "main_screen"

@Composable
fun NavPage() {
    val navController = rememberNavController()
    val preferencesManager = PreferencesManager.getInstance(LocalContext.current)
    var isFirstLaunch by remember {
        mutableStateOf(preferencesManager.isFirstLaunch)
    }
    if (isFirstLaunch) {
        NavHost(navController = navController, startDestination = FIRST_SETTING) {
            composable(FIRST_SETTING) {
                SettingsScreen {
                    preferencesManager.isFirstLaunch = false
                    isFirstLaunch = false
                    navController.navigate(MAIN_SCREEN) {
                        popUpTo(FIRST_SETTING) { inclusive = true }
                    }
                }
            }
            composable(MAIN_SCREEN) { MainScreen() }
        }
    } else {
        NavHost(navController = navController, startDestination = MAIN_SCREEN) {
            composable(MAIN_SCREEN) { MainScreen() }
        }
    }

}

@Composable
fun SettingsScreen(onSettingsComplete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "Settings Page")
        Button(onClick = onSettingsComplete) {
            Text(text = "Complete Settings")
        }
    }
}

@Composable
fun MainScreen() {
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
fun NavPagePreview() {
    CoffeeTheme {
        MainScreen()
    }
}