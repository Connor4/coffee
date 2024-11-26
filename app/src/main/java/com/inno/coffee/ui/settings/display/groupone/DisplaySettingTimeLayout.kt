package com.inno.coffee.ui.settings.display.groupone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.draw9Patch
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.firstinstall.CoffeeTimePickerView
import com.inno.coffee.utilities.HOUR
import com.inno.coffee.utilities.MINUTES
import com.inno.coffee.utilities.nsp
import java.util.Calendar
import java.util.Locale

@Composable
fun DisplaySettingTimeLayout(
    onTimePick: (Int, Int) -> Unit,
    onCloseClick: () -> Unit = {},
    onCancelClick: () -> Unit,
) {
    val selectHour = remember {
        mutableStateOf(true)
    }
    val timePickerViewRef = remember {
        mutableStateOf<CoffeeTimePickerView?>(null)
    }
    val hour = remember {
        mutableStateOf("00")
    }
    val minutes = remember {
        mutableStateOf("00")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
    ) {
        Text(
            text = stringResource(id = R.string.first_install_select_time),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 305.dp, start = 213.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .debouncedClickable({
                        selectHour.value = true
                        timePickerViewRef.value?.setShowType(HOUR)
                    })
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
                    text = hour.value,
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
                    .debouncedClickable({
                        selectHour.value = false
                        timePickerViewRef.value?.setShowType(MINUTES)
                    })
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
                    text = minutes.value,
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
                .padding(top = 216.dp, end = 212.dp)
                .size(372.dp)
                .align(Alignment.TopEnd),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    CoffeeTimePickerView(context).apply {
                        onValueSelected = { pickerType, newValue, autoAdvance ->
                            when (pickerType) {
                                HOUR -> {
                                    hour.value = String.format(Locale.getDefault(), "%02d",
                                        newValue)
                                    if (autoAdvance) {
                                        selectHour.value = false
                                        timePickerViewRef.value?.setShowType(MINUTES)
                                    }
                                }
                                MINUTES -> {
                                    minutes.value = String.format(Locale.getDefault(), "%02d",
                                        newValue)
                                }
                            }
                        }
                        timePickerViewRef.value = this
                    }
                },
                update = {
                    val calendar = Calendar.getInstance(Locale.getDefault())
                    val currentHour = calendar[Calendar.HOUR_OF_DAY]
                    val currentMinute = calendar[Calendar.MINUTE]
                    hour.value = String.format(Locale.getDefault(), "%02d",
                        currentHour)
                    minutes.value = String.format(Locale.getDefault(), "%02d",
                        currentMinute)
                }
            )
        }
        ChangeColorButton(
            modifier = Modifier
                .padding(end = 250.dp, bottom = 110.dp)
                .width(180.dp)
                .height(50.dp)
                .align(Alignment.BottomEnd),
            text = stringResource(R.string.common_button_cancel)
        ) {
            onCancelClick()
        }
        ChangeColorButton(
            modifier = Modifier
                .padding(end = 50.dp, bottom = 110.dp)
                .width(180.dp)
                .height(50.dp)
                .align(Alignment.BottomEnd),
            text = stringResource(R.string.common_button_confirm)
        ) {
            onTimePick(hour.value.toInt(), minutes.value.toInt())
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplaySettingTime() {
    DisplaySettingTimeLayout({ _, _ -> }, {}, {})
}