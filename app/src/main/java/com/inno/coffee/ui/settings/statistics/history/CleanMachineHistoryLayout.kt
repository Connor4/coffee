package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.inno.common.db.entity.CleanMachineHistory


@Composable
fun CleanMachineHistoryLayout(
    viewModel: ProductHistoryViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val historyList by viewModel.cleanHistory.collectAsState()
//    val historyList = mutableListOf<CleanMachineHistory>()
    val placeHolder = CleanMachineHistory()

    CommonHistoryListLayout(
        listPaddingTop = 202,
        title = stringResource(R.string.statistic_clean_history),
        placeHolder = placeHolder,
        list = historyList,
        onCloseClick = { onCloseClick() },
        header = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 50.dp, top = 170.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = stringResource(R.string.statistic_product_top_date), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 29.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_wash_top_duration),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 310.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_wash_top_stop), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 500.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_wash_top_left), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 703.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_wash_top_right), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 877.dp)
                )
            }
        },
        listItem = { color, item ->
            HistoryItem(color, item as CleanMachineHistory)
//            HistoryItem(history = CleanMachineHistory(
//                time = "21.10.2024 11:50:06", startTime = "12:00",
//                duration = "12:00", stopped = true, tabsL = true, tabsR = true,
//            ))
        }
    )

}


@Composable
private fun HistoryItem(
    backgroundColor: Color = Color(0xFF191A1D),
    history: CleanMachineHistory,
) {
    val left: String
    val right: String
    val leftTextColor: Color
    val rightTextColor: Color
    val stop =
        if (history.stopped) stringResource(R.string.statistic_yes)
        else stringResource(R.string.statistic_no)
    if (history.tabsL) {
        left = stringResource(R.string.statistic_yes)
        leftTextColor = Color(0xFF6DD400)
    } else {
        left = stringResource(R.string.statistic_no)
        leftTextColor = Color(0xFFE02020)
    }
    if (history.tabsR) {
        right = stringResource(R.string.statistic_yes)
        rightTextColor = Color(0xFF6DD400)
    } else {
        right = stringResource(R.string.statistic_no)
        rightTextColor = Color(0xFFE02020)
    }

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
            contentAlignment = Alignment.CenterStart
        ) {
            if (history.time.isNotEmpty()) {
                Text(
                    text = history.time, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 25.dp)
                )
                Text(
                    text = history.duration, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 310.dp)
                )
                Text(
                    text = stop, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 500.dp)
                )
                Text(
                    text = left, color = leftTextColor,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 703.dp)
                )
                Text(
                    text = right, color = rightTextColor,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 877.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewCleanMachineHistory() {
    CleanMachineHistoryLayout()
}