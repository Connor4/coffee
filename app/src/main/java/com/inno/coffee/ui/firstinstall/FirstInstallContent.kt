package com.inno.coffee.ui.firstinstall

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R
import com.inno.coffee.utilities.DEFAULT_SYSTEM_TIME
import com.inno.coffee.viewmodel.firstinstall.InstallViewModel
import kotlinx.coroutines.delay

private const val SPLASH = "splash"
private const val LANGUAGE = "language"
private const val DATE = "date"
private const val TIME = "time"
private const val SPLASH_TIME = 3000L

@Composable
fun InstallSetting(
    onSetComplete: () -> Unit,
    viewModel: InstallViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF191A1D))
    val defaultLanguage = stringResource(id = R.string.first_install_language_English)
    var selectedLanguage by remember {
        mutableStateOf(defaultLanguage)
    }
    var selectedDateMillis by remember {
        mutableStateOf<Long?>(DEFAULT_SYSTEM_TIME)
    }
    var selectedHour by remember {
        mutableIntStateOf(0)
    }
    var selectedMin by remember {
        mutableIntStateOf(0)
    }
    NavHost(navController = navController, startDestination = SPLASH) {
        composable(SPLASH) {
            SplashPage(navController, modifier)
        }
        composable(LANGUAGE) {
            FirstLanguageLayout(modifier) { language ->
                selectedLanguage = language
                navController.navigate(DATE)
                viewModel.selectLanguage(context, language)
            }
        }
        composable(DATE) {
            DatePickerLayout(modifier) {
                selectedDateMillis = it
                navController.navigate(TIME)
            }
        }
        composable(TIME) {
            TimePickerLayout(modifier) { hour, min ->
                selectedHour = hour
                selectedMin = min
                viewModel.finishSetting(context, selectedDateMillis ?: DEFAULT_SYSTEM_TIME,
                    selectedHour, selectedMin, selectedLanguage)
                onSetComplete()
            }
        }
    }
}


@Composable
private fun SplashPage(navController: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(SPLASH_TIME)
        navController.navigate(LANGUAGE) {
            popUpTo(SPLASH) {
                inclusive = true
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = R.drawable.install_splash_logo_ic),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
        )

        Image(
            painter = painterResource(id = R.drawable.install_splash_loading_ic),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 615.dp, top = 601.dp, end = 615.dp, bottom = 149.dp)
                .size(50.dp)
                .graphicsLayer {
                    rotationZ = rotation
                },
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewInstallPage() {
//    SplashPage(navController = rememberNavController(), modifier = Modifier
//        .fillMaxSize()
//        .background(color = Color(0xFF191A1D))
//    )
}