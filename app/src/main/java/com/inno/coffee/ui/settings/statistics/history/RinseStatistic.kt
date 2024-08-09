package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.viewmodel.settings.statistics.ProductHistoryViewModel
import com.inno.common.db.entity.RinseHistory

@Composable
fun RinseStatistic(
    modifier: Modifier = Modifier,
    viewModel: ProductHistoryViewModel = hiltViewModel()
) {
    val historyList by viewModel.rinseHistory.collectAsState()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        RinseTopBar(modifier)
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = historyList) {
                RinseHistoryItem(it)
            }
        }
    }
}

@Composable
private fun RinseHistoryItem(history: RinseHistory) {
    Row {
        Text(text = history.time, color = Color.Black, modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = history.rinseType, color = Color.Black, modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.systemFlowRateL}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.systemFlowRateR}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.systemWaterPressureL}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.systemWaterPressureR}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.nozzleFlowRateL}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.nozzleFlowRateR}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.nozzleWaterPressureL}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
        Text(text = "${history.nozzleWaterPressureR}", color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center)
    }
}

@Composable
private fun RinseTopBar(
    modifier: Modifier = Modifier
) {
    val left = stringResource(id = R.string.statistic_left)
    val right = stringResource(id = R.string.statistic_right)
    Row {
        Text(text = stringResource(R.string.statistic_product_top_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_rinse_top_type), color = Color.Black,
            modifier = modifier.weight(1f))
        Text(text = stringResource(
            R.string.statistic_rinse_top_system_flow_rate) + "\n$left\t\t\t\t\t\t$right",
            color = Color.Black, modifier = modifier.weight(2f), textAlign = TextAlign.Center)
        Text(
            text = stringResource(
                R.string.statistic_rinse_top_water_pressure) + "\n$left\t\t\t\t\t\t$right",
            color = Color.Black, modifier = modifier.weight(2f), textAlign = TextAlign.Center)
        Text(text = stringResource(
            R.string.statistic_rinse_top_limit_flow_rate) + "\n$left\t\t\t\t\t\t$right",
            color = Color.Black, modifier = modifier.weight(2f), textAlign = TextAlign.Center)
        Text(
            text = stringResource(
                R.string.statistic_rinse_top_water_pressure) + "\n$left\t\t\t\t\t\t$right",
            color = Color.Black, modifier = modifier.weight(2f), textAlign = TextAlign.Center)
    }
}