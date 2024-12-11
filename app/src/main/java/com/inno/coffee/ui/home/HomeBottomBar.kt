package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.notice.GlobalDialogLeftManager
import com.inno.coffee.utilities.nsp

@Composable
fun HomeBottomBar(
    extractionTime: Float,
    leftTemp: String,
    rightTemp: String,
    showExtractionTime: Boolean = true,
    showGrinderButton: Boolean = false,
    onReleaseSteam: () -> Unit = {},
    onClickWarning: () -> Unit = {},
    onClickStop: () -> Unit = {},
    onClickGrinder: () -> Unit = {},
) {
    val operateRinse by SelfCheckManager.operateRinse.collectAsState()
    val coffeeHeating by SelfCheckManager.coffeeHeating.collectAsState()
    val steamHeating by SelfCheckManager.steamHeating.collectAsState()
    val warningList by GlobalDialogLeftManager.getInstance().warningExist.collectAsState()
//    val operateRinse = true
//    val coffeeHeating = false
//    val steamHeating = false
//    val warningList = false

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color(0xFF000002)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End)
                        .size(60.dp)
                        .debouncedClickable({ onClickStop() }),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_stop_normal_ic),
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.size(44.dp)
                    )
                }
                if (operateRinse && !coffeeHeating && !steamHeating) {
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp),
                        colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C),
                            Color(0xFF2C2C2C), Color(0xFF2C2C2C)),
                        shape = RoundedCornerShape(10.dp),
                        onClick = composeClick {

                        },
                    ) {
                        Text(text = "*Decaffeinated", fontSize = 5.nsp(), color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier
                            .width(320.dp)
                            .height(50.dp),
                        colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C), Color.Green,
                            Color.Magenta),
                        shape = RoundedCornerShape(10.dp),
                        onClick = composeClick { /*TODO*/ },
                    ) {
                        Text(text = "*Press first for Second Milk", fontSize = 5.nsp(),
                            color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    if (showExtractionTime) {
                        Text(text = String.format("%.1f s", extractionTime), fontSize = 6.nsp(),
                            color = Color.White)
                    }
                }
            }

            Row {
                if (showGrinderButton && operateRinse && !coffeeHeating && !steamHeating) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End)
                            .size(60.dp)
                            .debouncedClickable({ onClickGrinder() }),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.home_grinder_ic),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(80.dp))
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = stringResource(id = R.string.home_left_boiler_temperature),
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
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
                        text = " $leftTemp",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = " $rightTemp",
                        fontSize = 4.nsp(),
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End)
                        .size(60.dp)
                        .debouncedClickable({ onReleaseSteam() }),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_release_steam_ic),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End)
                        .size(60.dp)
                        .debouncedClickable({ onClickWarning() }, enabled = warningList),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = if (warningList) painterResource(
                            id = R.drawable.home_warning_exist_ic)
                        else painterResource(id = R.drawable.home_warning_normal_ic),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
            }
        }

        if (!operateRinse) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 6.nsp())) {
                        append(stringResource(id = R.string.home_self_check_rinse_first_text) + " ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF01D88F), fontSize = 6.nsp())) {
                        append(stringResource(id = R.string.home_self_check_rinse_second_text))
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 6.nsp())) {
                        append(" " + stringResource(id = R.string.home_self_check_rinse_third_text))
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (coffeeHeating) {
            Text(
                text = stringResource(id = R.string.home_self_check_boiler_heating),
                fontSize = 6.nsp(),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (steamHeating) {
            Text(
                text = stringResource(id = R.string.home_self_check_steam_heating),
                fontSize = 6.nsp(),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewHomeBottomBar() {
    HomeBottomBar(11.33f, "80", "81")
}