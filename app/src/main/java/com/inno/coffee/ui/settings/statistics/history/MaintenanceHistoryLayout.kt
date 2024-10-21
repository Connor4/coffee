package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.SingleInputLayout
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.statistics.ProductHistoryViewModel
import com.inno.common.db.entity.MaintenanceHistory

@Composable
fun MaintenanceHistoryLayout(
    viewModel: ProductHistoryViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    var showInput by remember { mutableStateOf(false) }
    val historyList by viewModel.maintenanceHistory.collectAsState()
//    val historyList = mutableListOf<MaintenanceHistory>()
    val placeHolder = MaintenanceHistory()

    Box {
        CommonHistoryListLayout(
            listPaddingTop = 202,
            title = stringResource(R.string.statistic_maintenance_history),
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
                        text = stringResource(R.string.statistic_product_top_time),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 126.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_maintenance_top_description),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 236.dp)
                    )
                }
            },
            listItem = { color, item ->
                HistoryItem(color, item as MaintenanceHistory)
//                HistoryItem(color, MaintenanceHistory(time = "04.08.2025 20:34:01",
//                    description = "test"))
            }
        )

        ChangeColorButton(
            text = stringResource(R.string.statistic_add),
            modifier = Modifier
                .width(200.dp)
                .height(77.dp)
                .padding(bottom = 27.dp)
                .align(Alignment.BottomCenter)) {
            showInput = true
        }

        if (showInput) {
            SingleInputLayout(
                title = stringResource(R.string.statistic_maintenance_history),
                tips = stringResource(R.string.statistic_maintenance_enter_description),

                onEnterClick = { description ->
                    showInput = false
                    viewModel.addMaintenanceHistory(description)
                },
                onCloseClick = {
                    showInput = false
                })
        }
    }
}

@Composable
private fun HistoryItem(
    backgroundColor: Color = Color(0xFF191A1D),
    history: MaintenanceHistory,
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
                    text = history.description,
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 236.dp)
                )
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMaintenanceHistory() {
    MaintenanceHistoryLayout()
}

