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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.viewmodel.home.HomeViewModel
import kotlinx.coroutines.flow.combine

private const val PAGE_COUNT = 12

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomeDrinksLayout(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val combinedState by combine(
        viewModel.drinksTypes,
        SelfCheckManager.checking,
        SelfCheckManager.releaseSteam,
        if (mainScreen) MakeLeftDrinksHandler.size else MakeRightDrinksHandler.size,
    ) { drinksList, checking, releaseSteam, size ->
        CombinedState(drinksList, checking, releaseSteam, size)
    }.collectAsState(initial = CombinedState(emptyList(), false, 0, 0))
    val totalCount = (combinedState.drinksList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val selected = rememberSaveable { mutableIntStateOf(INVALID_INT) }

    if (combinedState.size < 1) {
        selected.intValue = INVALID_INT
    }

    if (combinedState.releaseSteam == 1 || combinedState.releaseSteam == 2) {
        ReleaseSteamLayout {
            viewModel.selfCheckReleaseSteam()
        }
        return
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(670.dp)
                .background(color = Color(0xFF2C2C2C))
        ) {
            HorizontalPager(state = pagerState) { page ->
                val fromIndex = page * PAGE_COUNT
                val toIndex = minOf(fromIndex + PAGE_COUNT, combinedState.drinksList.size)
                val currentList = combinedState.drinksList.subList(fromIndex, toIndex)
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp, top = 27.dp, end = 40.dp, bottom = 42.dp),
                    maxItemsInEachRow = 4,
                ) {
                    currentList.forEach { drinkModel ->
                        val enable =
                            viewModel.enableMask(combinedState.size > 0, combinedState.checking,
                                drinkModel)
                        val select = selected.intValue == drinkModel.productId

                        DrinkItem(model = drinkModel, enableMask = enable, selected = select) {
                            selected.intValue = drinkModel.productId
                            viewModel.startMakeDrink(drinkModel, mainScreen, combinedState.checking)
                        }
                    }
                }
            }

            if (totalCount > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 23.dp),
                ) {
                    HomePageIndicator(totalPage = totalCount, selectedPage = pagerState.currentPage)
                }
            }
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

data class CombinedState(
    val drinksList: List<DrinksModel>,
    val checking: Boolean,
    val releaseSteam: Int,
    val size: Int
)

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewHomeDrinks() {
    HomeDrinksLayout()
}