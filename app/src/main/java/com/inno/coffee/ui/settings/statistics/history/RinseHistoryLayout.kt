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
import com.inno.common.db.entity.RinseHistory

@Composable
fun RinseHistoryLayout(
    viewModel: ProductHistoryViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val historyList by viewModel.rinseHistory.collectAsState()
//    val historyList = mutableListOf<RinseHistory>()
    val placeHolder = RinseHistory()

    CommonHistoryListLayout(
        minimumSize = 12,
        listPaddingTop = 276,
        title = stringResource(R.string.statistic_rinse_history),
        placeHolder = placeHolder,
        list = historyList,
        onCloseClick = { onCloseClick() },
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 50.dp, top = 170.dp),
            ) {
                Box {
                    Text(
                        text = stringResource(R.string.statistic_product_top_date),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 28.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_product_top_time),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 145.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_rinse_top_type),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 236.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_rinse_top_system_flow_rate),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 449.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_rinse_top_water_pressure),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 611.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_rinse_top_limit_flow_rate),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 773.dp)
                    )
                    Text(
                        text = stringResource(R.string.statistic_rinse_top_water_pressure),
                        color = Color.White,
                        fontSize = 5.nsp(),
                        modifier = Modifier.padding(start = 935.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    LAndR(449, 525)
                    LAndR(611, 687)
                    LAndR(773, 849)
                    LAndR(935, 1011)
                }
            }
        },
        listItem = { color, item ->
            HistoryItem(color, item as RinseHistory)
//            HistoryItem(
//                history = RinseHistory(time = "04.08.2023 11:00:00", rinseType = "Cold Water",
//                    systemFlowRateL = 6.9f, systemFlowRateR = 6.9f, systemWaterPressureL = 1.0f,
//                    systemWaterPressureR = 1.0f, nozzleFlowRateL = 6.9f, nozzleFlowRateR = 6.9f,
//                    nozzleWaterPressureL = 1.0f, nozzleWaterPressureR = 1.0f))
        }
    )
}

@Composable
private fun HistoryItem(
    backgroundColor: Color = Color(0xFF191A1D),
    history: RinseHistory,
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
                    text = history.rinseType,
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 236.dp)
                )
                Text(
                    text = "${history.systemFlowRateL}",
                    color = Color(0xFF6DD400),
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 449.dp)
                )
                Text(
                    text = "${history.systemFlowRateR}",
                    color = Color(0xFF6DD400),
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 525.dp)
                )
                Text(
                    text = "${history.systemWaterPressureL}",
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 611.dp)
                )
                Text(
                    text = "${history.systemWaterPressureR}",
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 687.dp)
                )
                Text(
                    text = "${history.nozzleFlowRateL}",
                    color = Color(0xFF6DD400),
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 773.dp)
                )
                Text(
                    text = "${history.nozzleFlowRateR}",
                    color = Color(0xFF6DD400),
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 849.dp)
                )
                Text(
                    text = "${history.nozzleWaterPressureL}",
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 935.dp)
                )
                Text(
                    text = "${history.nozzleWaterPressureR}",
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 1011.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
private fun LAndR(
    lPaddingStart: Int,
    rPaddingStart: Int,
) {
    Text(
        text = stringResource(id = R.string.statistic_left),
        color = Color.White,
        fontSize = 5.nsp(),
        modifier = Modifier.padding(start = lPaddingStart.dp)
    )
    Text(
        text = stringResource(id = R.string.statistic_right),
        color = Color.White,
        fontSize = 5.nsp(),
        modifier = Modifier.padding(start = rPaddingStart.dp)
    )
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewRinseHistory() {
    RinseHistoryLayout()
//    RinseHistory(time = "04.08.2023 11:00:00", rinseType = "Cold Water",
//        systemFlowRateL = 6.9f, systemFlowRateR = 6.9f, systemWaterPressureL = 1.0f,
//        systemWaterPressureR = 1.0f, nozzleFlowRateL = 6.9f, nozzleFlowRateR = 6.9f,
//        nozzleWaterPressureL = 1.0f, nozzleWaterPressureR = 1.0f)
}