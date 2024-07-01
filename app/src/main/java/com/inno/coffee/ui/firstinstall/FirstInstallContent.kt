package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R

private const val LANGUAGE = "language"
private const val DATE = "date"
private const val TIME = "time"


@Composable
fun InstallSetting(onSetComplete: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LANGUAGE) {
        composable(LANGUAGE) {
            LanguagePage {
                navController.navigate(DATE)
            }
        }
        composable(DATE) {
            DatePickerPage {
                navController.navigate(TIME)
            }
        }
        composable(TIME) {
            TimePickerPage {
                onSetComplete()
            }
        }
    }
}

@Composable
fun LanguagePage(onLanguagePick: () -> Unit) {
    val context = LocalContext.current
    val radioOptions = listOf(
        context.getString(R.string.first_install_language_Chinese),
        context.getString(R.string.first_install_language_English)
    )
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[0])
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
                            selected = (it == selectedOption),
                            onClick = { onOptionSelected(it) },
                            role = Role.RadioButton,
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (it == selectedOption),
                        onClick = {
                            onLanguagePick()
                        }
                    )
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Button(modifier = Modifier
                .align(Alignment.End)
                .padding(end = 60.dp),
                onClick = {
                    onLanguagePick()
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_confirm))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerPage(onDatePick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier
            .width(1100.dp)
            .wrapContentHeight()) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = 1704124800000)
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )

            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 60.dp),
                onClick = {
                    onDatePick()
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_confirm))
            }

//        Text(
//            "Selected date timestamp: ${datePickerState.selectedDateMillis ?: "no selection"}",
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerPage(onSetComplete: () -> Unit) {
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
                onClick = { /* Handle cancel action */ },
                modifier = Modifier.constrainAs(cancelButton) {
                    top.linkTo(timePicker.bottom, margin = 16.dp)
                    end.linkTo(confirmButton.start, margin = 8.dp)
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_cancel))
            }

            Button(
                onClick = {
                    onSetComplete()
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


@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewInstallPage() {
//    LanguagePage {}
//    TimePickerPage {}
    DatePickerPage {}
}