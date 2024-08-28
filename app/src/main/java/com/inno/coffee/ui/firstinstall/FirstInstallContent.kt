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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R
import com.inno.coffee.utilities.DEFAULT_SYSTEM_TIME
import com.inno.coffee.utilities.StateImage
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.draw9Patch
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.firstinstall.InstallViewModel
import com.inno.common.utils.Logger
import kotlinx.coroutines.delay
import java.util.Locale

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
            LanguagePage(modifier) { language ->
                selectedLanguage = language
                navController.navigate(DATE)
                viewModel.selectLanguage(context, language)
            }
        }
        composable(DATE) {
            DatePickerPage {
                selectedDateMillis = it
                navController.navigate(TIME)
            }
        }
        composable(TIME) {
            TimePickerPage { hour, min ->
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
private fun LanguagePage(modifier: Modifier = Modifier, onLanguagePick: (String) -> Unit) {
    val context = LocalContext.current
    val english = context.getString(R.string.first_install_language_English)
    val simplifiedChinese = context.getString(R.string.first_install_language_Chinese_simplified)
    val radioOptions = mapOf<String, String>(
        Pair(english, Locale.ENGLISH.language),
        Pair(simplifiedChinese, Locale.SIMPLIFIED_CHINESE.language)
    )

    val (selectedKey, setSelectedKey) = remember {
        mutableStateOf(english)
    }
    val (selectedValue, setSelectedValue) = remember {
        mutableStateOf(radioOptions[english]!!)
    }

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 66.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.first_install_title),
                fontSize = 15.nsp(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 217.dp)
                .background(color = Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .width(450.dp)
                    .height(450.dp)
                    .selectableGroup()
            ) {
                radioOptions.forEach {
                    LanguageRadioButton(text = it.key, isSelected = (it.key == selectedKey),
                        onClick = {
                            setSelectedKey(it.key)
                            setSelectedValue(it.value)
                        })
                }
            }
        }

        Button(
            onClick = {
//                onLanguagePick(selectedValue)
                Logger.d("SELECTED VALUE $selectedValue")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.first_install_next),
                fontSize = 7.nsp(),
                color = Color.White
            )
            Spacer(modifier = Modifier.width(13.dp))
            StateImage(
                normalImage = painterResource(id = R.drawable.install_language_next_normal_ic),
                pressedImage = painterResource(id = R.drawable.install_language_next_pressed_ic),
            )
        }
    }
}

@Composable
private fun LanguageRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .debouncedClickable({
                onClick()
            }),
    ) {
        if (isSelected) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .draw9Patch(LocalContext.current, R.drawable.install_select_bg))
        }
        Text(
            text = text,
            fontSize = 7.nsp(),
            color = Color.White,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerPage(onDatePick: (Long?) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier
            .width(1100.dp)
            .fillMaxHeight()) {
            val datePickerState =
                rememberDatePickerState(
                    initialSelectedDateMillis = (DEFAULT_SYSTEM_TIME + 86400000))
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 60.dp),
                onClick = {
                    onDatePick(datePickerState.selectedDateMillis)
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_confirm))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerPage(onSetComplete: (Int, Int) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ConstraintLayout(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            val (timePicker, cancelButton, confirmButton) = createRefs()

            val state = rememberTimePickerState()

            TimePicker(
                state = state,
                modifier = Modifier.constrainAs(timePicker) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Button(
                onClick = {

                },
                modifier = Modifier.constrainAs(cancelButton) {
                    top.linkTo(timePicker.bottom, margin = 16.dp)
                    end.linkTo(confirmButton.start, margin = 8.dp)
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_cancel))
            }

            Button(
                onClick = {
                    onSetComplete(state.hour, state.minute)
                },
                modifier = Modifier.constrainAs(confirmButton) {
                    top.linkTo(timePicker.bottom, margin = 16.dp)
                    end.linkTo(timePicker.end)
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_confirm))
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

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewInstallPage() {
    LanguagePage(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF191A1D))
    ) {}
//    TimePickerPage {}
//    DatePickerPage {}
//    SplashPage(navController = rememberNavController(), modifier = Modifier
//        .fillMaxSize()
//        .background(color = Color(0xFF191A1D))
//    )
}