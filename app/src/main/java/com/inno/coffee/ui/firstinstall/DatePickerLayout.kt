package com.inno.coffee.ui.firstinstall

import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.inno.coffee.utilities.nsp


@Composable
fun DatePickerLayout(modifier: Modifier = Modifier, onDatePick: (Long?) -> Unit) {
    var isPressed by remember {
        mutableStateOf(false)
    }

    Box(
//        modifier = modifier,
        modifier = Modifier.background(Color.LightGray)
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

        AndroidView(
            modifier = Modifier.padding(start = 50.dp, top = 155.dp, end = 50.dp, bottom = 100.dp),
            factory = {
                DatePicker(it).apply {

                }
            })

        Button(
            onClick = {
                isPressed = true
//                onDatePick(datePickerState.selectedDateMillis)
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
            Image(
                painter = if (isPressed) painterResource(
                    id = R.drawable.install_language_next_pressed_ic)
                else painterResource(
                    id = R.drawable.install_language_next_normal_ic),
                contentDescription = null,
            )
        }
    }
}


@Preview(device = Devices.TABLET)
@Composable
private fun PreviewDatePicker() {
    DatePickerLayout() {

    }
}