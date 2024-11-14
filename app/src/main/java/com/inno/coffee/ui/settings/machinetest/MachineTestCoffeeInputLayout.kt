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
import androidx.compose.runtime.LaunchedEffect
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
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestInputViewModel


@Composable
fun MachineTestCoffeeInputLayout(
    viewModel: MachineTestInputViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val okColor = Color(0xFF6DD400)
    val failColor = Color.Red
    val present = stringResource(R.string.machine_test_present)
    val missing = stringResource(R.string.machine_test_missing)
    val ok = stringResource(R.string.machine_test_ok)
    val warning = stringResource(R.string.machine_test_warning)

    val microSwitchLeft = viewModel.microSwitchLeft.collectAsState()
    val microSwitchRight = viewModel.microSwitchRight.collectAsState()
    val rear = viewModel.rear.collectAsState()
    val front = viewModel.front.collectAsState()
    val drawer = viewModel.drawer.collectAsState()
    val switch = viewModel.switch.collectAsState()
    val pressure = viewModel.pressure.collectAsState()
    val leftTemp = viewModel.leftTemp.collectAsState()
    val rightTemp = viewModel.rightTemp.collectAsState()
    val leftFlow = viewModel.leftFlow.collectAsState()
    val rightFlow = viewModel.rightFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCoffeeInputs()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_coffee_inputs),
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
            stringResource(R.string.machine_test_micro_switch_left),
            if (microSwitchLeft.value) ok else warning,
            modifier = Modifier.padding(top = 179.dp, start = 55.dp),
        )
        TextWithValue(
            stringResource(R.string.machine_test_micro_switch_right),
            if (microSwitchRight.value) ok else warning,
            modifier = Modifier.padding(top = 179.dp, start = 579.dp),
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
                    stringResource(R.string.machine_test_bean_hopper_rear),
                    if (rear.value) present else missing,
                    textColor = if (rear.value) okColor else failColor,
                )
                TextWithValue(
                    stringResource(R.string.machine_test_bean_hopper_front),
                    if (front.value) present else missing,
                    textColor = if (front.value) okColor else failColor,
                )
                TextWithValue(
                    stringResource(R.string.machine_test_grounds_drawer),
                    if (drawer.value) present else missing,
                    textColor = if (drawer.value) okColor else failColor,
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
                stringResource(R.string.machine_test_line_pressure_switch),
                value = if (switch.value) ok else warning,
                textColor = if (switch.value) okColor else failColor,
                modifier = Modifier.padding(start = 30.dp, top = 20.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_water_pressure),
                value = "${pressure.value}",
                unit = "bar",
                modifier = Modifier.padding(start = 600.dp, top = 20.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_boiler_temperature_left),
                value = leftTemp.value,
                modifier = Modifier.padding(start = 30.dp, top = 55.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_boiler_temperature_right),
                value = rightTemp.value,
                modifier = Modifier.padding(start = 600.dp, top = 55.dp)
            )

            TextWithValue(
                stringResource(R.string.machine_test_flow_rate_left),
                value = "${leftFlow.value}",
                unit = "ticks/s",
                modifier = Modifier.padding(start = 30.dp, top = 120.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_flow_rate_right),
                value = "${rightFlow.value}",
                unit = "ticks/s",
                modifier = Modifier.padding(start = 600.dp, top = 120.dp)
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineTestInput() {
    MachineTestCoffeeInputLayout()
}