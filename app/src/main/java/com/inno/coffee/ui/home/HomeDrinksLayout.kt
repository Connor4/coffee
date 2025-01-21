package com.inno.coffee.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.function.selfcheck.SelfCheckManager.RELEASE_STEAM_READY
import com.inno.coffee.function.selfcheck.SelfCheckManager.RELEASE_STEAM_START
import com.inno.coffee.ui.common.ConfirmDialogLayout
import com.inno.coffee.ui.common.PageIndicator
import com.inno.coffee.ui.home.selfcheck.ReleaseSteamLayout
import com.inno.coffee.utilities.DISPLAY_PER_PAGE_COUNT_12
import com.inno.coffee.viewmodel.home.HomeViewModel
import com.inno.common.enums.ProductType
import kotlinx.coroutines.delay

private const val PAGE_WAIT_TIME = 10000L

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomeDrinksLayout(
    viewModel: HomeViewModel = hiltViewModel(),
    onShowRinseDialog: () -> Unit = {},
) {
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val size by if (mainScreen) {
        MakeLeftDrinksHandler.size.collectAsState()
    } else {
        MakeRightDrinksHandler.size.collectAsState()
    }
    val executingQueue = if (mainScreen) {
        val list1 = MakeLeftDrinksHandler.productQueue.collectAsState()
        val list2 = MakeLeftDrinksHandler.operationQueue.collectAsState()
        list1.value.plus(list2.value)
    } else {
        val list1 = MakeRightDrinksHandler.productQueue.collectAsState()
        val list2 = MakeRightDrinksHandler.operationQueue.collectAsState()
        list1.value.plus(list2.value)
    }
    val autoBack = viewModel.autoReturnEnabled.collectAsState(initial = false)
    val showProductPrice by viewModel.showProductPrice.collectAsState(initial = false)
//    val showProductName by viewModel.showProductName.collectAsState(initial = false)
    val numberOfPage by viewModel.numberOfProductPerPage.collectAsState(
        initial = DISPLAY_PER_PAGE_COUNT_12)
    val drinksList by viewModel.formulaList.collectAsState()
    val cleanMachine by SelfCheckManager.washMachine.collectAsState()
    val putWashPill by SelfCheckManager.lackWashPill.collectAsState()
    val releaseSteam by SelfCheckManager.releaseSteam.collectAsState()
    val totalPage = (drinksList.size + numberOfPage - 1) / numberOfPage
    val rowCount = if (numberOfPage == DISPLAY_PER_PAGE_COUNT_12) 4 else 5
    val normalSize = numberOfPage == DISPLAY_PER_PAGE_COUNT_12
    val pagerState = rememberPagerState(pageCount = { totalPage })

    // auto back to first page
    LaunchedEffect(pagerState.currentPage, autoBack.value) {
        if (size <= 0 && autoBack.value && releaseSteam != RELEASE_STEAM_READY
                && releaseSteam != RELEASE_STEAM_START) {
            delay(PAGE_WAIT_TIME)
            pagerState.scrollToPage(0)
        }
    }

    if (cleanMachine || putWashPill) {
        val title = if (cleanMachine) {
            stringResource(id = R.string.home_wash_machine_title)
        } else {
            stringResource(id = R.string.home_lack_wash_pill_title)
        }
        val description = if (cleanMachine) {
            stringResource(id = R.string.home_wash_machine_content)
        } else {
            stringResource(id = R.string.home_lack_wash_pill_content)
        }
        ConfirmDialogLayout(title = title, description = description, {
            if (cleanMachine) {
                viewModel.selfCheckWashMachine()
            } else {
                viewModel.selfCheckPutWashPill()
            }
        }, {}, showCancelButton = false)
    } else if (releaseSteam == RELEASE_STEAM_READY || releaseSteam == RELEASE_STEAM_START) {
        ReleaseSteamLayout(normalSize = normalSize) {
            viewModel.selfCheckReleaseSteam()
        }
    } else {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(670.dp)
            .background(color = Color(0xFF2C2C2C))
        ) {
            HorizontalPager(state = pagerState) { page ->
                val fromIndex = page * numberOfPage
                val toIndex = minOf(fromIndex + numberOfPage, drinksList.size)
                val currentList = drinksList.subList(fromIndex, toIndex)
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp, top = 27.dp, end = 40.dp, bottom = 42.dp),
                    maxItemsInEachRow = rowCount,
                ) {
                    currentList.forEach { drinkModel ->
                        val enable = viewModel.enableMask(drinkModel, executingQueue)
                        val select = viewModel.enableSelect(mainScreen, drinkModel)

                        DrinkItem(
                            formula = drinkModel, enableMask = enable, selected = select,
                            normalSize = normalSize, showProductName = true,
                            showProductPrice = showProductPrice,
                            onDrinkClick = {
                                viewModel.startMakeDrink(drinkModel, mainScreen)
                            },
                            onDrinkLongClick = {
                                val showRinse =
                                    ProductType.assertType(drinkModel.productType?.type,
                                        ProductType.RINSE)
                                if (showRinse) {
                                    onShowRinseDialog()
                                }
                            }
                        )
                    }
                }
            }

            if (totalPage > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 23.dp),
                ) {
                    PageIndicator(totalPage = totalPage, selectedPage = pagerState.currentPage)
                }
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewHomeDrinks() {
    HomeDrinksLayout()
}