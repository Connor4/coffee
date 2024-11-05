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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.ADJUSTMENT_AUTO
import com.inno.coffee.utilities.ADJUSTMENT_MANUAL
import com.inno.coffee.utilities.ADJUSTMENT_NONE
import com.inno.coffee.utilities.CONFIG_OPERATION_MODE_NORMAL
import com.inno.coffee.utilities.CONFIG_OPERATION_MODE_SELF_SERVICE
import com.inno.coffee.utilities.HOT_WATER_SIDE
import com.inno.coffee.utilities.HOW_WATER_MID
import com.inno.coffee.utilities.INDEX_AMERICANO_TEMP_ADJUST
import com.inno.coffee.utilities.INDEX_DIFFERENT_BOILER
import com.inno.coffee.utilities.INDEX_HOT_WATER_OUTPUT
import com.inno.coffee.utilities.INDEX_LOW_POWER
import com.inno.coffee.utilities.INDEX_OPERATION_MODE
import com.inno.coffee.utilities.INDEX_SMART_MODE
import com.inno.coffee.utilities.INDEX_STEAM_WAND_POSITION
import com.inno.coffee.utilities.INDEX_TEMPERATURE_UNIT
import com.inno.coffee.utilities.INDEX_WATER_TANK_SURVEILLANCE
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.SMART_MODE_BACK
import com.inno.coffee.utilities.SMART_MODE_FRONT
import com.inno.coffee.utilities.SMART_MODE_OFF
import com.inno.coffee.utilities.SMART_MODE_ON
import com.inno.coffee.utilities.STEAM_WAND_BOTH
import com.inno.coffee.utilities.STEAM_WAND_LEFT
import com.inno.coffee.utilities.STEAM_WAND_RIGHT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.config.MachineConfigViewModel

@Composable
fun MachineConfigLayout(
    viewModel: MachineConfigViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val lowPower = viewModel.lowPower.collectAsState()
    val machineType = viewModel.machineType.collectAsState()
    val temperatureUnit = viewModel.temperatureUnit.collectAsState()
    val operationMode = viewModel.operationMode.collectAsState()
    val smartMode = viewModel.smartMode.collectAsState()
    val waterTankSurveillance = viewModel.waterTankSurveillance.collectAsState()
    val differentBoiler = viewModel.differentBoiler.collectAsState()
    val americanoTempAdjust = viewModel.americanoTempAdjust.collectAsState()
    val hotWaterOutput = viewModel.hotWaterOutput.collectAsState()
    val steamWandPosition = viewModel.steamWandPosition.collectAsState()
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val celsius = stringResource(R.string.config_temp_unit_celsius)
    val fahrenheit = stringResource(R.string.config_temp_unit_fahrenheit)
    val operationNormal = stringResource(R.string.config_operation_mode_normal)
    val operationSelfService = stringResource(R.string.config_operation_mode_self_service)
    val smartModeFront = stringResource(R.string.config_smart_mode_front_hopper)
    val smartModeBack = stringResource(R.string.config_smart_mode_back_hopper)
    val adjustManual = stringResource(R.string.config_adjustment_manual)
    val adjustAuto = stringResource(R.string.config_adjustment_auto)
    val waterMid = stringResource(R.string.config_hot_water_output_mid)
    val waterCoffee = stringResource(R.string.config_hot_water_output_coffee)
    val steamWandBoth = stringResource(R.string.config_steam_wand_both)
    val steamWandLeft = stringResource(R.string.config_steam_wand_left)
    val steamWandRight = stringResource(R.string.config_steam_wand_right)

    val lowPowerValue = if (lowPower.value) on else off
    val unitValue = if (temperatureUnit.value) fahrenheit else celsius
    val operationModeValue = when (operationMode.value) {
        CONFIG_OPERATION_MODE_NORMAL -> operationNormal
        CONFIG_OPERATION_MODE_SELF_SERVICE -> operationSelfService
        else -> ""
    }
    val smartModeValue = when (smartMode.value) {
        SMART_MODE_OFF -> off
        SMART_MODE_ON -> on
        SMART_MODE_FRONT -> smartModeFront
        SMART_MODE_BACK -> smartModeBack
        else -> ""
    }
    val waterTankValue = if (waterTankSurveillance.value) on else off
    val differentBoilerValue = if (differentBoiler.value) on else off
    val americanoTempAdjustValue = when (americanoTempAdjust.value) {
        ADJUSTMENT_NONE -> off
        ADJUSTMENT_MANUAL -> adjustManual
        ADJUSTMENT_AUTO -> adjustAuto
        else -> ""
    }
    val hotWaterOutputValue = when (hotWaterOutput.value) {
        HOW_WATER_MID -> waterMid
        HOT_WATER_SIDE -> waterCoffee
        else -> ""
    }
    val steamWandPositionValue = when (steamWandPosition.value) {
        STEAM_WAND_BOTH -> steamWandBoth
        STEAM_WAND_LEFT -> steamWandLeft
        STEAM_WAND_RIGHT -> steamWandRight
        else -> ""
    }

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
            DisplayItemLayout(stringResource(R.string.config_machine_type),
                "CM01", Color(0xFF2A2B2D)) {

            }
            DisplayItemLayout(stringResource(R.string.config_low_power),
                lowPowerValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = INDEX_LOW_POWER
                defaultValue.value = lowPowerValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(on, true), Pair(off, false)))
            }
            DisplayItemLayout(stringResource(R.string.config_temperature_unit),
                unitValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = INDEX_TEMPERATURE_UNIT
                defaultValue.value = unitValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(celsius, false), Pair(fahrenheit, true)))
            }
            DisplayItemLayout(stringResource(R.string.config_operation_mode),
                operationModeValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = INDEX_OPERATION_MODE
                defaultValue.value = operationModeValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(operationNormal, CONFIG_OPERATION_MODE_NORMAL),
                    Pair(operationSelfService, CONFIG_OPERATION_MODE_SELF_SERVICE)))
            }
            DisplayItemLayout(stringResource(R.string.config_smart_mode),
                smartModeValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = INDEX_SMART_MODE
                defaultValue.value = smartModeValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(off, SMART_MODE_OFF), Pair(on, SMART_MODE_ON),
                    Pair(smartModeFront, SMART_MODE_FRONT), Pair(smartModeBack, SMART_MODE_BACK)))
            }
            DisplayItemLayout(stringResource(R.string.config_water_tank_surveillance),
                waterTankValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = INDEX_WATER_TANK_SURVEILLANCE
                defaultValue.value = waterTankValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(on, true), Pair(off, false)))
            }
            DisplayItemLayout(stringResource(R.string.config_different_boiler),
                differentBoilerValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = INDEX_DIFFERENT_BOILER
                defaultValue.value = differentBoilerValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(on, true), Pair(off, false)))
            }
            DisplayItemLayout(stringResource(R.string.config_americano_temp_adjust),
                americanoTempAdjustValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = INDEX_AMERICANO_TEMP_ADJUST
                defaultValue.value = americanoTempAdjustValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(off, ADJUSTMENT_NONE),
                    Pair(adjustManual, ADJUSTMENT_MANUAL), Pair(adjustAuto, ADJUSTMENT_AUTO)))
            }
            DisplayItemLayout(stringResource(R.string.config_hot_water_output),
                hotWaterOutputValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = INDEX_HOT_WATER_OUTPUT
                defaultValue.value = hotWaterOutputValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(waterMid, HOW_WATER_MID),
                    Pair(waterCoffee, HOT_WATER_SIDE)))
            }
            DisplayItemLayout(stringResource(R.string.config_steam_wand_position),
                steamWandPositionValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = INDEX_STEAM_WAND_POSITION
                defaultValue.value = steamWandPositionValue
                dataMap.clear()
                dataMap.putAll(mapOf(Pair(steamWandBoth, STEAM_WAND_BOTH), Pair(steamWandLeft,
                    STEAM_WAND_LEFT), Pair(steamWandRight, STEAM_WAND_RIGHT)))
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            ListSelectLayout(defaultValue.value, dataMap.toMap(), { _, value ->
                viewModel.saveMachineConfigValue(itemSelectIndex.value, value)
                itemSelectIndex.value = INVALID_INT
            }, {
                itemSelectIndex.value = INVALID_INT
            })
        }
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewConfigMain() {
    MachineConfigLayout()
}