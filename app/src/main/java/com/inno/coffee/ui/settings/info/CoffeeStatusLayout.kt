package com.inno.coffee.ui.settings.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import com.inno.coffee.viewmodel.settings.info.MachineInfoViewModel

@Composable
fun CoffeeStatusLayout(
    viewModel: MachineInfoViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val processCoffeeLeft = viewModel.processCoffeeLeft.collectAsState()
    val actionCoffeeLeft = viewModel.actionCoffeeLeft.collectAsState()
    val processCoffeeRight = viewModel.processCoffeeRight.collectAsState()
    val actionCoffeeRight = viewModel.actionCoffeeRight.collectAsState()
    val prodIdLeft = viewModel.prodIdLeft.collectAsState()
    val prodIdRight = viewModel.prodIdRight.collectAsState()
    val numWarning = viewModel.numWarning.collectAsState()
    val numStop = viewModel.numStop.collectAsState()
    val numError = viewModel.numError.collectAsState()

    val systemFlowLeft = viewModel.systemFlowLeft.collectAsState()
    val nozzleFlowLeft = viewModel.nozzleFlowLeft.collectAsState()
    val systemFlowRight = viewModel.systemFlowRight.collectAsState()
    val nozzleFlowRight = viewModel.nozzleFlowRight.collectAsState()

    val valveBrewLeft = viewModel.valveBrewLeft.collectAsState()
    val valveBrewRight = viewModel.valveBrewRight.collectAsState()
    val valveBypassLeft = viewModel.valveBypassLeft.collectAsState()
    val valveBypassRight = viewModel.valveBypassRight.collectAsState()
    val fanFront = viewModel.fanFront.collectAsState()
    val valveOutLeft = viewModel.valveOutLeft.collectAsState()
    val valveOutRight = viewModel.valveOutRight.collectAsState()
    val valveWaterInlet = viewModel.valveWaterInlet.collectAsState()
    val valveCleanTab = viewModel.valveCleanTab.collectAsState()
    val grinderLeft = viewModel.grinderLeft.collectAsState()
    val grinderRight = viewModel.grinderRight.collectAsState()
    val boilerLeft = viewModel.boilerLeft.collectAsState()
    val boilerRight = viewModel.boilerRight.collectAsState()
    val waterPump = viewModel.waterPump.collectAsState()
    val relayStandby = viewModel.relayStandby.collectAsState()
    val fanLeft = viewModel.fanLeft.collectAsState()
    val fanRight = viewModel.fanRight.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.info_coffee_status),
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

        CoffeeStatus1(processCoffeeLeft.value, processCoffeeRight.value, numWarning.value,
            actionCoffeeLeft.value, actionCoffeeRight.value, numStop.value,
            prodIdLeft.value, prodIdRight.value, numError.value)
        CoffeeStatus2(systemFlowLeft.value, systemFlowRight.value, nozzleFlowLeft.value,
            nozzleFlowRight.value)
        CoffeeStatus3(valveBrewLeft.value, valveBrewRight.value, valveBypassLeft.value,
            valveBypassRight.value, fanFront.value, valveOutLeft.value, valveOutRight.value,
            valveWaterInlet.value, valveCleanTab.value, grinderLeft.value, grinderRight.value,
            boilerLeft.value, boilerRight.value, waterPump.value, relayStandby.value,
            fanLeft.value, fanRight.value)
    }
}

@Composable
private fun CoffeeStatus3(
    value1: Boolean = false,
    value2: Boolean = false,
    value3: Boolean = false,
    value4: Boolean = false,
    value5: Boolean = false,
    value6: Boolean = false,
    value7: Boolean = false,
    value8: Boolean = false,
    value9: Boolean = false,
    value10: Boolean = false,
    value11: Boolean = false,
    value12: Boolean = false,
    value13: Boolean = false,
    value14: Boolean = false,
    value15: Boolean = false,
    value16: Boolean = false,
    value17: Boolean = false,
) {
    Box(
        modifier = Modifier
            .padding(top = 434.dp, start = 55.dp)
            .width(1153.dp)
            .height(328.dp)
            .background(Color(0xFF191A1D))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp)
                .wrapContentSize(),
        ) {
            Column(
                modifier = Modifier
                    .width(540.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                InfoOnOffText(stringResource(R.string.info_status_valve_brew_left), value1)
                InfoOnOffText(stringResource(R.string.info_status_value_brew_right), value2)
                InfoOnOffText(stringResource(R.string.info_status_valve_bypass_left), value3)
                InfoOnOffText(stringResource(R.string.info_status_valve_bypass_right), value4)
                InfoOnOffText(stringResource(R.string.info_status_fan_front), value5)
                InfoOnOffText(stringResource(R.string.info_status_valve_out_left), value6)
                InfoOnOffText(stringResource(R.string.info_status_valve_out_right), value7)
                InfoOnOffText(stringResource(R.string.info_status_valve_water_inlet), value8)
                InfoOnOffText(stringResource(R.string.info_status_valve_clean_tab), value9)
            }
            Column(
                modifier = Modifier
                    .width(540.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                InfoOnOffText(stringResource(R.string.info_status_grinder_left), value10)
                InfoOnOffText(stringResource(R.string.info_status_grinder_right), value11)
                InfoOnOffText(stringResource(R.string.info_status_boiler_left), value12)
                InfoOnOffText(stringResource(R.string.info_status_boiler_right), value13)
                InfoOnOffText(stringResource(R.string.info_status_water_pump), value14)
                InfoOnOffText(stringResource(R.string.info_status_relay_standby), value15)
                InfoOnOffText(stringResource(R.string.info_status_fan_left), value16)
                InfoOnOffText(stringResource(R.string.info_status_fan_right), value17)
            }
        }
    }
}

@Composable
private fun CoffeeStatus2(
    value1: Int = 0,
    value2: Int = 0,
    value3: Int = 0,
    value4: Int = 0,
) {
    val v1 = value1 / 10f
    val v2 = value2 / 10f
    val v3 = value3 / 10f
    val v4 = value4 / 10f
    Box(
        modifier = Modifier
            .padding(top = 320.dp, start = 55.dp)
            .width(1153.dp)
            .height(94.dp)
            .background(Color(0xFF191A1D))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp)
                .wrapContentSize(),
        ) {
            Column(
                modifier = Modifier
                    .width(538.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextWithValue(stringResource(R.string.info_status_system_flow_left),
                    value = "$v1", unit = "ticks/s")
                TextWithValue(stringResource(R.string.info_status_nozzle_flow_left),
                    value = "$v3", unit = "ticks/s")
            }
            Column(
                modifier = Modifier
                    .width(538.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextWithValue(stringResource(R.string.info_status_system_flow_right),
                    value = "$v2", unit = "ticks/s")
                TextWithValue(stringResource(R.string.info_status_nozzle_flow_right),
                    value = "$v4", unit = "ticks/s")
            }
        }
    }
}

@Composable
private fun CoffeeStatus1(
    value1: Int = 0,
    value2: Int = 0,
    value3: Int = 0,
    value4: Int = 0,
    value5: Int = 0,
    value6: Int = 0,
    value7: Int = 0,
    value8: Int = 0,
    value9: Int = 0,
) {
    Box(
        modifier = Modifier
            .padding(top = 180.dp, start = 55.dp)
            .width(1153.dp)
            .height(120.dp)
            .background(Color(0xFF191A1D))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp)
                .wrapContentSize(),
        ) {
            Column(
                modifier = Modifier
                    .width(318.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextWithValue(stringResource(R.string.info_status_process_coffee_left),
                    value = "$value1")
                TextWithValue(stringResource(R.string.info_status_action_coffee_left),
                    value = "$value4")
                TextWithValue(stringResource(R.string.info_status_prodid_left),
                    value = "$value7")
            }
            Column(
                modifier = Modifier
                    .width(318.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextWithValue(stringResource(R.string.info_status_process_coffee_right),
                    value = "$value2")
                TextWithValue(stringResource(R.string.info_status_action_coffee_right),
                    value = "$value5")
                TextWithValue(stringResource(R.string.info_status_prodid_right),
                    value = "$value8")
            }
            Column(
                modifier = Modifier
                    .width(318.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextWithValue(stringResource(R.string.info_status_num_warning),
                    value = "$value3")
                TextWithValue(stringResource(R.string.info_status_num_stop),
                    value = "$value6")
                TextWithValue(stringResource(R.string.info_status_num_error),
                    value = "$value9")
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewInfoStatus() {
    CoffeeStatusLayout()
}