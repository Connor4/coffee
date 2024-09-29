package com.inno.coffee.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.inno.coffee.R

@Composable
fun PageIndicator(
    totalPage: Int,
    selectedPage: Int = 0,
) {
    Row {
        repeat(totalPage) {
            Image(
                painter = if (it == selectedPage) painterResource(id = R.drawable
                    .home_page_indicator_selected_ic)
                else painterResource(id = R.drawable.home_page_indicator_normal_ic),
                contentDescription = null
            )
            if (it in 0 until totalPage - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}