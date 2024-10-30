package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.statistics.ProductHistoryViewModel
import com.inno.common.db.entity.ErrorHistory

@Composable
fun ErrorHistoryLayout(
    viewModel: ProductHistoryViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val historyList by viewModel.errorHistory.collectAsState()
//    val historyList = mutableListOf<ErrorHistory>()
    val placeHolder = ErrorHistory()

    CommonHistoryListLayout(
        listPaddingTop = 180,
        title = stringResource(R.string.statistic_error_history),
        placeHolder = placeHolder,
        list = historyList,
        onCloseClick = { onCloseClick() },
        header = {
        },
        listItem = { color, item ->
            HistoryItem(color, item as ErrorHistory)
//            HistoryItem(color, ErrorHistory(time = "04.08.2025 20:34:01",
            //            detail = "Regular maintenance and upkeep are required", code = "W-204"))
        }
    )
}

@Composable
private fun HistoryItem(
    backgroundColor: Color = Color(0xFF191A1D),
    history: ErrorHistory,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = backgroundColor),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (history.time.isNotEmpty()) {
                Text(
                    text = history.time,
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 25.dp)
                )
                Text(
                    text = history.code,
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 270.dp)
                )
                Text(
                    text = history.detail,
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 360.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewErrorHistory() {
    ErrorHistoryLayout()
}