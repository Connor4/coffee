package com.inno.coffee.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun VerticalScrollComposable(
    scrollBarWidth: Int = 14,
    scrollTrackHeight: Int,
    listHeight: Int,
    contentHeight: Int,
    listPaddingStart: Int = 0,
    listPaddingTop: Int = 0,
    listPaddingEnd: Int = 0,
    listPaddingBottom: Int = 0,
    content: @Composable () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val firstVisibleItemScrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val scrollBarHeight = (listHeight * scrollTrackHeight) / contentHeight

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(start = listPaddingStart.dp, top = listPaddingTop.dp,
                    end = (listPaddingEnd + 95).dp, bottom = listPaddingBottom.dp)
                .fillMaxSize(),
        ) {
            items(listOf(1)) {
                content()
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 36.dp, end = 40.dp)
                .wrapContentWidth()
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        coroutineScope.launch {
                            lazyListState.scroll {
                                scrollBy(dragAmount)
                            }
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
                        val rate = firstVisibleItemScrollOffset.value / contentHeight.toFloat()
                        val scrollHeight = scrollTrackHeight * rate
                        IntOffset(0, scrollHeight.toInt())
                    }
                    .background(Color(0xFF00DE93), RoundedCornerShape(20.dp))
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewVerticalScrollComposable() {
    VerticalScrollComposable(scrollBarWidth = 14, scrollTrackHeight = 500, listHeight = 568,
        contentHeight = 969) {
        Text("hahah")
    }
}