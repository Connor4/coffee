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
import com.inno.coffee.utilities.MACHINE_TEST_AIR_PUMP
import com.inno.coffee.utilities.MACHINE_TEST_FOAM_VALVE_1
import com.inno.coffee.utilities.MACHINE_TEST_FOAM_VALVE_2
import com.inno.coffee.utilities.MACHINE_TEST_HOT_WATER_MAX_VALVE
import com.inno.coffee.utilities.MACHINE_TEST_HOT_WATER_VALVE
import com.inno.coffee.utilities.MACHINE_TEST_PURGE_MAX_VALVE
import com.inno.coffee.utilities.MACHINE_TEST_PURGE_VALVE
import com.inno.coffee.utilities.MACHINE_TEST_STEAM_BOILER_HEATER1
import com.inno.coffee.utilities.MACHINE_TEST_STEAM_BOILER_HEATER2
import com.inno.coffee.utilities.MACHINE_TEST_STEAM_VALVE_1
import com.inno.coffee.utilities.MACHINE_TEST_STEAM_VALVE_2
import com.inno.coffee.utilities.MACHINE_TEST_WATER_FILL_VALVE
import com.inno.coffee.utilities.MACHINE_TEST_WATER_INLET_VALVE
import com.inno.coffee.utilities.MACHINE_TEST_WATER_PUMP_ID
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestOutputViewModel

@Composable
fun MachineTestSteamOutputLayout(
    viewModel: MachineTestOutputViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.steamTestTurnOff()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.steamTestTurnOff()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_output_test_steam),
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
            MachineTestStatusButton(R.string.machine_test_so_water_fill_valve) {
                viewModel.sendTestCommand(MACHINE_TEST_WATER_FILL_VALVE, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_air_pump) {
                viewModel.sendTestCommand(MACHINE_TEST_AIR_PUMP, if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 316.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_so_purge_valve) {
                viewModel.sendTestCommand(MACHINE_TEST_PURGE_VALVE, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_purge_mix_valve) {
                viewModel.sendTestCommand(MACHINE_TEST_PURGE_MAX_VALVE, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_hot_water_valve) {
                viewModel.sendTestCommand(MACHINE_TEST_HOT_WATER_VALVE, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_hot_water_mix_valve) {
                viewModel.sendTestCommand(MACHINE_TEST_HOT_WATER_MAX_VALVE, if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 411.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_so_steam_boiler_heater1) {
                viewModel.sendTestCommand(MACHINE_TEST_STEAM_BOILER_HEATER1, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_steam_boiler_heater2) {
                viewModel.sendTestCommand(MACHINE_TEST_STEAM_BOILER_HEATER2, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_steam_valve1) {
                viewModel.sendTestCommand(MACHINE_TEST_STEAM_VALVE_1, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_steam_valve2) {
                viewModel.sendTestCommand(MACHINE_TEST_STEAM_VALVE_2, if (it) 1 else 0)
            }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 506.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestStatusButton(R.string.machine_test_so_efoam_valve1) {
                viewModel.sendTestCommand(MACHINE_TEST_FOAM_VALVE_1, if (it) 1 else 0)
            }
            MachineTestStatusButton(R.string.machine_test_so_efoam_valve2) {
                viewModel.sendTestCommand(MACHINE_TEST_FOAM_VALVE_2, if (it) 1 else 0)
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineTestSteamOutput() {
    MachineTestSteamOutputLayout()
}