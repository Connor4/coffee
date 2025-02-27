package com.inno.coffee.ui.settings.config

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.config.MilkSettingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MilkSettingLayout(
    viewModel: MilkSettingViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val page by viewModel.page.collectAsState()
    val viewpagerState = rememberPagerState { 7 }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.config_milk_setting),
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
                0 -> MilkSettingPage1(viewModel)
                1 -> MilkSettingPage2(viewModel)
                2 -> MilkSettingPage3(viewModel)
                3 -> MilkSettingPage4(viewModel)
                4 -> MilkSettingPage5()
                5 -> MilkSettingPage6(viewModel)
                6 -> MilkSettingPage7(viewModel)
            }
        }

        Text(text = "${page + 1} / 7", fontSize = 7.nsp(), color = Color.White,
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
            if ((page + 1) != 7) {
                viewModel.nextPage()
            } else {
                // FINISH
                onCloseClick()
            }

        }

    }
}

@Preview
@Composable
private fun PreviewMilkSettingLayout() {
    MilkSettingLayout()
}