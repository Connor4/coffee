package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.inno.coffee.ui.home.DrinkItem
import com.inno.coffee.ui.settings.formula.FormulaValueItem
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem

private const val PAGE_COUNT = 10

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ETCSettingsPage4(
    viewModel: FormulaViewModel = hiltViewModel(),
) {
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val drinksTypeList by viewModel.drinksList.collectAsState()
    val selectFormula by viewModel.formula.collectAsState()
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val tempUnit by viewModel.tempUnit.collectAsState()
    val blade = 3.32
    val adjust = 2

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.bean_etc_settings_4_check_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 90.dp, top = 10.dp)
                .width(269.dp)
                .wrapContentHeight()
        )

        Text(text = stringResource(R.string.bean_etc_settings_4_press_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 90.dp, top = 70.dp)
                .width(200.dp)
                .height(190.dp)
        )

        DrinkItem(
            formula = Formula(productId = 4, imageRes = "operate_rinse_ic",
                productName = FormulaItem.FormulaProductName(name = "",
                    nameRes = "home_item_rinse")),
            normalSize = false,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 90.dp, top = 160.dp),
            onDrinkClick = { }
        )

        DrinkItem(
            formula = Formula(productId = 4, imageRes = "etc_setting_auto_adjust_ic",
                productName = FormulaItem.FormulaProductName(name = "",
                    nameRes = "bean_etc_settings_4_auto_adjust")),
            normalSize = false,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 90.dp, top = 360.dp),
            onDrinkClick = { }
        )

        Text(text = stringResource(R.string.bean_etc_settings_4_blade_grinder_capacity) + blade +
                " [mm/s]",
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 370.dp, top = 400.dp)
                .wrapContentSize()
        )

        Text(text = stringResource(R.string.bean_etc_settings_4_adjust_thickness) + adjust,
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 370.dp, top = 440.dp)
                .wrapContentSize()
        )

        FormulaValueItem(isFahrenheit = tempUnit, selectFormula = selectFormula,
            fromETCSetting = true,
            onValueChange = {
            }, onProductTest = {
            }, onLearn = { index ->
            })
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun ETCSetting4Preview() {
    ETCSettingsPage4()
}