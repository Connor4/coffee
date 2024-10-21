package com.inno.coffee.ui.settings.statistics.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun CommonHistoryListLayout(
    listPaddingTop: Int,
    title: String = "",
    placeHolder: Any = Any(),
    list: List<Any> = mutableListOf(),
    onCloseClick: () -> Unit = {},
    header: @Composable () -> Unit,
    listItem: @Composable (color: Color, item: Any) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = title,
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )
        header()

        Box(
            modifier = Modifier.padding(top = listPaddingTop.dp)
        ) {
            VerticalScrollList2(list = list, placeHolder = placeHolder,
                scrollBarWidth = 14, scrollTrackHeight = 500,
                listPaddingStart = 50, listPaddingTop = 14, listPaddingEnd = 95,
                listItemHeight = 32f) { index, item ->

                val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                listItem(color, item)
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewCommonHistoryList() {
    CommonHistoryListLayout(
        listPaddingTop = 202,
        title = stringResource(id = R.string.statistic_clean_history),
        header = {},
        listItem = { color, item -> Text("test") },
    )
}