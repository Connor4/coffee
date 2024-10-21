package com.inno.coffee.ui.settings.statistics

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.statistics.history.StatisticHistoryActivity
import com.inno.coffee.ui.settings.statistics.product.StatisticProductActivity
import com.inno.coffee.utilities.HISTORY_VALUE_CLEAN
import com.inno.coffee.utilities.KEY_HISTORY
import com.inno.coffee.utilities.nsp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatisticLayout(
    onCloseClick: () -> Unit = {},
) {
    val names = arrayOf(
        Pair(1, R.string.statistic_day_counter),
        Pair(2, R.string.statistic_period_counter),
        Pair(3, R.string.statistic_total_counter),
    )
    val historyNames = arrayOf(
        Pair(4, R.string.statistic_product_history),
        Pair(5, R.string.statistic_error_history),
        Pair(6, R.string.statistic_clean_history),
        Pair(7, R.string.statistic_rinse_history),
        Pair(8, R.string.statistic_maintenance_history),
    )
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF191A1D))
    ) {
        Text(
            text = stringResource(id = R.string.statistic_layout_title),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 107.dp, end = 50.dp)
                .fastclick { onCloseClick() }
        )
        Text(
            text = stringResource(id = R.string.statistic_main_product_counter),
            fontSize = 6.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 190.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 244.dp, end = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 4,
        ) {
            names.forEach { pair ->
                Item(title = pair.second) {
                    when (pair.first) {
                        1 -> {
                            ScreenDisplayManager.autoRoute(context,
                                StatisticProductActivity::class.java)
                        }
                        2 -> {

                        }
                        3 -> {

                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(id = R.string.statistic_main_history_data),
            fontSize = 6.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 347.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 401.dp, end = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 4,
        ) {
            historyNames.forEach { pair ->
                Item(title = pair.second) {
                    when (pair.first) {
                        4 -> {

                        }
                        5 -> {

                        }
                        6 -> {
                            ScreenDisplayManager.autoRoute(
                                context,
                                StatisticHistoryActivity::class.java,
                                Bundle().apply {
                                    putString(KEY_HISTORY, HISTORY_VALUE_CLEAN)
                                }
                            )
                        }
                        7 -> {

                        }
                        8 -> {

                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(id = R.string.statistic_main_machine_counter),
            fontSize = 6.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 594.dp)
        )
        Item(title = R.string.statistic_main_machine_counter,
            modifier = Modifier.padding(start = 50.dp, top = 648.dp, end = 50.dp)) {

        }
    }
}

@Composable
private fun Item(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    val boarderColor = if (isPressed) Color(0xFF00DE93) else Color(0xFF484848)

    Box(
        modifier = modifier
            .width(280.dp)
            .height(73.dp)
            .border(2.dp, boarderColor, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF191A1D))
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onClick()
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                })
            },
    ) {
        Text(
            text = stringResource(id = title),
            fontSize = 5.nsp(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewStatistic() {
    StatisticLayout()
}