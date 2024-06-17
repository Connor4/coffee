package com.inno.coffee.ui.settings.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.R
import com.inno.coffee.data.settings.statistics.DrinksHistoryViewModel
import com.inno.common.db.entity.DrinksHistory
import com.inno.common.db.entity.ProductType

@Composable
fun ProductStatistic(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        Column {
            StatisticTopBar()
            Divider(color = Color.Gray, thickness = 1.dp)
            DrinksHistoryList()
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

@Composable
fun DrinksHistoryList(
    modifier: Modifier = Modifier,
    viewModel: DrinksHistoryViewModel = hiltViewModel(),
) {
    val drinksHistoryList by viewModel.drinksHistory.collectAsStateWithLifecycle()
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {

        Column {
            Text(text = "历史数据")

            Column {
                Button(onClick = {
                    viewModel.insertDrinksHistory(
                        DrinksHistory("2023", "left", "2f", "4s", ProductType.Coffee.name)
                    )
                }) {
                    Text(text = "insert")
                }

                Button(onClick = {
                    viewModel.deleteAllDrinksHistory()
                }) {
                    Text(text = "delete")
                }

                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(items = drinksHistoryList) {
                        DrinksHistoryItem(history = it)
                    }
                }
            }
        }
    }
}

@Composable
fun DrinksHistoryItem(history: DrinksHistory, modifier: Modifier = Modifier) {
    Row {
        Text(text = history.time)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = history.side)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = history.grindTime)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = history.pqc)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = history.type)
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewProductStatistic() {
    ProductStatistic()
}