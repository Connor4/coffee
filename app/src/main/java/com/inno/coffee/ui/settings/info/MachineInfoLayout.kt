package com.inno.coffee.ui.settings.info

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.INFO_KEY_ACTIVITY
import com.inno.coffee.utilities.INFO_VALUE_COFFEE_ACTIVITY
import com.inno.coffee.utilities.INFO_VALUE_STEAM_ACTIVITY
import com.inno.coffee.utilities.nsp

@Composable
fun MachineInfoLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.common_machine_info),
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

        InfoHeader()
        InfoCpu()
        InfoDisplay()
    }
}

@Composable
private fun InfoDisplay() {
    Box(
        modifier = Modifier
            .padding(top = 450.dp, start = 55.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.info_display), color = Color(0xFF32C5FF),
            fontSize = 7.nsp()
        )
        Row(
            modifier = Modifier
                .padding(top = 50.dp)
                .width(1153.dp)
                .height(238.dp)
                .background(Color(0xFF191A1D))
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .width(558.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoMarginTextWithValue(stringResource(R.string.info_sn), "HUMAI-01A")
                InfoMarginTextWithValue(stringResource(R.string.info_board), "ApalisiMX6 V1.1D")
                InfoMarginTextWithValue(stringResource(R.string.info_ext_board_id),
                    "0.9.21- main-20220721")
                InfoMarginTextWithValue(stringResource(R.string.info_ext_board_sn), "2.0.1- main")
                InfoMarginTextWithValue(stringResource(R.string.info_ext_board_sn), "0d 14h 50m")
                InfoMarginTextWithValue(stringResource(R.string.info_ext_board_sn), "31")
            }
            Column(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .width(558.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoMarginTextWithValue(stringResource(R.string.info_sn), "13262")
                InfoMarginTextWithValue(stringResource(R.string.info_sn), "10927835")
                InfoMarginTextWithValue("", "")
                InfoMarginTextWithValue("", "")
                InfoMarginTextWithValue("", "")
                InfoMarginTextWithValue("", "")
            }
        }
    }
}

@Composable
private fun InfoCpu(
    sn: String = "",
    board: String = "",
    extId: String = "",
    extSn: String = "",
) {
    Box(
        modifier = Modifier
            .padding(top = 226.dp, start = 55.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.info_cpu), color = Color(0xFF32C5FF),
            fontSize = 7.nsp()
        )
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .width(1153.dp)
                .height(133.dp)
                .background(Color(0xFF191A1D))
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoMarginTextWithValue(stringResource(R.string.info_sn), sn)
                InfoMarginTextWithValue(stringResource(R.string.info_board), board)
                InfoMarginTextWithValue(stringResource(R.string.info_ext_board_id), extId)
                InfoMarginTextWithValue(stringResource(R.string.info_ext_board_sn), extSn)
            }
            Column(
                modifier = Modifier
                    .padding(start = 620.dp)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = stringResource(R.string.info_cpu_feature), fontSize = 5.nsp(),
                    color = Color.White)
                Text(text = "-", fontSize = 5.nsp(), color = Color.White)
                Text(text = "-", fontSize = 5.nsp(), color = Color.White)
                Text(text = "-", fontSize = 5.nsp(), color = Color.White)
            }
        }
    }
}

@Composable
private fun InfoHeader(
    sn: String = "01166000",
    sn2: String = "22263 E0001",
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .padding(top = 172.dp, start = 55.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.info_sn_machine), color = Color(0xFF32C5FF),
            fontSize = 7.nsp(), modifier = Modifier.align(Alignment.CenterStart)
        )
        Text(text = sn, color = Color.White, fontSize = 5.nsp(),
            modifier = Modifier
                .padding(start = 300.dp)
                .align(Alignment.CenterStart)
        )
        Text(text = sn2, color = Color.White, fontSize = 7.nsp(),
            modifier = Modifier
                .padding(start = 400.dp)
                .align(Alignment.CenterStart)
        )
        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 335.dp)
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.info_coffee_status)
        ) {
            ScreenDisplayManager.autoRoute(context, MachineInfoStatusActivity::class.java,
                Bundle().apply {
                    putString(INFO_KEY_ACTIVITY, INFO_VALUE_COFFEE_ACTIVITY)
                })
        }
        Spacer(modifier = Modifier.width(20.dp))
        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 95.dp)
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.info_steam_status),
        ) {
            ScreenDisplayManager.autoRoute(context, MachineInfoStatusActivity::class.java,
                Bundle().apply {
                    putString(INFO_KEY_ACTIVITY, INFO_VALUE_STEAM_ACTIVITY)
                })
        }
    }

}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineInfoLayout() {
    MachineInfoLayout()
}