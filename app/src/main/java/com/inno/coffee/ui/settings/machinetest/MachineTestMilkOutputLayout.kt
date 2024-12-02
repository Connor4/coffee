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
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_AIR_PUMP
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_AIR_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_AIR_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_DRAIN_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_DRAIN_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_MILK_PUMP_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_MILK_PUMP_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_OUTLET_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_OUTLET_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_RINSE_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_RINSE_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_STEAM_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_STEAM_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_TANK_VALVE_LEFT
import com.inno.serialport.utilities.MACHINE_TEST_MILK_OUTPUT_TANK_VALVE_RIGHT
import com.inno.serialport.utilities.MACHINE_TEST_WATER_INLET_VALVE
import com.inno.serialport.utilities.MACHINE_TEST_WATER_PUMP_ID

@Composable
fun MachineTestMilkOutputLayout(
    viewModel: MachineTestOutputViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.milkTestTurnOff()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.milkTestTurnOff()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_output_test_milk),
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
            MachineTestStatusButton(R.string.machine_test_mo_rinse_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_RINSE_VALVE_LEFT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_rinse_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_RINSE_VALVE_RIGHT,
                    if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 316.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_mo_tank_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_TANK_VALVE_LEFT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_tank_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_TANK_VALVE_RIGHT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_steam_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_STEAM_VALVE_LEFT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_steam_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_STEAM_VALVE_RIGHT,
                    if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 411.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_mo_outlet_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_OUTLET_VALVE_LEFT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_outlet_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_OUTLET_VALVE_RIGHT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_drain_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_DRAIN_VALVE_LEFT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_drain_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_DRAIN_VALVE_RIGHT,
                    if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 506.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_mo_air_valve_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_AIR_VALVE_LEFT, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_air_valve_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_AIR_VALVE_RIGHT,
                    if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_milk_left) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_MILK_PUMP_LEFT, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_mo_milk_right) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_MILK_PUMP_RIGHT,
                    if (it) 1 else 0)
            }
        }

        Row(
            modifier = Modifier.padding(start = 54.dp, top = 601.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_so_air_pump) {
                viewModel.sendTestCommand(MACHINE_TEST_MILK_OUTPUT_AIR_PUMP, if (it) 1 else 0)
            }
        }

    }
}

@Preview
@Composable
private fun PreviewMachineTestMilkOutput() {
    MachineTestMilkOutputLayout() { }
}