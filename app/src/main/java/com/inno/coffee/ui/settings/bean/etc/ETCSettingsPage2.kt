package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.settings.formula.DrinkPager
import com.inno.coffee.ui.settings.formula.FormulaValueItem
import com.inno.coffee.utilities.FORMULA_SHOW_LEARN_WATER
import com.inno.coffee.utilities.FORMULA_SHOW_POWDER_TEST
import com.inno.coffee.utilities.nsp
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
        viewModel.loadETCDrinkList(mainScreen, true)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.bean_etc_settings_2_front_bean_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 90.dp, top = 120.dp)
                .width(269.dp)
                .wrapContentHeight()
        )
        Image(painter = painterResource(id = R.drawable.etc_setting_grinder_ic),
            modifier = Modifier
                .padding(start = 440.dp, top = 40.dp)
                .width(216.dp)
                .height(226.dp),
            contentDescription = null
        )
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