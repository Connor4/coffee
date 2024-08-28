package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import com.inno.coffee.viewmodel.firstinstall.InstallViewModel
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
            SplashPage(navController)
        }
        composable(LANGUAGE) {
            LanguagePage { language ->
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
private fun LanguagePage(onLanguagePick: (String) -> Unit) {
    val context = LocalContext.current
    val english = context.getString(R.string.first_install_language_English)
    val simplifiedChinese = context.getString(R.string.first_install_language_Chinese)
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier
            .width(500.dp)
            .align(Alignment.Center)
            .background(color = Color.LightGray)
            .selectableGroup()) {
            radioOptions.forEach {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (it.key == selectedKey),
                            onClick = {
                                setSelectedKey(it.key)
                                setSelectedValue(it.value)
                            },
                            role = Role.RadioButton,
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (it.key == selectedKey),
                        onClick = {
                            setSelectedKey(it.key)
                            setSelectedValue(it.value)
                        }
                    )
                    Text(
                        text = it.key,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Button(modifier = Modifier
                .align(Alignment.End)
                .padding(end = 60.dp),
                onClick = {
                    onLanguagePick(selectedValue)
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_confirm))
            }
        }
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
private fun SplashPage(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(SPLASH_TIME)
        navController.navigate(LANGUAGE) {
            popUpTo(SPLASH) {
                inclusive = true
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.install_splash_logo_ic),
                contentDescription = null,
            )
            Text(
                text = "*欢迎使用，正在加载",
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewInstallPage() {
//    LanguagePage {}
//    TimePickerPage {}
    DatePickerPage {}
}