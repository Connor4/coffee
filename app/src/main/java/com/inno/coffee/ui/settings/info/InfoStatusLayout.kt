package com.inno.coffee.ui.settings.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun CoffeeStatusLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.info_coffee_status),
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

        CoffeeStatus1()
        CoffeeStatus2()
        CoffeeStatus3()
    }
}

@Composable
private fun CoffeeStatus3() {
    Box(
        modifier = Modifier
            .padding(top = 434.dp, start = 55.dp)
            .width(1153.dp)
            .height(328.dp)
            .background(Color(0xFF191A1D))
    ) {

    }
}

@Composable
private fun CoffeeStatus2() {
    Box(
        modifier = Modifier
            .padding(top = 320.dp, start = 55.dp)
            .width(1153.dp)
            .height(94.dp)
            .background(Color(0xFF191A1D))
    ) {

    }
}

@Composable
private fun CoffeeStatus1(

) {
    Box(
        modifier = Modifier
            .padding(top = 180.dp, start = 55.dp)
            .width(1153.dp)
            .height(120.dp)
            .background(Color(0xFF191A1D))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp)
                .width(600.dp)
                .wrapContentHeight(),
        ) {
            Column {

            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewInfoStatus() {
    CoffeeStatusLayout()
}