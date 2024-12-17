package com.inno.coffee.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.inno.common.db.entity.CleanMachineHistory
import com.inno.common.utils.DimenUtils
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun VerticalScrollList2(
    list: List<Any> = mutableListOf(),
    minimumSize: Int = 15,
    extraSize: Int = 0,
    placeHolder: Any = Any(),
    scrollBarWidth: Int,
    scrollTrackHeight: Int,
    scrollBarPaddingEnd: Int = 40,
    listItemHeight: Float,
    listPaddingStart: Int = 0,
    listPaddingTop: Int = 0,
    listPaddingEnd: Int = 0,
    listPaddingBottom: Int = 0,
    content: @Composable (index: Int, item: Any) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex = remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }
    val firstVisibleItemScrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    val adjustedList = if ((list.size + extraSize) < minimumSize) {
        list + List(minimumSize - list.size - extraSize) { placeHolder }
    } else {
        list
    }
    val scrollBarHeight = (minimumSize.toFloat() / adjustedList.size.toFloat()) * scrollTrackHeight

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(scrollTrackHeight.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = listPaddingStart.dp, top = listPaddingTop.dp,
                    end = listPaddingEnd.dp, bottom = listPaddingBottom.dp),
            state = lazyListState,
        ) {
            itemsIndexed(adjustedList) { index, item ->
                content(index, item)
            }
        }

        if (adjustedList.size > minimumSize) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(scrollTrackHeight.dp)
                    .align(Alignment.TopEnd)
                    .padding(end = scrollBarPaddingEnd.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            dragOffset += dragAmount
                            val scrollOffset =
                                (dragOffset / size.height * lazyListState.layoutInfo
                                    .totalItemsCount).roundToInt()
                            coroutineScope.launch {
                                lazyListState.scrollToItem(
                                    scrollOffset.coerceIn(0, lazyListState
                                        .layoutInfo.totalItemsCount - 1))
                            }
                        }
                    }
                    .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
            ) {

                Box(
                    modifier = Modifier
                        .width(scrollBarWidth.dp)
                        .height(scrollBarHeight.dp)
                        .offset {
                            val itemHeight = DimenUtils.dp2px(context, listItemHeight)
                            val scrollHeight = itemHeight * firstVisibleItemIndex.value +
                                    firstVisibleItemScrollOffset.value
                            val rate = scrollHeight / (adjustedList.size * itemHeight)
                            val offset = scrollTrackHeight * rate
                            IntOffset(0, offset.toInt())
                        }
                        .background(Color(0xFF00DE93), RoundedCornerShape(20.dp))
                )
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewVerticalScrollList() {
    val list = mutableListOf<CleanMachineHistory>()
    VerticalScrollList2(list = list, scrollBarWidth = 14, scrollTrackHeight = 500,
        listItemHeight = 32f) { index, item ->
        Text(text = "test")
    }
}
