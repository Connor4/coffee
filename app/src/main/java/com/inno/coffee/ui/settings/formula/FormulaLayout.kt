package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ConfirmDialogLayout
import com.inno.coffee.utilities.FORMULA_SHOW_LEARN_WATER
import com.inno.coffee.utilities.FORMULA_SHOW_POWDER_TEST
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel

private const val PAGE_COUNT = 10

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FormulaLayout(
    viewModel: FormulaViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val drinksTypeList by viewModel.drinksList.collectAsState()
    val selectFormula by viewModel.formula.collectAsState()
//    val allFormula by viewModel.formulaList.collectAsState()
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val openConfirmDialog = remember { mutableStateOf(false) }
    val selectedCups = remember { mutableStateOf(1) }
    val leftTemp by viewModel.leftTemp.collectAsState()
    val rightTemp by viewModel.rightTemp.collectAsState()
    val wandTemp by viewModel.wandTemp.collectAsState()
    val steamPressure by viewModel.steamPressure.collectAsState()
    val tempUnit by viewModel.tempUnit.collectAsState()
    val updateFlag by viewModel.updateFormulaFlag.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDrinkTypeList(mainScreen)
    }

    LaunchedEffect(updateFlag) {
        selectFormula?.let {
            viewModel.refreshDrinkTypeList(mainScreen)
            viewModel.getFormula(it.productId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        FunctionHeader(text = stringResource(id = R.string.formula_title)) {
            onCloseClick()
        }
        FormulaCupSelectorLayout(selectFormula) { cups ->
            openConfirmDialog.value = true
            selectedCups.value = cups
        }
        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 450.dp, top = 330.dp)
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.formula_assimilation_key),
        ) {
            viewModel.assimilationProduct(selectFormula)
        }
        FormulaDrinkPage(selectFormula, totalCount, pagerState, drinksTypeList) {
            viewModel.getFormula(it.productId)
        }
        FormulaValuesDisplay(leftCoffee = leftTemp, rightCoffee = rightTemp, wand = wandTemp,
            steam = steamPressure)
        FormulaValueItem(isFahrenheit = tempUnit, selectFormula = selectFormula, onValueChange = {
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
        }, onChangeType = { type ->
            viewModel.updateNewProductType(type, selectFormula)
        })
        if (openConfirmDialog.value) {
            ConfirmDialogLayout(
                stringResource(R.string.formula_product_cups_title),
                stringResource(R.string.formula_product_cups_change_tips), {
                    openConfirmDialog.value = false
                    viewModel.setFormulaCups(selectedCups.value, selectFormula)
                }, {
                    openConfirmDialog.value = false
                }
            )
        }
    }

}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFormula() {
    FormulaLayout()
}