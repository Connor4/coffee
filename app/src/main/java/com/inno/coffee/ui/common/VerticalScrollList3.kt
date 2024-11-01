package com.inno.coffee.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerticalScrollList3(
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    val itemCount = 13
    val scrollBarHeight = 190f

    Box {
        Column(
            modifier = Modifier
                .width(492.dp)
                .height(286.dp)
                .verticalScroll(scrollState)
                .align(Alignment.CenterStart),
        ) {
            content()
        }

//        Box(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .width(14.dp)
//                .fillMaxHeight()
//                .pointerInput(Unit) {
//                    detectVerticalDragGestures { _, dragAmount ->
//                        dragOffset += dragAmount
//                        val scrollOffset = (dragOffset / size.height * lazyListState.layoutInfo
//                            .totalItemsCount).roundToInt()
//                        coroutineScope.launch {
//                            lazyListState.scrollToItem(scrollOffset.coerceIn(0, lazyListState
//                                .layoutInfo.totalItemsCount - 1))
//                        }
//                    }
//                }
//                .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
//        ) {
//            val scrollProgress = lazyListState.firstVisibleItemIndex.toFloat() / (itemCount - 1)
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(scrollBarHeight.dp)
//                    .offset {
//                        val den = DimenUtils.dp2px(context, 293f)
//                        IntOffset(0, (scrollProgress * den).toInt())
//                    }
//                    .background(Color(0xFF00DE93), RoundedCornerShape(10.dp))
//            )
//        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewVerticalScrollList3() {
    VerticalScrollList3({ Text("hahahah") })
}