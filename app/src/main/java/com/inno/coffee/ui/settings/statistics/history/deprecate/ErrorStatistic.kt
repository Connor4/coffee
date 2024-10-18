package com.inno.coffee.ui.settings.statistics.history.deprecate

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
import com.inno.common.db.entity.ErrorHistory

@Composable
fun ErrorStatistic(
    modifier: Modifier = Modifier,
    viewModel: ProductHistoryViewModel = hiltViewModel()
) {
    val historyList by viewModel.errorHistory.collectAsState()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ErrorTopBar(modifier)
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(items = historyList) {
                ErrorHistoryItem(history = it)
            }
        }
    }
}

@Composable
private fun ErrorHistoryItem(history: ErrorHistory) {
    Row {
        Text(text = history.time, color = Color.Black,
            modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = history.code, color = Color.Black, modifier = Modifier.weight(1f))
        Text(text = history.detail, color = Color.Black, modifier = Modifier.weight(5f))
    }
}

@Composable
private fun ErrorTopBar(
    modifier: Modifier = Modifier
) {
    Row {
        Text(text = stringResource(R.string.statistic_product_top_time), color = Color.Black,
            modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.statistic_error_top_code), color = Color.Black,
            modifier = modifier.weight(1f))
        Text(text = stringResource(R.string.statistic_error_top_event), color = Color.Black,
            modifier = modifier.weight(5f))
    }
}