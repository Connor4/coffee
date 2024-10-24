package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.ui.common.PageIndicator
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun FormulaDrinkPage(
    selectedModel: DrinksModel?,
    selectedFormula: Formula?,
    totalCount: Int,
    pagerState: PagerState,
    drinksTypeList: List<DrinksModel>,
    onDrinkItemClick: (model: DrinksModel) -> Unit,
) {
    val PAGE_COUNT = 10

    Box {
        Box(
            modifier = Modifier.padding(start = 219.dp, top = 198.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .border(1.dp, Color(0xFF484848), RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color(0xFF191A1D)),
            ) {
                val drawableRes = selectedModel?.imageRes ?: R.drawable.drink_espresso_ic
//                val stringRes = selectedFormula?.productName ?: R.string.home_item_espresso
                val name = selectedFormula?.productName?.name ?: ""
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 40.dp),
                )
                Text(
                    text = name,
                    fontSize = 6.nsp(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 129.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(top = 450.dp, start = 90.dp)
        ) {
            HorizontalPager(
                modifier = Modifier
                    .width(620.dp)
                    .height(260.dp),
                state = pagerState
            ) { page ->
                val fromIndex = page * PAGE_COUNT
                val toIndex = minOf(fromIndex + PAGE_COUNT, drinksTypeList.size)
                val currentList = drinksTypeList.subList(fromIndex, toIndex)
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    maxItemsInEachRow = 5,
                ) {
                    currentList.forEach {
                        val select = selectedModel?.productId == it.productId
                        FormulaDrinkItem(model = it, selected = select) {
//                            selectedModel = it
//                            viewModel.getFormula(it.productId)
                            onDrinkItemClick(it)
                        }
                    }
                }
            }

            if (totalCount > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 230.dp),
                ) {
                    PageIndicator(totalPage = totalCount, selectedPage = pagerState.currentPage)
                }
            }
        }
    }
}

@Composable
private fun FormulaDrinkItem(
    model: DrinksModel,
    selected: Boolean = false,
    onDrinkClick: () -> Unit = {},
) {

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .debouncedClickable({ onDrinkClick() }),
        contentAlignment = Alignment.Center,
    ) {

        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.common_drink_item_selected_bg),
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.common_drink_item_normal_bg),
                contentDescription = null,
                modifier = Modifier.size(101.dp)
            )
        }
        Image(
            painter = painterResource(id = model.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .align(Alignment.Center)
        )
    }
}
