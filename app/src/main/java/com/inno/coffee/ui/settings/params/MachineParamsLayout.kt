package com.inno.coffee.ui.settings.params

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.PARAMS_KEY_BOILER_TEMP
import com.inno.coffee.utilities.PARAMS_KEY_NTC_LEFT
import com.inno.coffee.utilities.PARAMS_KEY_NTC_RIGHT
import com.inno.coffee.utilities.PARAMS_KEY_STEAM_BOILER_PRESSURE
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.params.MachineParamsViewModel
import com.inno.common.db.entity.FormulaItem

@Composable
fun MachineParamsLayout(
    viewModel: MachineParamsViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }
    val scrollDefaultValue = remember { mutableStateOf<FormulaItem.FormulaUnitValue?>(null) }

    val boilerTemp = viewModel.boilerTemp.collectAsState()
    val coldRinseQuantity = viewModel.coldRinseQuantity.collectAsState()
    val warmRinseQuantity = viewModel.warmRinseQuantity.collectAsState()
    val groundsDrawerQuantity = viewModel.groundsDrawerQuantity.collectAsState()
    val brewGroupLoadBalancing = viewModel.brewGroupLoadBalancing.collectAsState()
    val brewGroupPreHeating = viewModel.brewGroupPreHeating.collectAsState()
    val grinderPurgeFunction = viewModel.grinderPurgeFunction.collectAsState()
    val numberOfCyclesRinse = viewModel.numberOfCyclesRinse.collectAsState()
    val steamBoilerPressure = viewModel.steamBoilerPressure.collectAsState()
    val ntcCorrectionSteamLeft = viewModel.ntcCorrectionSteamLeft.collectAsState()
    val ntcCorrectionSteamRight = viewModel.ntcCorrectionSteamRight.collectAsState()

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.common_machine_params),
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
            DisplayItemLayout(stringResource(R.string.params_coffee_boiler_temp),
                "${boilerTemp.value}", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_BOILER_TEMP
                scrollDefaultValue.value = FormulaItem.FormulaUnitValue(
                    value = boilerTemp.value.toShort(), rangeStart = 80F, rangeEnd = 100F,
                    unit = "[°C]"
                )
            }
            DisplayItemLayout(stringResource(R.string.params_cold_rinse_quantity),
                "${coldRinseQuantity.value}", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_warm_rinse_quantity),
                "${warmRinseQuantity.value}", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_grounds_drawer_quantity),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_brew_group_load_balancing),
                "", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_brew_group_pre_heating),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_grinder_purge_function),
                "", Color(0xFF191A1D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_number_of_cycles_rinse),
                "", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.params_steam_boiler_pressure),
                "${steamBoilerPressure.value}", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_STEAM_BOILER_PRESSURE
                scrollDefaultValue.value = FormulaItem.FormulaUnitValue(
                    value = steamBoilerPressure.value.toShort(), rangeStart = -10F, rangeEnd = 10F,
                    unit = "[bar]"
                )
            }
            DisplayItemLayout(stringResource(R.string.params_ntc_correction_steam_left),
                "${ntcCorrectionSteamLeft.value}", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = PARAMS_KEY_NTC_LEFT
                scrollDefaultValue.value = FormulaItem.FormulaUnitValue(
                    value = ntcCorrectionSteamLeft.value.toShort(), rangeStart = -10F,
                    rangeEnd = 10F,
                    unit = "[°C]"
                )
            }
            DisplayItemLayout(stringResource(R.string.params_ntc_correction_steam_right),
                "${ntcCorrectionSteamRight.value}", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_NTC_RIGHT
                scrollDefaultValue.value = FormulaItem.FormulaUnitValue(
                    value = ntcCorrectionSteamRight.value.toShort(), rangeStart = -10F,
                    rangeEnd = 10F,
                    unit = "[°C]"
                )
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            if (itemSelectIndex.value == PARAMS_KEY_BOILER_TEMP ||
                    itemSelectIndex.value == PARAMS_KEY_STEAM_BOILER_PRESSURE ||
                    itemSelectIndex.value == PARAMS_KEY_NTC_LEFT ||
                    itemSelectIndex.value == PARAMS_KEY_NTC_RIGHT) {
                key(scrollDefaultValue.value) {
                    UnitValueScrollBar(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 172.dp, end = 90.dp)
                            .width(550.dp)
                            .wrapContentHeight(),
                        unitValue = scrollDefaultValue.value!!) { changeValue ->
//                        viewModel.saveDisplayGroupTwoValue(itemSelectIndex.value, changeValue.value
//                            .toInt())
                    }
                }
            } else {
                ListSelectLayout(defaultValue.value, dataMap.toMap(), { _, value ->
//                    viewModel.saveDisplayGroupTwoValue(itemSelectIndex.value, value)
                    itemSelectIndex.value = INVALID_INT
                }, {
                    itemSelectIndex.value = INVALID_INT
                })
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineParams() {
    MachineParamsLayout()
}