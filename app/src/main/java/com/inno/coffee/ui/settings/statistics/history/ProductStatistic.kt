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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.viewmodel.settings.statistics.ProductHistoryViewModel
import com.inno.common.db.entity.ProductHistory
import com.inno.common.enums.ProductType

@Composable
fun ProductStatistic(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        StatisticTopBar()
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        ProductHistoryList()
    }
}

@Composable
private fun StatisticTopBar(modifier: Modifier = Modifier) {
    Row {
        Text(text = stringResource(R.string.statistic_product_top_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_press_final), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_brew_side),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_grind_time),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_pqc), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_grind_adjust),
            color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_ext_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_water_qnty),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_water_temp),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_mike_temp),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_stream_pressure),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_product_type),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_cups),
            color = Color.Black, modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_product_top_operator), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
    }
}

@Composable
private fun ProductHistoryList(
    modifier: Modifier = Modifier,
    viewModel: ProductHistoryViewModel = hiltViewModel(),
) {
    val drinksHistoryList by viewModel.productHistory.collectAsState()
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        Column {
            Button(onClick = {
                viewModel.insertProductHistory(
                    ProductHistory("2023.06.23 10:40:00", 20.3f, true,
                        5.5f, true, -60, 12.2f, 400,
                        92, 0, 0, ProductType.COFFEE, 1,
                        false, "Operator1"))
            }) {
                Text(text = "*insert")
            }

            Button(onClick = {
                viewModel.deleteAllProductHistory()
            }) {
                Text(text = "*delete")
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

@Composable
private fun DrinksHistoryItem(history: ProductHistory, modifier: Modifier = Modifier) {
    val side =
        if (history.brewSide) stringResource(id = R.string.statistic_left)
        else stringResource(id = R.string.statistic_right)
    val pqc =
        if (history.pqc) stringResource(id = R.string.statistic_on)
        else stringResource(id = R.string.statistic_off)
    val discard = if (history.discard) Color.Red else Color.Green
    Row {
        Text(text = history.time, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.pressFinal}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = side, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.grindTime}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = pqc, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.grindAdjust}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.extTime}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.waterQuality}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.waterTemp}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.milkTemp}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.steamPressure}", color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.productType.value, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.cups}x", color = discard, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.account, color = Color.Black, modifier = modifier.weight(1f),
            textAlign = TextAlign.Center)
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewProductStatistic() {
    ProductStatistic()
}