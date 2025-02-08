package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.settings.formula.DrinkPager
import com.inno.coffee.ui.settings.formula.FormulaValueItem
import com.inno.coffee.utilities.FORMULA_SHOW_LEARN_WATER
import com.inno.coffee.utilities.FORMULA_SHOW_POWDER_TEST
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel


private const val PAGE_COUNT = 10

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ETCSettingsPage2(
    viewModel: FormulaViewModel = hiltViewModel(),
) {
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val drinksTypeList by viewModel.drinksList.collectAsState()
    val selectFormula by viewModel.formula.collectAsState()
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val tempUnit by viewModel.tempUnit.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDrinkTypeList(mainScreen)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DrinkPager(selectFormula, totalCount, pagerState, drinksTypeList, 300) {
            viewModel.getFormula(it.productId)
        }
        FormulaValueItem(isFahrenheit = tempUnit, selectFormula = selectFormula,
            fromETCSetting = true,
            onValueChange = {
                selectFormula?.let {
                    viewModel.updateFormula(it)
                }
            }, onProductTest = {
                selectFormula?.let {
                    viewModel.productTest(it, mainScreen)
                }
            }, onLearn = { index ->
                when (index) {
                    FORMULA_SHOW_LEARN_WATER -> {
                        viewModel.learnWater()
                    }
                    FORMULA_SHOW_POWDER_TEST -> {
                        viewModel.powderTest()
                    }
                }
            })
    }
}

@Preview
@Composable
private fun ETCSetting2Preview() {
    ETCSettingsPage2()
}