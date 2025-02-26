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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.settings.formula.DrinkPager
import com.inno.coffee.ui.settings.formula.FormulaValueItem
import com.inno.coffee.utilities.FORMULA_SHOW_LEARN_WATER
import com.inno.coffee.utilities.FORMULA_SHOW_POWDER_TEST
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ETCSettingsPage2(
    drinksList: List<Formula> = emptyList(),
    selectFormula: Formula?,
    isFahrenheit: Boolean = false,
    pageCount: Int = 0,
    isFront: Boolean = false,
    onSelectFormula: (Int) -> Unit = {},
    onUpdateFormula: (Formula) -> Unit = {},
    onLearnWater: () -> Unit = {},
    onPowderTest: () -> Unit = {},
    onProductTest: (Formula) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.bean_etc_settings_2_front_bean_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 70.dp, top = 120.dp)
                .width(269.dp)
                .wrapContentHeight()
        )
        val grinderDrawable = if (isFront) {
            R.drawable.etc_setting_front_grinder_ic
        } else {
            R.drawable.etc_setting_rear_grinder_ic
        }
        Image(painter = painterResource(id = grinderDrawable),
            modifier = Modifier
                .padding(start = 440.dp, top = 40.dp)
                .width(216.dp)
                .height(226.dp),
            contentDescription = null
        )
        DrinkPager(selectFormula, pageCount, pagerState, drinksList, 300, 70) {
//            viewModel.getFormula(it.productId)
            onSelectFormula(it.productId)
        }
        FormulaValueItem(isFahrenheit = isFahrenheit, selectFormula = selectFormula,
            fromETCSetting = true,
            onValueChange = {
                selectFormula?.let {
//                    viewModel.updateFormula(it)
                    onUpdateFormula(it)
                }
            }, onProductTest = {
                selectFormula?.let {
//                    viewModel.productTest(it, mainScreen)
                    onProductTest(it)
                }
            }, onLearn = { index ->
                when (index) {
                    FORMULA_SHOW_LEARN_WATER -> {
//                        viewModel.learnWater()
                        onLearnWater()
                    }
                    FORMULA_SHOW_POWDER_TEST -> {
//                        viewModel.powderTest()
                        onPowderTest()
                    }
                }
            })
    }
}

@Preview
@Composable
private fun ETCSetting2Preview() {
    ETCSettingsPage2(emptyList(), Formula(productId = 4, imageRes = "operate_rinse_ic",
        productName = FormulaItem.FormulaProductName(name = "",
            nameRes = "home_item_rinse")), false, 0)
}