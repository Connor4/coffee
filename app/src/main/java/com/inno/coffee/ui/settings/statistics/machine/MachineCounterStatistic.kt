package com.inno.coffee.ui.settings.statistics.machine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inno.coffee.R

@Composable
fun MachineCounterStatistic(
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val itemWidth = screenWidthDp / 4

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.statistic_main_machine_counter),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            TotalCounter(width = itemWidth)
            Spacer(Modifier.width(40.dp))
            CleanCounter(width = itemWidth)
            Spacer(Modifier.width(40.dp))
            OtherCounter(width = itemWidth)
        }
    }
}

@Composable
private fun OtherCounter(width: Dp) {
    Column(
        modifier = Modifier
            .width(width)
            .wrapContentHeight()
    ) {
        InfoRow(stringResource(id = R.string.statistic_heating_time_boiler_left), "7.30h")
        InfoRow(stringResource(id = R.string.statistic_heating_time_boiler_right), "6.52h")
        InfoRow(stringResource(id = R.string.statistic_heat_time_steam_boiler_1), "9.52h")
        InfoRow(stringResource(id = R.string.statistic_heat_time_steam_boiler_2), "8.25h")
        InfoRow(stringResource(id = R.string.statistic_time_air_pump), "0.10h")
        InfoRow(stringResource(id = R.string.statistic_time_milk_steam_left), "0.00h")
        InfoRow(stringResource(id = R.string.statistic_time_milk_steam_right), "0.00h")
    }
}

@Composable
private fun CleanCounter(width: Dp) {
    Column(
        modifier = Modifier
            .width(width)
            .wrapContentHeight()
    ) {
        InfoRow(stringResource(id = R.string.statistic_cleaning_counter), "60")
        InfoRow(stringResource(id = R.string.statistic_time_hot_water), "0.14h")
        InfoRow(stringResource(id = R.string.statistic_time_steam_left), "0.12h")
        InfoRow(stringResource(id = R.string.statistic_time_steam_right), "0.05h")
        InfoRow(stringResource(id = R.string.statistic_time_steam_boiler_purge), "0.00h")
        InfoRow(stringResource(id = R.string.statistic_brew_cycles_left), "420")
        InfoRow(stringResource(id = R.string.statistic_brew_cycles_right), "169")
        InfoRow(stringResource(id = R.string.statistic_cycles_rinse_left), "160")
        InfoRow(stringResource(id = R.string.statistic_cycles_rinse_right), "136")
        InfoRow(stringResource(id = R.string.statistic_milk_cycles_left), "0")
        InfoRow(stringResource(id = R.string.statistic_milk_cycles_right), "0")
        InfoRow(stringResource(id = R.string.statistic_time_milk), "0.00h")
    }
}

@Composable
private fun TotalCounter(width: Dp) {
    Column(
        modifier = Modifier
            .width(width)
            .wrapContentHeight()
    ) {
        InfoRow(stringResource(id = R.string.statistic_absolute_counter), "667")
        InfoRow(stringResource(id = R.string.statistic_total_water_quantity), "230.6 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_water_products), "71.0 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_water_rinse), "75.4 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_water_cleaning), "68.1 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_coffee_bean_quantity), "230.6 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_coffee_beans_rear), "71.0 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_coffee_beans_front), "75.4 [L]")
        InfoRow(stringResource(id = R.string.statistic_total_coffee_powder_chute), "68.1 [L]")
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
        )
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewCounter() {
    MachineCounterStatistic()
}