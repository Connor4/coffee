package com.inno.coffee.ui.settings.statistics.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.PageIndicator
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.getImageResId
import com.inno.coffee.utilities.getStringResId
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.statistics.StatisticProductViewModel
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType

private const val PAGE_COUNT = 10

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun StatisticTotalProductLayout(
    viewModel: StatisticProductViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val drinksTypeList by viewModel.drinksList.collectAsState()
    val typeCounts by viewModel.typeCounts.collectAsState()
    val selectedProductCount by viewModel.productCount.collectAsState()
    val time by viewModel.time.collectAsState()
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val selectFormula by viewModel.formula.collectAsState()

    val coffee = lookForCount(typeCounts, ProductType.COFFEE)
    val hotWater = lookForCount(typeCounts, ProductType.HOT_WATER)
    val milk = lookForCount(typeCounts, ProductType.MILK)
    val foam = lookForCount(typeCounts, ProductType.FOAM)
    val steam = lookForCount(typeCounts, ProductType.STEAM)
//    val total = coffee + hotWater + milk + foam + steam

    LaunchedEffect(Unit) {
        viewModel.loadDrinkTypeList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF191A1D))
    ) {
        // ==============================left========================================
        Text(
            text = stringResource(id = R.string.statistic_total_counter),
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
                .padding(start = 90.dp, top = 140.dp)
                .width(200.dp)
                .height(180.dp)
                .border(1.dp, Color(0xFF484848), RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color(0xFF191A1D)),
        ) {
            val drawableRes = selectFormula?.imageRes ?: "drink_item_empty_ic"
            Image(
                painter = painterResource(id = getImageResId(drawableRes)),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 40.dp),
            )
            val name = if (!selectFormula?.productName?.name.isNullOrBlank()) {
                selectFormula?.productName?.name
            } else if (!selectFormula?.productName?.nameRes.isNullOrBlank()) {
                stringResource(getStringResId(selectFormula?.productName?.nameRes!!))
            } else {
                stringResource(R.string.common_empty_string)
            }
            Text(
                text = name!!,
                fontSize = 6.nsp(),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 129.dp)
            )
        }
        Text(
            text = stringResource(
                id = R.string.statistic_product_single_counter) + "     $selectedProductCount",
            fontSize = 6.nsp(),
            color = Color.White,
            modifier = Modifier.padding(start = 90.dp, top = 355.dp)
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(top = 420.dp, start = 90.dp)
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
                    modifier = Modifier.fillMaxSize(),
                    maxItemsInEachRow = 5,
                ) {
                    currentList.forEach {
                        val select = selectFormula?.productId == it.productId
                        StatisticDrinkItem(model = it, selected = select) {
                            viewModel.getProductCount(it.productId)
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

        // ============================right=======================================
        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 132.dp, end = 90.dp)
                .width(230.dp)
                .height(50.dp),
            text = stringResource(id = R.string.statistic_reset_all)
        ) {
            viewModel.resetData()
        }
        Text(
            text = stringResource(id = R.string.statistic_last_reset, time),
            fontSize = 6.nsp(),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 218.dp, end = 90.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 805.dp, top = 295.dp)
                .width(385.dp)
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.statistic_product_coffee_counter),
                    fontSize = 5.nsp(), color = Color.White)
                Text(text = "$coffee", fontSize = 6.nsp(), color = Color.White)
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.statistic_product_hotwater_counter),
                    fontSize = 5.nsp(), color = Color.White)
                Text(text = "$hotWater", fontSize = 6.nsp(), color = Color.White)
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.statistic_product_milk_counter),
                    fontSize = 5.nsp(), color = Color.White)
                Text(text = "$milk", fontSize = 6.nsp(), color = Color.White)
            }

            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.statistic_product_foam_counter),
                    fontSize = 5.nsp(), color = Color.White)
                Text(text = "$foam", fontSize = 6.nsp(), color = Color.White)
            }

            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.statistic_product_steam_counter),
                    fontSize = 5.nsp(), color = Color.White)
                Text(text = "$steam", fontSize = 6.nsp(), color = Color.White)
            }
        }
    }
}

@Composable
private fun StatisticDrinkItem(
    model: Formula,
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
        val drawableRes = model.imageRes ?: "drink_item_empty_ic"
        Image(
            painter = painterResource(id = getImageResId(drawableRes)),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .align(Alignment.Center)
        )
    }
}

private fun lookForCount(list: List<ProductTypeCount>, type: ProductType): Int {
    val typeCount = list.find { it.type == type }
    return typeCount?.totalCount ?: 0
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewStatisticProduct() {
//    StatisticProductLayout()
}