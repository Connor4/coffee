package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ACCURACY_2
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MILK_DETECT_METHOD
import com.inno.coffee.utilities.MILK_FOAM_WARNING
import com.inno.coffee.utilities.MILK_OUTLET
import com.inno.coffee.utilities.MILK_SETTING_MODE
import com.inno.coffee.utilities.MILK_STEAM_PRESSURE
import com.inno.coffee.utilities.MILK_TANK_DETECT
import com.inno.coffee.utilities.MILK_TEMP_DETECT
import com.inno.coffee.utilities.MILK_VALVE
import com.inno.coffee.viewmodel.settings.config.MilkSettingViewModel

@Composable
fun MilkSettingPage1(
    viewModel: MilkSettingViewModel = hiltViewModel(),
) {
    val detectMethod = stringResource(R.string.config_milk_tank_detect_method)
    val methodMilkFlow = stringResource(R.string.config_milk_tank_detect_milk_flow)
    val methodMilkSensor = stringResource(R.string.config_milk_tank_detect_milk_flow_sensor)
    val methodMilkTemp = stringResource(R.string.config_milk_tank_detect_temp_detect)
    val tankDetect = stringResource(R.string.config_milk_tank_detect)
    val tankDetectClose = stringResource(R.string.config_milk_tank_detect_close)
    val tankDetectTemp = stringResource(R.string.config_milk_tank_temperature)
    val tankDetectRefrigerator = stringResource(R.string.config_milk_tank_refrigerator)
    val tankDetectBoth = stringResource(R.string.config_milk_tank_both)
    val milkValve = stringResource(R.string.config_milk_valve)
    val valveExist = stringResource(R.string.config_milk_valve_exist)
    val valveNotExist = stringResource(R.string.config_milk_valve_not_exist)
    val milkOutlet = stringResource(R.string.config_milk_outlet)
    val outletCoffee = stringResource(R.string.config_milk_outlet_coffee)
    val outletMilkStick = stringResource(R.string.config_milk_outlet_milk_stick)
    val foamWarning = stringResource(R.string.config_milk_foam_temp_warning)
    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val milkTempDetect = stringResource(R.string.config_milk_temp_detect)
    val milkSettingMode = stringResource(R.string.config_milk_setting_mode)
    val modeStandard = stringResource(R.string.config_milk_setting_mode_standard)
    val modeExtend = stringResource(R.string.config_milk_setting_mode_extend)
    val steamBoilerPressure = stringResource(R.string.config_milk_setting_steam_pressure)

    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val scrollAccuracy = remember { mutableStateOf(1) }
    val titleValue = remember { mutableStateOf("") }

    val detectMethodValue by viewModel.detectMethod.collectAsState()
    val tankDetectValue by viewModel.tankDetect.collectAsState()
    val valveValue by viewModel.valve.collectAsState()
    val outletValue by viewModel.outlet.collectAsState()
    val foamWarningValue by viewModel.foamWarning.collectAsState()
    val tempDetectValue by viewModel.tempDetect.collectAsState()
    val settingModeValue by viewModel.settingMode.collectAsState()
    val steamPressureValue by viewModel.steamPressure.collectAsState()

    val methodValue = when (detectMethodValue) {
        0 -> methodMilkFlow
        1 -> methodMilkSensor
        2 -> methodMilkTemp
        else -> ""
    }
    val tank = when (tankDetectValue) {
        0 -> tankDetectClose
        1 -> tankDetectTemp
        2 -> tankDetectRefrigerator
        3 -> tankDetectBoth
        else -> ""
    }
    val valve = if (valveValue == 0) valveExist else valveNotExist
    val outlet = if (outletValue == 0) outletCoffee else outletMilkStick
    val warning = if (foamWarningValue == 0) on else off
    val mode = if (settingModeValue == 0) modeStandard else modeExtend
    val tempDetect = if (tempDetectValue == 0) on else off

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 160.dp, end = 95.dp)
        ) {
            DisplayItemLayout(detectMethod, methodValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_DETECT_METHOD
                defaultValue.value = methodValue
                titleValue.value = detectMethod
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(methodMilkFlow, 0),
                        Pair(methodMilkSensor, 1),
                        Pair(methodMilkTemp, 2)
                    )
                )
            }
            DisplayItemLayout(tankDetect, tank, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = MILK_TANK_DETECT
                defaultValue.value = tank
                titleValue.value = tankDetect
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(tankDetectClose, 0),
                        Pair(tankDetectTemp, 1),
                        Pair(tankDetectRefrigerator, 2),
                        Pair(tankDetectBoth, 3)
                    )
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            DisplayItemLayout(milkValve, valve, Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_VALVE
                titleValue.value = milkValve
                defaultValue.value = valve
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(valveExist, 0),
                        Pair(valveNotExist, 1)
                    )
                )
            }
            DisplayItemLayout(milkOutlet, outlet, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = MILK_OUTLET
                titleValue.value = milkOutlet
                defaultValue.value = outlet
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(outletCoffee, 0),
                        Pair(outletMilkStick, 1)
                    )
                )
            }

            DisplayItemLayout(foamWarning, warning, Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_FOAM_WARNING
                titleValue.value = foamWarning
                defaultValue.value = warning
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, 0),
                        Pair(off, 1)
                    )
                )
            }
            DisplayItemLayout(milkTempDetect, tempDetect, Color(0xFF2A2B2D), enable = false) {
                itemSelectIndex.value = MILK_TEMP_DETECT
                titleValue.value = milkTempDetect
                defaultValue.value = tempDetect
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, 0),
                        Pair(off, 1)
                    )
                )
            }
            DisplayItemLayout(milkSettingMode, mode, Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_SETTING_MODE
                titleValue.value = milkSettingMode
                defaultValue.value = mode
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(modeStandard, 0),
                        Pair(modeExtend, 1)
                    )
                )
            }
            DisplayItemLayout(steamBoilerPressure, steamPressureValue.toString(),
                Color(0xFF2A2B2D)) {
                itemSelectIndex.value = MILK_STEAM_PRESSURE
                scrollDefaultValue.value = steamPressureValue
                scrollRangeStart.value = 1.0f
                scrollRangeEnd.value = 2.0f
                scrollUnit.value = "[mm/s]"
                scrollAccuracy.value = ACCURACY_2
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            when (itemSelectIndex.value) {
                MILK_STEAM_PRESSURE -> {
                    key(scrollDefaultValue.value) {
                        UnitValueScrollBar(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 40.dp, end = 90.dp)
                                .width(550.dp)
                                .wrapContentHeight(),
                            value = scrollDefaultValue.value,
                            rangeStart = scrollRangeStart.value,
                            rangeEnd = scrollRangeEnd.value,
                            unit = scrollUnit.value,
                            accuracy = scrollAccuracy.value
                        ) { changeValue ->
                            viewModel.savePageOneValue(itemSelectIndex.value, changeValue)
                        }
                    }
                }
                else -> {
                    ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                        { _, value ->
                            viewModel.savePageOneValue(itemSelectIndex.value, value)
                            itemSelectIndex.value = INVALID_INT
                        }, {
                            itemSelectIndex.value = INVALID_INT
                        }
                    )
                }
            }
        }
    }
}