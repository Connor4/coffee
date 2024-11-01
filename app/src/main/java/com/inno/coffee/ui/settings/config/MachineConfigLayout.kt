package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.nsp

@Composable
fun MachineConfigLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.common_machine_config),
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 95.dp)
        ) {
            DisplayItemLayout(stringResource(R.string.config_power_configuration),
                "2/PE, 208V-, 60Hz |30A (USA)", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_low_power),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_machine_type),
                "CM01", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_temperature_unit),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_operation_mode),
                "", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_smart_mode),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_water_tank_surveillance),
                "", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_different_boiler),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_americano_temp_adjust),
                "", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_hot_water_output),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_steam_wand_position),
                "", Color(0xFF191A1D)) {

            }
        }
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewConfigMain() {
    MachineConfigLayout()
}