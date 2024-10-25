package com.inno.coffee.ui.settings.statistics.machine

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

val groupOne = listOf(
    R.string.statistic_total_water_quantity,
    R.string.statistic_total_water_products,
    R.string.statistic_total_water_rinse,
    R.string.statistic_total_water_cleaning,
    R.string.statistic_total_coffee_bean_quantity,
    R.string.statistic_total_coffee_beans_rear,
    R.string.statistic_total_coffee_beans_front,
    R.string.statistic_total_coffee_powder_chute,
)

@Composable
fun MachineCounterLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.statistic_main_machine_counter),
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
        Text(
            text = stringResource(id = R.string.statistic_absolute_counter),
            fontSize = 5.nsp(),
            color = Color(0xFF32C5FF),
            modifier = Modifier.padding(start = 79.dp, top = 180.dp)
        )
        Text(
            text = "0", color = Color(0xFF32C5FF),
            fontSize = 5.nsp(),
            modifier = Modifier.padding(start = 450.dp, top = 180.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 50.dp, top = 224.dp)
                .width(550.dp)
                .height(512.dp),
        ) {
            CounterItem(R.string.statistic_total_water_quantity, value = 0f, unit = "[L]",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_total_water_products, value = 0f, unit = "[L]")
            CounterItem(R.string.statistic_total_water_rinse, value = 0f, unit = "[L]",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_total_water_cleaning, value = 0f, unit = "[L]")
            CounterItem(R.string.statistic_total_coffee_bean_quantity, value = 0f, unit = "[L]",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_total_coffee_beans_rear, value = 0f, unit = "[L]")
            CounterItem(R.string.statistic_total_coffee_beans_front, value = 0f, unit = "[L]",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_total_coffee_powder_chute, value = 0f, unit = "[L]")

            CounterItem(R.string.statistic_cleaning_counter, value = 0f, unit = "h",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_time_hot_water, value = 0f, unit = "h")
            CounterItem(R.string.statistic_time_steam_left, value = 0f, unit = "h",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_time_steam_right, value = 0f, unit = "h")
            CounterItem(R.string.statistic_time_steam_boiler_purge, value = 0f, unit = "h",
                color = Color(0xFF191A1D))

            CounterItem2(stringResource(R.string.statistic_last_reset, ""),
                value = "30.09.2022, 05:30:41")
            CounterItem2(color = Color(0xFF191A1D))
            CounterItem2()

        }

        Column(
            modifier = Modifier
                .padding(start = 625.dp, top = 224.dp)
                .width(550.dp)
                .height(224.dp)
        ) {
            CounterItem(R.string.statistic_brew_cycles_left, 0f, color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_brew_cycles_right, 0f)
            CounterItem(R.string.statistic_cycles_rinse_left, 0f, color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_cycles_rinse_right, 0f)
            CounterItem(R.string.statistic_milk_cycles_left, 0f, color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_milk_cycles_right, 0f)
            CounterItem(R.string.statistic_time_milk, 0f, unit = "h", color = Color(0xFF191A1D))
        }

        Column(
            modifier = Modifier
                .padding(start = 625.dp, top = 514.dp)
                .width(550.dp)
                .height(224.dp)
        ) {
            CounterItem(R.string.statistic_heating_time_boiler_left, 0f, unit = "h")
            CounterItem(R.string.statistic_heating_time_boiler_right, 0f, unit = "h",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_heat_time_steam_boiler_1, 0f, unit = "h")
            CounterItem(R.string.statistic_heat_time_steam_boiler_2, 0f, unit = "h",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_time_air_pump, 0f, unit = "h")
            CounterItem(R.string.statistic_time_milk_steam_left, 0f, unit = "h",
                color = Color(0xFF191A1D))
            CounterItem(R.string.statistic_time_milk_steam_right, 0f, unit = "h")
        }
    }
}

@Composable
private fun CounterItem(
    @StringRes key: Int,
    value: Float,
    unit: String = "",
    color: Color = Color(0xFF2A2B2D),
) {
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .width(550.dp)
                .height(30.dp)
                .background(color = color),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(key), color = Color.White,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 29.dp)
            )
            Text(
                text = "$value $unit", color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 400.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
private fun CounterItem2(
    key: String = "",
    value: String = "",
    color: Color = Color(0xFF2A2B2D),
) {
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .width(550.dp)
                .height(30.dp)
                .background(color = color),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = key, color = Color.White,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 29.dp)
            )
            Text(
                text = value, color = Color.White,
                fontSize = 5.nsp(),
                modifier = Modifier.padding(start = 280.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineCounterLayout() {
    MachineCounterLayout()
//    CounterItem("Total Water Quantity:", 230.6f, "[L]")
}