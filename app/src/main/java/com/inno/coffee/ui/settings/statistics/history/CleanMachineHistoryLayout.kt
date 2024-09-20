package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.CleanMachineHistory
import com.inno.common.utils.DimenUtils
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun CleanMachineHistoryLayout(
    onCloseClick: () -> Unit = {},
) {
    val minimumSize = 15
    val singularColor = Color(0xFF191A1D)
    val evenColor = Color(0xFF2A2B2D)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    val list = mutableListOf<CleanMachineHistory>()
    if (list.size < minimumSize) {
        for (i in 0 until (minimumSize - list.size)) {
            list.add(CleanMachineHistory())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.statistic_clean_history),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 55.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 47.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 78.dp, top = 120.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = stringResource(R.string.statistic_product_top_time), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 78.dp)
            )
            Text(
                text = stringResource(R.string.statistic_wash_top_start), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 263.dp)
            )
            Text(
                text = stringResource(R.string.statistic_wash_top_duration), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 457.dp)
            )
            Text(
                text = stringResource(R.string.statistic_wash_top_stop), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 587.dp)
            )
            Text(
                text = stringResource(R.string.statistic_wash_top_left), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 751.dp)
            )
            Text(
                text = stringResource(R.string.statistic_wash_top_right), color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 920.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 166.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 78.dp, end = 95.dp),
                state = lazyListState,
            ) {
                itemsIndexed(list) { index, item ->
                    val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                    HistoryItem(history = item, backgroundColor = color)
                }
            }
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 152.dp, end = 38.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        dragOffset += dragAmount
                        val scrollOffset = (dragOffset / size.height * lazyListState.layoutInfo
                            .totalItemsCount).roundToInt()
                        coroutineScope.launch {
                            lazyListState.scrollToItem(scrollOffset.coerceIn(0, lazyListState
                                .layoutInfo.totalItemsCount - 1))
                        }
                    }
                }
                .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
        ) {
            val scrollProgress = lazyListState.firstVisibleItemIndex.toFloat() / (list.size - 1)
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(500.dp)
                    .offset {
                        val den = DimenUtils.dp2px(context, 293f)
                        IntOffset(0, (scrollProgress * den).toInt())
                    }
                    .background(Color(0xFF00DE93), RoundedCornerShape(10.dp))
            )
        }
    }
}


@Composable
private fun HistoryItem(
    modifier: Modifier = Modifier,
    history: CleanMachineHistory,
    backgroundColor: Color = Color(0xFF191A1D),
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
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = backgroundColor),
            contentAlignment = Alignment.CenterStart
        ) {
            if (history.time.isNotEmpty()) {
                Text(
                    text = history.time, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 78.dp)
                )
                Text(
                    text = history.startTime, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 263.dp)
                )
                Text(
                    text = history.duration, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 457.dp)
                )
                Text(
                    text = stop, color = Color.White,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 587.dp)
                )
                Text(
                    text = left, color = leftTextColor,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 751.dp)
                )
                Text(
                    text = right, color = rightTextColor,
                    fontSize = 5.nsp(),
                    modifier = Modifier.padding(start = 920.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewCleanMachineHistory() {
    CleanMachineHistoryLayout()
}