package com.inno.coffee.ui.settings.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R

@Composable
fun MachineChangeStatistic(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ChangeTopBar(modifier)
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
    }
}

@Composable
fun ChangeTopBar(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row {
        Text(text = context.getString(R.string.statistic_product_top_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_change_top_event), color = Color.Black,
            modifier = modifier.weight(4f))
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewChange() {
    MachineChangeStatistic()
}