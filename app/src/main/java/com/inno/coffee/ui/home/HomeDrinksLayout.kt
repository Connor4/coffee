package com.inno.coffee.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R

private const val TOTAL_PAGE = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeDrinksLayout(
) {
    val pagerState = rememberPagerState(pageCount = { TOTAL_PAGE })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(670.dp)
            .background(color = Color(0xFF2C2C2C))
    ) {
        HorizontalPager(state = pagerState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 23.dp),
        ) {
            HomePageIndicator(pagerState.currentPage)
        }
    }
}

@Composable
private fun HomePageIndicator(
    selectedPage: Int = 0,
) {
    Row {
        for (i in 0 until TOTAL_PAGE) {
            Image(
                painter = if (i == selectedPage) painterResource(id = R.drawable
                    .home_page_indicator_selected_ic)
                else painterResource(id = R.drawable.home_page_indicator_normal_ic),
                contentDescription = null
            )
            if (i in 0 until TOTAL_PAGE - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewHomeDrinks() {
    HomeDrinksLayout()
}