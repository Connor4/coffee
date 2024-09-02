package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.nsp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeTopBar(
    open: Boolean,
    onClick: (show: Boolean) -> Unit = {},
) {
    var date by remember { mutableStateOf(getCurrentDate()) }
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            date = getCurrentDate()
            currentTime = getCurrentTime()
            delay(60_000L)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color(0xFF000002)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = date,
            fontSize = 6.nsp(),
            color = Color(0xFFECFFF9),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        )
        Text(
            text = currentTime,
            fontSize = 6.nsp(),
            color = Color(0xFFECFFF9),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .wrapContentWidth(Alignment.End)
                .size(60.dp)
                .debouncedClickable({
                    onClick(!open)
                }),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = if (open) painterResource(id = R.drawable.home_entrance_select_ic)
                else painterResource(id = R.drawable.home_entrance_normal_ic),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp),
            )
        }
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return sdf.format(Date())
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewTop() {
    HomeTopBar(true)
}