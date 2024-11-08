package com.inno.coffee.ui.settings.machinetest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.inno.coffee.ui.common.TextWithValue
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestViewModel

@Composable
fun MachineTestSteamInputLayout(
    viewModel: MachineTestViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {

    val okColor = Color(0xFF6DD400)
    val failColor = Color.Red

    val ok = stringResource(R.string.machine_test_ok)
    val warning = stringResource(R.string.machine_test_warning)

    val steamPressure = viewModel.steamPressure.collectAsState()
    val securityLevel = viewModel.securityLevel.collectAsState()
    val workLevel = viewModel.workLevel.collectAsState()
    val leftTemp = viewModel.leftWandTemp.collectAsState()
    val rightTemp = viewModel.rightWandTemp.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_steam_inputs),
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

        TextWithValue(
            stringResource(R.string.machine_test_steam_pressure),
            "${steamPressure.value}",
            unit = "bar",
            modifier = Modifier.padding(top = 179.dp, start = 55.dp),
        )

        Box(
            modifier = Modifier
                .padding(top = 247.dp, start = 55.dp)
                .width(1153.dp)
                .height(133.dp)
                .background(Color(0xFF191A1D))
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                TextWithValue(
                    stringResource(R.string.machine_test_security_level),
                    if (securityLevel.value) ok else warning,
                    textColor = if (securityLevel.value) okColor else failColor,
                )
                TextWithValue(
                    stringResource(R.string.machine_test_work_level),
                    if (workLevel.value) ok else warning,
                    textColor = if (workLevel.value) okColor else failColor,
                )
                TextWithValue(
                    "", "", "",
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 400.dp, start = 55.dp)
                .width(1153.dp)
                .height(166.dp)
                .background(Color(0xFF191A1D))
        ) {
            TextWithValue(
                stringResource(R.string.machine_test_left_wand_temp),
                leftTemp.value,
                modifier = Modifier.padding(start = 30.dp, top = 20.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_right_wand_temp),
                rightTemp.value,
                modifier = Modifier.padding(start = 30.dp, top = 55.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMachineTestSteamInput() {
    MachineTestSteamInputLayout()
}