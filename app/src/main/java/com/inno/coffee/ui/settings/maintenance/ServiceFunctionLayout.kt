package com.inno.coffee.ui.settings.maintenance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.maintenance.ServiceFunctionViewModel
import com.inno.serialport.utilities.MAINTENANCE_DEPRESSURIZE_STEAM_BOILER_ID
import com.inno.serialport.utilities.MAINTENANCE_EMPTY_COFFEE_BOILER_ID
import com.inno.serialport.utilities.MAINTENANCE_EMPTY_STEAM_BOILER_ID
import com.inno.serialport.utilities.MAINTENANCE_FLOW_RATE_RINSE_ID
import com.inno.serialport.utilities.MAINTENANCE_FRONT_RINSE_ID
import com.inno.serialport.utilities.MAINTENANCE_STEAM_BOILER_OVER_PRESSURE_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_WATER_PUMP_PRESSURE_ID

@Composable
fun ServiceFunctionLayout(
    viewModel: ServiceFunctionViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val ok = stringResource(R.string.machine_test_ok)
    val warning = stringResource(R.string.machine_test_warning)

    val secureLevel = false
    val workLevel = false
    val pressure = 1.8

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.maintenance_service_functions),
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
                .padding(start = 50.dp, top = 226.dp, end = 95.dp)
        ) {
            ServiceFunctionItemLayout(
                key = stringResource(R.string.maintenance_steam_boiler_secure_level),
                value = if (secureLevel) ok else warning,
                textColor = if (secureLevel) Color(0xFF6DD400) else Color.Red,
                backgroundColor = Color(0xFF191A1D))
            ServiceFunctionItemLayout(stringResource(R.string.maintenance_steam_boiler_work_level),
                value = if (workLevel) ok else warning,
                textColor = if (workLevel) Color(0xFF6DD400) else Color.Red,
                backgroundColor = Color(0xFF2A2B2D))
            ServiceFunctionItemLayout(stringResource(R.string.maintenance_steam_boiler_pressure),
                "$pressure bar", backgroundColor = Color(0xFF191A1D))
            ServiceFunctionItemLayout("", "", backgroundColor = Color(0xFF2A2B2D))
            ServiceFunctionItemLayout("", "", backgroundColor = Color(0xFF191A1D))
        }

        Row(
            modifier = Modifier.padding(start = 50.dp, top = 474.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_empty_coffee_boiler)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_EMPTY_COFFEE_BOILER_ID)
            }
            ServiceFunctionStatusButton(R.string.maintenance_water_pump_pressure_check) {
                viewModel.sendTestCommand(MAINTENANCE_WATER_PUMP_PRESSURE_ID, if (it) 1 else 0)
            }
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_reduce_steam_boiler_pressure)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_DEPRESSURIZE_STEAM_BOILER_ID)
            }
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_empty_steam_boiler)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_EMPTY_STEAM_BOILER_ID)
            }
        }
        Row(
            modifier = Modifier.padding(start = 50.dp, top = 564.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_steam_boiler_pressure_test)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_STEAM_BOILER_OVER_PRESSURE_TEST_ID)
            }
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_front_rinse)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_FRONT_RINSE_ID)
            }
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_flow_rate_rinse)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_FLOW_RATE_RINSE_ID)
            }
        }

    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMaintain() {
    ServiceFunctionLayout()
}