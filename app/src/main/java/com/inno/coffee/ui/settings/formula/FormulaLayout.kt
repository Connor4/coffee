package com.inno.coffee.ui.settings.formula

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.PageIndicator
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.VerticalScrollList2
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaUnitValue
import kotlin.reflect.full.memberProperties

private const val PAGE_COUNT = 10

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun FormulaLayout(
    viewModel: FormulaViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val drinksTypeList by viewModel.drinksType.collectAsState()
    val selectFormula by viewModel.formula.collectAsState()
    val selectedModel = rememberSaveable { mutableStateOf<DrinksModel?>(null) }
    val totalCount = (drinksTypeList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val pagerState = rememberPagerState(pageCount = { totalCount })
    val selectTimes = rememberSaveable { mutableIntStateOf(1) }
    val placeHolder = Formula(
        productId = -1, productType = "", productName = "", vat = false,
        coffeeWater = FormulaUnitValue(0, 0f, 0f, ""),
        powderDosage = FormulaUnitValue(0, 0f, 0f, ""),
        pressWeight = FormulaUnitValue(0, 0f, 0f, ""),
        preMakeTime = FormulaUnitValue(0, 0f, 0f, ""),
        postPreMakeWaitTime = FormulaUnitValue(0, 0f, 0f, ""),
        secPressWeight = FormulaUnitValue(0, 0f, 0f, ""),
        hotWater = FormulaUnitValue(0, 0f, 0f, ""),
    )
    val scrollBarWidth = 14
    val scrollTrackHeight = 300

    val keys = listOf(
        R.string.formula_product_type,
        R.string.formula_product_name,
        R.string.formula_water_dosage,
        R.string.formula_powder_dosage,
        R.string.formula_press_weight,
        R.string.formula_pre_make_time,
        R.string.formula_pre_make_wait_time,
        R.string.formula_second_press_weight,
        R.string.formula_hot_water_dosage,
        R.string.formula_americano_seq,
        R.string.formula_coffee_cycles,
        R.string.formula_bypass_dosage,
    )
    val formulaProperties = Formula::class.memberProperties

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
        // ==============================left========================================
        Text(
            text = stringResource(id = R.string.formula_title),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(start = 89.dp, top = 188.dp)
        ) {
            repeat(2) {
                val value = it + 1
                TimesItem(text = "$value" + "x", selected = selectTimes.intValue == value) {
                    selectTimes.intValue = value
                }
            }
        }
        Box(
            modifier = Modifier.padding(start = 219.dp, top = 198.dp)
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
                    textAlign = TextAlign.Center,
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
                .padding(start = 450.dp, top = 330.dp)
        ) {
            ChangeColorButton(
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.formula_assimilation_key),
            )
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(top = 450.dp, start = 90.dp)
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
                            viewModel.getFormula(it.productId)
                        }
                    }
                }
            }

            if (totalCount > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 230.dp),
                ) {
                    PageIndicator(totalPage = totalCount, selectedPage = pagerState.currentPage)
                }
            }
        }


        // ==============================right=======================================
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 172.dp, end = 90.dp)
        ) {
            Row {
                ChangeColorButton(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    text = stringResource(id = R.string.formula_product_test),
                ) {
                    viewModel.loadFromSdCard(context)
                }
                Spacer(modifier = Modifier.width(20.dp))
                ChangeColorButton(
                    modifier = Modifier
                        .width(220.dp)
                        .height(50.dp),
                    text = stringResource(id = R.string.formula_learn_quantity),
                )
            }
        }

        UnitValueScrollBar(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 250.dp, end = 90.dp),
            unitValue = FormulaUnitValue(50, 0f, 1000f, "[tick]")) {

        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(top = 331.dp, end = 38.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(543.dp)
                    .height(293.dp),
            ) {
                val valueList = mutableListOf<Any>()
                selectFormula?.let { formula ->
                    keys.forEachIndexed { index, i ->
                        val property = formulaProperties.elementAt(index).get(formula)
                        valueList.add(property ?: "")
                    }
                }
                VerticalScrollList2(list = valueList, minimumSize = 9,
                    placeHolder = placeHolder, scrollBarWidth = scrollBarWidth,
                    scrollTrackHeight = scrollTrackHeight, listPaddingEnd = 48,
                    scrollBarPaddingEnd = 0, listItemHeight = 52f) { index, item ->

                    val color = if (index % 2 == 0) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                    FormulaItem(backgroundColor = color, description = keys[index], value = item) {

                    }
                }
            }
        }
        //=============================values=========================================
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopStart)
                .padding(top = 657.dp, start = 719.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = stringResource(id = R.string.home_left_boiler_temperature),
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.home_right_boiler_temperature),
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = " 0°C",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = " 0°C",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = stringResource(id = R.string.formula_steam_wand),
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.formula_steam_boiler),
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = " 0°C",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = " 0°C",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "0.0 ticks",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "0.0 s",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "0,0 bar",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "- mm",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "-°C/°F",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
            }
        }

    }

}

@Composable
private fun TimesItem(
    text: String,
    selected: Boolean,
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


@Composable
private fun FormulaItem(
    backgroundColor: Color,
    @StringRes description: Int,
    value: Any,
    onClick: () -> Unit = {},
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    val bgColor: Color?
    val textColor: Color?
    if (isPressed) {
        bgColor = Color(0xFF00DE93)
        textColor = Color.Black
    } else {
        bgColor = backgroundColor
        textColor = Color.White
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = bgColor)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    })
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = description), fontSize = 5.nsp(), color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 19.dp)
            )
            if (value is FormulaUnitValue) {
                Text(
                    text = "${value.value}", fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 255.dp)
                )
                Text(
                    text = value.unit, fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 385.dp)
                )
            } else {
                Text(
                    text = "$value", fontSize = 5.nsp(), color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 255.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFormula() {
    FormulaLayout()
}