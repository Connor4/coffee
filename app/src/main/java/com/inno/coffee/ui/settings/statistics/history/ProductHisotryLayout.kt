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
import com.inno.common.db.entity.ProductHistory

@Composable
fun ProductHistoryLayout(
    viewModel: ProductHistoryViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val drinksHistoryList by viewModel.productHistory.collectAsState()
    val placeHolder = ProductHistory()

    CommonHistoryListLayout(
        listPaddingTop = 236,
        title = stringResource(R.string.statistic_product_history),
        placeHolder = placeHolder,
        list = drinksHistoryList,
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
                    text = stringResource(R.string.statistic_product_top_time), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 126.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_press_final),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 226.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_brew_side),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 296.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_grind_time),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 364.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_pqc), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 436.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_grind_adjust),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 496.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_ext_time),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 577.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_water_qnty),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 642.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_water_temp),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 720.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_mike_temp),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 798.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_stream_pressure),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 870.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_product_type),
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 972.dp)
                )
                Text(
                    text = stringResource(R.string.statistic_product_top_cups), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 1065.dp)
                )

            }
        },
        listItem = { color, item ->
            HistoryItem(color, item as ProductHistory)
        }
    )
}

@Composable
private fun HistoryItem(
    backgroundColor: Color = Color(0xFF191A1D),
    history: ProductHistory,
) {
    val side =
        if (history.brewSide) stringResource(id = R.string.statistic_left)
        else stringResource(id = R.string.statistic_right)
    val pqc =
        if (history.pqc) stringResource(id = R.string.statistic_on)
        else stringResource(id = R.string.statistic_off)
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
                    text = history.time, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 1.dp)
                )
                Text(
                    text = history.pressFinal.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 226.dp)
                )
                Text(
                    text = side,
                    color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 296.dp)
                )
                Text(
                    text = history.grindTime.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 364.dp)
                )
                Text(
                    text = pqc, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 436.dp)
                )
                Text(
                    text = history.grindAdjust.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 496.dp)
                )
                Text(
                    text = history.extTime.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 577.dp)
                )
                Text(
                    text = history.waterQuantity.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 642.dp)
                )
                Text(
                    text = history.waterTemp.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 720.dp)
                )
                Text(
                    text = history.milkTemp.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 798.dp)
                )
                Text(
                    text = history.steamPressure.toString(), color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 870.dp)
                )
                Text(
                    text = history.productType.value, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 972.dp)
                )
                Text(
                    text = "${history.cups}x", color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 1065.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewProductHistory() {
    ProductHistoryLayout()
//    HistoryItem(history = ProductHistory(time = "04.08.20 20:34:01", pressFinal = 24.0f,
//    brewSide = true, grindTime = 11.4f, pqc = true, grindAdjust = 2, extTime = 18.6f,
//    waterQuantity = 100, waterTemp = 80, milkTemp = 90, steamPressure = 19, productType =
//    ProductType.COFFEE, cups = 2))
}