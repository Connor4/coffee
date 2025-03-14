package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.bean.ETCSettingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ETCSettingsLayout(
    viewModel: ETCSettingViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val page by viewModel.page.collectAsState()
    val totalPageCount by viewModel.totalPageCount.collectAsState()
    val viewpagerState = rememberPagerState { totalPageCount }

    val left1Flow = 0f
    val left2Flow = 0f
    val right1Flow = 0f
    val right2Flow = 0f

    val PAGE_COUNT = 10
    val mainScreen = ScreenDisplayManager.isMainDisplay(LocalContext.current)
    val drinksList by viewModel.drinksList.collectAsState()
    val selectFormula by viewModel.formula.collectAsState()
    val tempUnit by viewModel.tempUnit.collectAsState()
    val pageCount = (drinksList.size + PAGE_COUNT - 1) / PAGE_COUNT
    val isFront by viewModel.isFront.collectAsState()

    val extractTime by viewModel.etcExtractTime.collectAsState()
    val rangeStart by viewModel.etcRangeStart.collectAsState()
    val rangeEnd by viewModel.etcRangeEnd.collectAsState()

    val blade by viewModel.blade.collectAsState()
    val adjust by viewModel.adjust.collectAsState()

    LaunchedEffect(totalPageCount) {
        viewpagerState.scrollToPage(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.bean_etc_configuration),
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

        HorizontalPager(
            userScrollEnabled = false,
            state = viewpagerState,
            modifier = Modifier
                .padding(top = 162.dp, bottom = 80.dp)
                .fillMaxSize()
        ) { _ ->
            when (page) {
                0 -> ETCSettingsPage1(left1Flow, left2Flow, right1Flow, right2Flow) {
                    // TODO 冲水
                }
                1, 4 -> ETCSettingsPage2(drinksList = drinksList, selectFormula = selectFormula,
                    isFahrenheit = tempUnit, pageCount = pageCount, isFront = isFront,
                    onSelectFormula = {
                        viewModel.getFormula(it)
                    }, onProductTest = {
                        viewModel.productTest(it, mainScreen)
                    }, onUpdateFormula = {
                        viewModel.updateFormula(it)
                    }, onLearnWater = {
//                        viewModel.learnWater()
                    }, onPowderTest = {
//                        viewModel.powderTest()
                    })
                2, 5 -> ETCSettingsPage3(currentValue = extractTime, rangeStart = rangeStart,
                    rangeEnd = rangeEnd, onValueChange = {
                        viewModel.setEtcExtractTime(it)
                    })
                3, 6 -> ETCSettingsPage4(blade = blade, adjust = adjust, isFahrenheit = tempUnit,
                    isFront = isFront, formula = selectFormula)
            }
        }

        Text(text = "${page + 1} / $totalPageCount", fontSize = 7.nsp(), color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp))

        if (page != 0) {
            ChangeColorButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 80.dp, bottom = 20.dp)
                    .width(240.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.bean_etc_settings_previous)
            ) {
                scope.launch {
//                    viewModel.loadPageContent(true)
                    viewModel.prevPage()
                }
            }
        }
        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 80.dp, bottom = 20.dp)
                .width(240.dp)
                .height(50.dp),
            text = stringResource(id = R.string.bean_etc_settings_next)
        ) {
            if ((page + 1) != totalPageCount) {
                viewModel.nextPage()
            } else {
                // FINISH
                onCloseClick()
            }

        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun ETCSettingsLayoutPreview() {
    ETCSettingsLayout()
}