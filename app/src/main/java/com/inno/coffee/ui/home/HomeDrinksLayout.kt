package com.inno.coffee.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.viewmodel.home.HomeViewModel

private const val PAGE_COUNT = 12

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomeDrinksLayout(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val drinksList by viewModel.drinksTypes.collectAsState()
    val totalCount = (drinksList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val selected = remember { mutableIntStateOf(INVALID_INT) }
    val size by if (mainScreen) {
        MakeLeftDrinksHandler.size.collectAsState()
    } else {
        MakeRightDrinksHandler.size.collectAsState()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(670.dp)
            .background(color = Color(0xFF2C2C2C))
    ) {
        HorizontalPager(state = pagerState) { page ->
            val fromIndex = page * PAGE_COUNT
            val toIndex = minOf(fromIndex + PAGE_COUNT, drinksList.size)
            val currentList = drinksList.subList(fromIndex, toIndex)
            FlowRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, top = 27.dp, end = 40.dp, bottom = 42.dp),
                maxItemsInEachRow = 4,
            ) {
                repeat(currentList.size) {
                    val drinkModel = currentList[it]
                    val enable = viewModel.enableMask(size > 0, drinkModel)
                    val select = selected.intValue == drinkModel.productId

                    DrinkItem(model = drinkModel, enableMask = enable, selected = select) { model ->
                        if (!enable) {
                            selected.intValue = model.productId
                            viewModel.startMakeDrink(model, mainScreen)
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 23.dp),
        ) {
            HomePageIndicator(totalPage = totalCount, selectedPage = pagerState.currentPage)
        }
    }
}

@Composable
private fun HomePageIndicator(
    totalPage: Int,
    selectedPage: Int = 0,
) {
    Row {
        repeat(totalPage) {
            Image(
                painter = if (it == selectedPage) painterResource(id = R.drawable
                    .home_page_indicator_selected_ic)
                else painterResource(id = R.drawable.home_page_indicator_normal_ic),
                contentDescription = null
            )
            if (it in 0 until totalPage - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewHomeDrinks() {
    HomeDrinksLayout()
}