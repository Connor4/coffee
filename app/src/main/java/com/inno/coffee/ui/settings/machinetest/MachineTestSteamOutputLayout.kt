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
fun MachineTestSteamOutputLayout(
    onCloseClick: () -> Unit = {},
) {
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
            MachineTestItem(R.string.machine_test_co_water_pump) { }
            MachineTestItem(R.string.machine_test_co_water_inlet_valve) { }
            MachineTestItem(R.string.machine_test_so_water_fill_valve) { }
            MachineTestItem(R.string.machine_test_so_air_pump) { }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 316.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestItem(R.string.machine_test_so_purge_valve) { }
            MachineTestItem(R.string.machine_test_so_purge_mix_valve) { }
            MachineTestItem(R.string.machine_test_so_hot_water_valve) { }
            MachineTestItem(R.string.machine_test_so_hot_water_mix_valve) { }
        }
        Row(
            modifier = Modifier.padding(start = 54.dp, top = 411.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MachineTestItem(R.string.machine_test_so_steam_boiler_heater1) { }
            MachineTestItem(R.string.machine_test_so_steam_boiler_heater2) { }
            MachineTestItem(R.string.machine_test_so_steam_valve1) { }
            MachineTestItem(R.string.machine_test_so_steam_valve2) { }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineTestSteamOutput() {
    MachineTestSteamOutputLayout()
}