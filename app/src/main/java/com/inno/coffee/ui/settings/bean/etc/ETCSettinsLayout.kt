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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ETCSettingsLayout(
    onCloseClick: () -> Unit = {},
) {
    val pageCount = 5
    val pagerState = rememberPagerState { pageCount }
    val scope = rememberCoroutineScope()

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
            state = pagerState,
            modifier = Modifier
                .padding(top = 162.dp, bottom = 80.dp)
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ETCSettingsPage1()
                1 -> ETCSettingsPage2()
                2 -> ETCSettingsPage3()
                3 -> ETCSettingsPage4()
                4 -> ETCSettingsPage5()
            }
        }

        if (pagerState.currentPage != 0) {
            ChangeColorButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 80.dp, bottom = 20.dp)
                    .width(240.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.bean_etc_settings_previous)
            ) {
                scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage - 1)
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
            if (pagerState.currentPage < pageCount - 1) {
                scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage + 1)
                }
            } else {
                // FINISH
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun ETCSettingsLayoutPreview() {
    ETCSettingsLayout()
}