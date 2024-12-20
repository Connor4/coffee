package com.inno.coffee.ui.settings.maintenance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.utilities.nsp

@Composable
fun TestResultLayout(
    title: String = "",
    min: Int = 0,
    max: Int = 0,
    average: Int = 0,
    offset: Int = 0,
    success: Boolean = false,
    onCloseClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.common_list_select_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(834.dp)
                .height(512.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(454.dp)
                .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 7.nsp(),
                color = Color.White, modifier = Modifier.padding(start = 40.dp, top = 20.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(500.dp)
                    .wrapContentHeight(),
            ) {
                Item(stringResource(id = R.string.maintenance_test_min), min, 650)
                Spacer(modifier = Modifier.height(10.dp))
                Item(stringResource(id = R.string.maintenance_test_max), max, 650)
                Spacer(modifier = Modifier.height(10.dp))
                Item(stringResource(id = R.string.maintenance_test_average), average, 650)
                Spacer(modifier = Modifier.height(10.dp))
                Item(stringResource(id = R.string.maintenance_test_oscillation), offset, 7)
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = if (success) stringResource(R.string.maintenance_test_milk_test_success)
                    else stringResource(R.string.maintenance_test_milk_test_failed),
                    fontSize = 7.nsp(), color = if (success) Color.Green else Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .width(180.dp)
                    .height(50.dp),
                colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C), Color.Green,
                    Color.Magenta),
                shape = RoundedCornerShape(10.dp),
                onClick = composeClick { onCloseClick() },
            ) {
                Text(text = stringResource(id = R.string.common_button_confirm),
                    fontSize = 5.nsp(), color = Color.White)
            }
        }
    }
}

@Composable
private fun Item(text: String = "", value: Int, standardValue: Int) {
    val valueTextColor = if (value > standardValue) {
        Color.Red
    } else {
        Color.Green
    }
    Row {
        Text(
            text = text, fontWeight = FontWeight.Bold,
            fontSize = 7.nsp(),
            color = Color.White,
            modifier = Modifier
                .width(240.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(60.dp))
        Text(
            text = "$value", fontSize = 7.nsp(), color = valueTextColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .width(70.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = "< $standardValue",
            fontWeight = FontWeight.Bold, fontSize = 4.nsp(), color = Color.White,
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240", locale = "zh")
@Composable
private fun PreviewTestResultLayout() {
    TestResultLayout("title", onCloseClick = {})
}