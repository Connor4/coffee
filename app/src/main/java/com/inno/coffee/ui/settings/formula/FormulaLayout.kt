package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel

private const val PAGE_COUNT = 10

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FormulaLayout(
    viewModel: FormulaViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val drinksTypeList by viewModel.drinksType.collectAsState()
    val selectFormula by viewModel.formula.collectAsState()
    val selectedModel = rememberSaveable { mutableStateOf<DrinksModel?>(null) }
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val selectTimes = rememberSaveable { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        if (drinksTypeList.isNotEmpty()) {
            selectedModel.value = drinksTypeList.first()
            val productId = selectedModel.value?.productId
            viewModel.getFormula(productId ?: -1)
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
        FormulaTimesSelector(selectTimes.value) {
            selectTimes.value = it
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(start = 450.dp, top = 330.dp)
        ) {
            ChangeColorButton(
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.formula_assimilation_key),
            )
        }
        FormulaDrinkPage(selectedModel.value, totalCount, pagerState, drinksTypeList) {
            selectedModel.value = it
            viewModel.getFormula(it.productId)
        }
        FormulaValuesDisplay()
        FormulaValueItem(selectFormula) {
            selectFormula?.let {
                viewModel.updateFormula(it)
            }
        }
    }

}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFormula() {
    FormulaLayout()
}