package com.inno.coffee.ui.settings

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.data.settings.DrinksHistoryViewModel
import com.inno.common.db.entity.DrinksHistory
import com.inno.common.db.entity.ProductType

@Composable
fun DrinksHistoryList(
    modifier: Modifier = Modifier,
    viewModel: DrinksHistoryViewModel = hiltViewModel(),
) {
    val drinksHistoryList by viewModel.drinksHistory.collectAsStateWithLifecycle()
    Surface {
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

@Preview
@Composable
fun PreviewDrinksHistoryList() {
    DrinksHistoryList()
}