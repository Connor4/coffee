package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.utilities.VerticalScrollList
import com.inno.coffee.utilities.composeClick
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.statistics.StatisticProductViewModel

private const val PAGE_COUNT = 10

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun FormulaLayout(
    viewModel: StatisticProductViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val drinksTypeList by viewModel.drinksType.collectAsState()
    val selectedModel = remember { mutableStateOf<DrinksModel?>(null) }
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        // ==============================left========================================
        Text(
            text = stringResource(id = R.string.formula_title),
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
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(start = 89.dp, top = 128.dp)
        ) {
            repeat(2) {
                TimesItem(text = "${it + 1}" + "x")
            }
        }
        Box(
            modifier = Modifier.padding(start = 219.dp, top = 138.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .border(1.dp, Color(0xFF484848), RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color(0xFF191A1D)),
            ) {
                val drawableRes = selectedModel.value?.imageRes ?: R.drawable.drink_espresso_ic
                val stringRes = selectedModel.value?.name ?: R.string.home_item_espresso
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 40.dp),
                )
                Text(
                    text = stringResource(id = stringRes),
                    fontSize = 6.nsp(),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 129.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(start = 450.dp, top = 270.dp)
        ) {
            Button(
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                colors = ButtonColors(
                    Color(0xFF191A1D), Color(0xFF191A1D),
                    Color(0xFF191A1D), Color(0xFF191A1D),
                ),
                border = BorderStroke(1.dp, Color(0xFF484848)),
                shape = RoundedCornerShape(10.dp),
                onClick = composeClick {
                },
            ) {
                Text(
                    text = stringResource(id = R.string.formula_assimilation_key),
                    fontSize = 5.nsp(),
                    color = Color.White
                )
            }
        }

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
                    modifier = Modifier
                        .fillMaxSize(),
                    maxItemsInEachRow = 5,
                ) {
                    currentList.forEach {
                        val select = selectedModel.value?.productId == it.productId
                        FormulaDrinkItem(model = it, selected = select) {
                            selectedModel.value = it
                            viewModel.getProductCount(it.productId)
                        }
                    }
                }
            }
        }


        // ==============================right=======================================
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 132.dp, end = 90.dp)
        ) {
            Row {
                Button(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    colors = ButtonColors(
                        Color(0xFF191A1D), Color(0xFF191A1D),
                        Color(0xFF191A1D), Color(0xFF191A1D),
                    ),
                    border = BorderStroke(1.dp, Color(0xFF484848)),
                    shape = RoundedCornerShape(10.dp),
                    onClick = composeClick {

                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.formula_product_test),
                        fontSize = 5.nsp(),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    colors = ButtonColors(
                        Color(0xFF191A1D), Color(0xFF191A1D),
                        Color(0xFF191A1D), Color(0xFF191A1D),
                    ),
                    border = BorderStroke(1.dp, Color(0xFF484848)),
                    shape = RoundedCornerShape(10.dp),
                    onClick = composeClick {

                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.formula_learn_quantity),
                        fontSize = 5.nsp(),
                        color = Color.White
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 271.dp, end = 38.dp)
        ) {
            VerticalScrollList()
        }

    }

}

@Composable
private fun TimesItem(
    text: String,
    selected: Boolean = true,
    onTimeClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp)
            .debouncedClickable({ onTimeClick() }),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(
                painter = painterResource(
                    id = R.drawable.setting_statistic_formula_times_selected_bg),
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(
                    id = R.drawable.setting_statistic_formula_times_normal_bg),
                contentDescription = null,
                modifier = Modifier
                    .width(101.dp)
                    .height(84.dp)
            )
        }
        Text(text = text, fontSize = 10.nsp(), color = Color.White)
    }
}

@Composable
private fun FormulaDrinkItem(
    model: DrinksModel,
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
        Image(
            painter = painterResource(id = model.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewFormula() {
    FormulaLayout()
}