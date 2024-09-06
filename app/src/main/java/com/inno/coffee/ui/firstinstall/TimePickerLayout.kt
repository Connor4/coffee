package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.inno.coffee.R
import com.inno.coffee.utilities.NextStepButton
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.draw9Patch
import com.inno.coffee.utilities.nsp

@Composable
fun TimePickerLayout(modifier: Modifier = Modifier, onTimePick: (Long?) -> Unit) {
    val selectHour = remember {
        mutableStateOf(true)
    }
    val selectAm = remember {
        mutableStateOf(true)
    }

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
                text = stringResource(id = R.string.first_install_time_title),
                fontSize = 15.nsp(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Text(
            text = stringResource(id = R.string.first_install_select_time),
            fontSize = 7.nsp(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 155.dp, start = 54.dp)
        )
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 365.dp, start = 213.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .debouncedClickable({ selectHour.value = true })
            ) {
                if (selectHour.value) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .draw9Patch(LocalContext.current, R.drawable.install_time_minus_bg))
                }
                Text(
                    text = "00",
                    fontSize = 20.nsp(),
                    color = if (selectHour.value) Color(0xFF00DE93) else Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = ":",
                fontSize = 20.nsp(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .debouncedClickable({ selectHour.value = false })
            ) {
                if (!selectHour.value) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .draw9Patch(LocalContext.current, R.drawable.install_time_minus_bg))
                }
                Text(
                    text = "00",
                    fontSize = 20.nsp(),
                    color = if (!selectHour.value) Color(0xFF00DE93) else Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
//            Spacer(modifier = Modifier.width(30.dp))
//            Column(
//                modifier = Modifier.align(Alignment.CenterVertically),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    text = "AM", fontSize = 12.nsp(), fontWeight = FontWeight.Bold,
//                    color = if (selectAm.value) Color(0xFF00DE93) else Color.White,
//                    modifier = Modifier.fastclick { selectAm.value = true }
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//                Text(
//                    text = "FM", fontSize = 12.nsp(), fontWeight = FontWeight.Bold,
//                    color = if (!selectAm.value) Color(0xFF00DE93) else Color.White,
//                    modifier = Modifier.fastclick { selectAm.value = false }
//                )
//            }
        }

        Box(
            modifier = Modifier
                .padding(top = 260.dp, end = 212.dp)
                .size(372.dp)
                .align(Alignment.TopEnd),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    CoffeeTimePickerView(context).apply {
                    }
                },
                update = {
                }
            )
        }

        NextStepButton(modifier = Modifier.align(Alignment.BottomEnd)) {
        }
    }
}


@Preview(device = Devices.TABLET)
@Composable
private fun PreviewTimePicker() {
    TimePickerLayout() {}
}