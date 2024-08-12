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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.viewmodel.settings.statistics.ProductHistoryViewModel
import com.inno.common.db.entity.CleanMachineHistory

@Composable
fun CleanMachineStatistic(
    modifier: Modifier = Modifier,
    viewModel: ProductHistoryViewModel = hiltViewModel()
) {
    val historyList by viewModel.cleanHistory.collectAsState()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CleanTopBar()
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = historyList) {
                CleanHistoryItem(it)
            }
        }
    }
}

@Composable
private fun CleanHistoryItem(history: CleanMachineHistory) {
    val stop =
        if (history.stopped) stringResource(R.string.statistic_yes)
        else stringResource(R.string.statistic_no)
    val left =
        if (history.tabsL) stringResource(R.string.statistic_yes)
        else stringResource(R.string.statistic_no)
    val right =
        if (history.tabsL) stringResource(R.string.statistic_yes)
        else stringResource(R.string.statistic_no)
    Row {
        Text(text = history.time, color = Color.Black,
            modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = history.startTime, color = Color.Black, modifier = Modifier.weight(1f))
        Text(text = history.duration, color = Color.Black, modifier = Modifier.weight(1f))
        Text(text = stop, color = Color.Black, modifier = Modifier.weight(2f))
        Text(text = left, color = Color.Black, modifier = Modifier.weight(1f))
        Text(text = right, color = Color.Black, modifier = Modifier.weight(3f))
    }
}

@Composable
private fun CleanTopBar() {
    Row {
        Text(text = stringResource(R.string.statistic_product_top_time), color = Color.Black,
            modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_wash_top_start), color = Color.Black,
            modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.statistic_wash_top_duration), color = Color.Black,
            modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.statistic_wash_top_stop), color = Color.Black,
            modifier = Modifier.weight(2f))
        Text(text = stringResource(R.string.statistic_wash_top_left), color = Color.Black,
            modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.statistic_wash_top_right), color = Color.Black,
            modifier = Modifier.weight(3f))
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewWash() {
    CleanMachineStatistic()
}