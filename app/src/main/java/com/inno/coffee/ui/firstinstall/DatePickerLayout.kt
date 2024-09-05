package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.inno.coffee.R
import com.inno.coffee.utilities.NextStepButton
import com.inno.coffee.utilities.composeClick
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.nsp


@Composable
fun DatePickerLayout(modifier: Modifier = Modifier, onDatePick: (Long?) -> Unit) {
    val datePickerViewRef = remember {
        mutableStateOf<CoffeeDatePickerView?>(null)
    }
    val monthDayYearState = remember {
        mutableStateOf<String?>("")
    }
    val monthYearState = remember {
        mutableStateOf<String?>("")
    }
    val openYear = remember {
        mutableStateOf(false)
    }

    val prevInteractionSource = remember { MutableInteractionSource() }
    val nextInteractionSource = remember { MutableInteractionSource() }
    val prevPressed by prevInteractionSource.collectIsPressedAsState()
    val nextPressed by nextInteractionSource.collectIsPressedAsState()

    datePickerViewRef.value?.showYearPickerView(openYear.value)

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 66.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.first_install_date_title),
                fontSize = 15.nsp(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Text(
            text = stringResource(id = R.string.first_install_select_date),
            fontSize = 7.nsp(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 155.dp, start = 54.dp)
        )
        Text(
            text = monthDayYearState.value ?: "",
            fontSize = 10.nsp(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 200.dp, start = 54.dp)
        )
        HorizontalDivider(
            color = Color(0xFFFDFDFD),
            thickness = 2.dp,
            modifier = Modifier.padding(top = 280.dp, start = 54.dp, end = 54.dp)
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 300.dp, end = 54.dp)
        ) {
            Button(
                onClick = { datePickerViewRef.value?.performPrevClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                interactionSource = prevInteractionSource,
            ) {
                Image(
                    painter =
                    if (prevPressed) painterResource(id = R.drawable.install_date_prev_pressed_ic)
                    else painterResource(id = R.drawable.install_date_prev_normal_ic),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
                onClick = composeClick { datePickerViewRef.value?.performNextClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                interactionSource = nextInteractionSource,
            ) {
                Image(
                    painter =
                    if (nextPressed) painterResource(id = R.drawable.install_date_next_pressed_ic)
                    else painterResource(id = R.drawable.install_date_next_normal_ic),
                    contentDescription = null,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 294.dp, start = 54.dp)
                .debouncedClickable({
                    openYear.value = !openYear.value
                })
        ) {
            Text(text = monthYearState.value ?: "", fontSize = 6.nsp(), color = Color.White)
            Image(
                painter = if (openYear.value)
                    painterResource(id = R.drawable.install_date_arrow_up_ic)
                else
                    painterResource(id = R.drawable.install_date_arrow_down_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 30.dp)
                    .width(30.dp)
                    .height(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 325.dp)
                .width(700.dp)
                .height(436.dp)
                .align(Alignment.TopCenter),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    CoffeeDatePickerView(context).apply {
                        onDateSelected = { arg1, arg2 ->
                            monthDayYearState.value = arg1
                            monthYearState.value = arg2
                            openYear.value = false
                        }
                        datePickerViewRef.value = this
                    }
                },
                update = {
                    it.updateDate()
                }
            )
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                .height(30.dp)
//                .background(Color(0xFF191A1D)))
        }

        NextStepButton(modifier = Modifier.align(Alignment.BottomEnd)) {
//            onDatePick(datePickerState.selectedDateMillis)
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewDatePicker() {
    DatePickerLayout() {

    }
}