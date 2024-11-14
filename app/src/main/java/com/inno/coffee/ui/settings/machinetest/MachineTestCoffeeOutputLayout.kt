package com.inno.coffee.ui.settings.machinetest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestOutputViewModel
import com.inno.serialport.utilities.MACHINE_TEST_AMERICANO_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_AMERICANO_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_BALL_DISPENSER_BACKWARD
import com.inno.serialport.utilities.MACHINE_TEST_BALL_DISPENSER_FORWARD
import com.inno.serialport.utilities.MACHINE_TEST_BOILER_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_BOILER_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_BREW_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_BREW_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_BYPASS_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_BYPASS_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_FAN_FRONT
import com.inno.serialport.utilities.MACHINE_TEST_FAN_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_FAN_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_GRINDER_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_GRINDER_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_WATER_INLET_VALVE
import com.inno.serialport.utilities.MACHINE_TEST_WATER_PUMP_ID

@Composable
fun MachineTestCoffeeOutLayout(
    viewModel: MachineTestOutputViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {

    LaunchedEffect(Unit) {
        viewModel.coffeeTestTurnOff()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.coffeeTestTurnOff()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_output_test_coffee),
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

        Row(
            modifier = Modifier.padding(start = 54.dp, top = 221.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_co_water_pump) {
                viewModel.sendTestCommand(MACHINE_TEST_WATER_PUMP_ID, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_co_water_inlet_valve) {
                viewModel.sendTestCommand(MACHINE_TEST_WATER_INLET_VALVE, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_co_brew_valve_left) {
                viewModel.sendTestCommand(MACHINE_TEST_BREW_VALVE_LEFT, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_co_brew_valve_right) {
                viewModel.sendTestCommand(MACHINE_TEST_BREW_VALVE_RIGHT, if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 316.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_co_bypass_valve_left) {
                viewModel.sendTestCommand(MACHINE_TEST_BYPASS_VALVE_LEFT, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_co_bypass_valve_right) {
                viewModel.sendTestCommand(MACHINE_TEST_BYPASS_VALVE_RIGHT, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_co_americano_left) {
                viewModel.sendTestCommand(MACHINE_TEST_AMERICANO_LEFT, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_co_americano_right) {
                viewModel.sendTestCommand(MACHINE_TEST_AMERICANO_RIGHT, if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 411.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestButton(R.string.machine_test_co_ball_dispenser_forward) {
                viewModel.sendTestCommand(MACHINE_TEST_BALL_DISPENSER_FORWARD)

            }
            MachineTestButton(R.string.machine_test_co_ball_dispenser_backward) {
                viewModel.sendTestCommand(MACHINE_TEST_BALL_DISPENSER_BACKWARD)
            }
        }

        Row(
            modifier = Modifier.padding(start = 54.dp, top = 506.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestButton(R.string.machine_test_co_coffee_boiler_left) {
                viewModel.sendTestCommand(MACHINE_TEST_BOILER_LEFT)
            }
            MachineTestButton(R.string.machine_test_co_coffee_boiler_right) {
                viewModel.sendTestCommand(MACHINE_TEST_BOILER_RIGHT)
            }
            MachineTestButton(R.string.machine_test_co_grinder_left) {
                viewModel.sendTestCommand(MACHINE_TEST_GRINDER_LEFT)
            }
            MachineTestButton(R.string.machine_test_co_grinder_right) {
                viewModel.sendTestCommand(MACHINE_TEST_GRINDER_RIGHT)
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 601.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestButton(R.string.machine_test_co_fan_front) {
                viewModel.sendTestCommand(MACHINE_TEST_FAN_FRONT)
            }
            MachineTestButton(R.string.machine_test_co_fan_left) {
                viewModel.sendTestCommand(MACHINE_TEST_FAN_LEFT)
            }
            MachineTestButton(R.string.machine_test_co_fan_right) {
                viewModel.sendTestCommand(MACHINE_TEST_FAN_RIGHT)
            }
        }

    }

}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewCoffeeOutput() {
    MachineTestCoffeeOutLayout()
}