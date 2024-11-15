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
import com.inno.coffee.viewmodel.settings.info.SteamStatusViewModel

@Composable
fun SteamStatusLayout(
    viewModel: SteamStatusViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val processSteamLeft = viewModel.processSteamLeft.collectAsState()
    val processHotWater = viewModel.processHotWater.collectAsState()
    val processSteamRight = viewModel.processSteamRight.collectAsState()
    val actionSteamLeft = viewModel.actionSteamLeft.collectAsState()
    val actionHotWater = viewModel.actionHotWater.collectAsState()
    val actionSteamRight = viewModel.actionSteamRight.collectAsState()
    val prodidSteamLeft = viewModel.prodidSteamLeft.collectAsState()
    val prodidHotWater = viewModel.prodidHotWater.collectAsState()
    val prodidSteamRight = viewModel.prodidSteamRight.collectAsState()
    val modidSteamLeft = viewModel.modidSteamLeft.collectAsState()
    val modidHotWater = viewModel.modidHotWater.collectAsState()
    val modidSteamRight = viewModel.modidSteamRight.collectAsState()
    val numError = viewModel.numError.collectAsState()
    val numStop = viewModel.numStop.collectAsState()
    val numWarning = viewModel.numWarning.collectAsState()

    val valveWaterInBoiler = viewModel.valveWaterInBoiler.collectAsState()
    val valveHotWater = viewModel.valveHotWater.collectAsState()
    val valveMixHotWater = viewModel.valveMixHotWater.collectAsState()
    val valveSteam1 = viewModel.valveSteam1.collectAsState()
    val valveSteam2 = viewModel.valveSteam2.collectAsState()
    val valveFoam1 = viewModel.valveFoam1.collectAsState()
    val valveFoam2 = viewModel.valveFoam2.collectAsState()
    val valvePurgeMix = viewModel.valvePurgeMix.collectAsState()
    val valvePurge = viewModel.valvePurge.collectAsState()
    val valveWaterInlet = viewModel.valveWaterInlet.collectAsState()
    val waterPump = viewModel.waterPump.collectAsState()
    val steamHeating1 = viewModel.steamHeating1.collectAsState()
    val steamHeating2 = viewModel.steamHeating2.collectAsState()
    val steamAirPump = viewModel.steamAirPump.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.info_steam_status),
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

        SteamStatus1(processSteamLeft.value, processHotWater.value, processSteamRight.value,
            actionSteamLeft.value, actionHotWater.value, actionSteamRight.value,
            prodidSteamLeft.value, prodidHotWater.value, prodidSteamRight.value,
            modidSteamLeft.value, modidHotWater.value, modidSteamRight.value,
            numError.value, numStop.value, numWarning.value)
        SteamStatus2(valveWaterInBoiler.value, valveHotWater.value, valveMixHotWater.value,
            valveSteam1.value, valveSteam2.value, valveFoam1.value, valveFoam2.value,
            valvePurgeMix.value, valvePurge.value, valveWaterInlet.value, waterPump.value,
            steamHeating1.value, steamHeating2.value, steamAirPump.value)
    }
}

@Composable
private fun SteamStatus2(
    value1: Boolean = false, value2: Boolean = false, value3: Boolean = false,
    value4: Boolean = false,
    value5: Boolean = false, value6: Boolean = false, value7: Boolean = false,
    value8: Boolean = false,
    value9: Boolean = false, value10: Boolean = false, value11: Boolean = false,
    value12: Boolean = false,
    value13: Boolean = false, value14: Boolean = false,
) {
    Row(
        modifier = Modifier
            .padding(top = 414.dp, start = 55.dp)
            .width(1153.dp)
            .height(255.dp)
            .background(Color(0xFF191A1D)),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 30.dp)
                .width(540.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            InfoOnOffText(stringResource(R.string.info_status_valve_water_in_boiler), value1)
            InfoOnOffText(stringResource(R.string.info_status_valve_hot_water), value2)
            InfoOnOffText(stringResource(R.string.info_status_valve_mix_hot_water), value3)
            InfoOnOffText(stringResource(R.string.info_status_valve_steam_1), value4)
            InfoOnOffText(stringResource(R.string.info_status_valve_steam_2), value5)
            InfoOnOffText(stringResource(R.string.info_status_valve_foam_1), value6)
            InfoOnOffText(stringResource(R.string.info_status_valve_foam_2), value7)
        }
        Column(
            modifier = Modifier
                .width(540.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            InfoOnOffText(stringResource(R.string.info_status_valve_purge_mix), value8)
            InfoOnOffText(stringResource(R.string.info_status_valve_purge), value9)
            InfoOnOffText(stringResource(R.string.info_status_valve_water_inlet), value10)
            InfoOnOffText(stringResource(R.string.info_status_water_pump), value11)
            InfoOnOffText(stringResource(R.string.info_status_steam_heating_1), value12)
            InfoOnOffText(stringResource(R.string.info_status_steam_heating_2), value13)
            InfoOnOffText(stringResource(R.string.info_status_steam_air_pump), value14)
        }
    }
}

@Composable
private fun SteamStatus1(
    value1: Int = 0, value2: Int = 0, value3: Int = 0, value4: Int = 0, value5: Int = 0,
    value6: Int = 0, value7: Int = 0, value8: Int = 0, value9: Int = 0, value10: Int = 0,
    value11: Int = 0, value12: Int = 0, value13: Int = 0, value14: Int = 0, value15: Int = 0,
) {
    Row(
        modifier = Modifier
            .padding(top = 180.dp, start = 55.dp)
            .width(1153.dp)
            .height(214.dp)
            .background(Color(0xFF191A1D)),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 30.dp)
                .width(318.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextWithValue(stringResource(R.string.info_status_process_steam_left),
                value = "$value1")
            TextWithValue(stringResource(R.string.info_status_action_steam_left),
                value = "$value4")
            TextWithValue(stringResource(R.string.info_status_prodid_steam_left),
                value = "$value7")
            TextWithValue(stringResource(R.string.info_status_modid_steam_left),
                value = "$value10")
            TextWithValue(stringResource(R.string.info_status_num_error),
                value = "$value13")
        }
        Column(
            modifier = Modifier
                .width(318.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextWithValue(stringResource(R.string.info_status_process_hot_water),
                value = "$value2")
            TextWithValue(stringResource(R.string.info_status_action_hot_water),
                value = "$value5")
            TextWithValue(stringResource(R.string.info_status_prodid_hot_water),
                value = "$value8")
            TextWithValue(stringResource(R.string.info_status_modid_steam_hot_water),
                value = "$value11")
            TextWithValue(stringResource(R.string.info_status_num_stop),
                value = "$value14")
        }
        Column(
            modifier = Modifier
                .width(318.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextWithValue(stringResource(R.string.info_status_process_steam_right),
                value = "$value3")
            TextWithValue(stringResource(R.string.info_status_action_steam_right),
                value = "$value6")
            TextWithValue(stringResource(R.string.info_status_prodid_steam_right),
                value = "$value9")
            TextWithValue(stringResource(R.string.info_status_modid_steam_right),
                value = "$value12")
            TextWithValue(stringResource(R.string.info_status_num_warning),
                value = "$value15")
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewSteamStatusLayout() {
    SteamStatusLayout()
}