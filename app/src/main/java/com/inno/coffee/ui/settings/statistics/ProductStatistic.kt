package com.inno.coffee.ui.settings.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
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
fun ProductStatistic(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
        Column {
            StatisticTopBar()
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun StatisticTopBar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row {
        Text(text = context.getString(R.string.statistic_product_top_date), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_brew_size),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_grind_time),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_PQC), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_ext_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_water_qnty),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_water_temp),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_mike_temp),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_stream_press),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_product_type),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewProductStatistic() {
    ProductStatistic()
}