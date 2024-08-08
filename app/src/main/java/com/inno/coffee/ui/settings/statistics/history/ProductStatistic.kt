package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import com.inno.coffee.viewmodel.settings.statistics.DrinksHistoryViewModel
import com.inno.common.db.entity.ProductHistory
import com.inno.common.db.entity.ProductType

@Composable
fun ProductStatistic(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        StatisticTopBar()
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        ProductHistoryList()
    }
}

@Composable
fun StatisticTopBar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row {
        Text(text = context.getString(R.string.statistic_product_top_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_brew_side),
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
        Text(text = context.getString(R.string.statistic_product_top_stream_pressure),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_product_type),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = context.getString(R.string.statistic_product_top_operator), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
    }
}

@Composable
fun ProductHistoryList(
    modifier: Modifier = Modifier,
    viewModel: DrinksHistoryViewModel = hiltViewModel(),
) {
    val drinksHistoryList by viewModel.productHistory.collectAsStateWithLifecycle()
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        Column {
            Button(onClick = {
                viewModel.insertProductHistory(
                    ProductHistory("2023.06.23 10:40:00", "left", "2f", "On", "19s", "50", "92",
                        "64", "1.8", ProductType.Coffee.name, "Operator1"))
            }) {
                Text(text = "insert")
            }

            Button(onClick = {
                viewModel.deleteAllProductHistory()
            }) {
                Text(text = "delete")
            }

            LazyColumn(modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items = drinksHistoryList) {
                    DrinksHistoryItem(history = it)
                }
            }
        }
    }
}

@Composable
fun DrinksHistoryItem(history: ProductHistory, modifier: Modifier = Modifier) {
    Row {
        Text(text = history.time, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.side, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.grindTime, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.pqc, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.extTime, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.waterQnty, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.waterTemp, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.milkTemp, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.streamPressure, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.type, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.username, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewProductStatistic() {
    ProductStatistic()
}