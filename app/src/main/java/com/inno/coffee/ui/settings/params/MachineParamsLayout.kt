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
import com.inno.coffee.utilities.PARAMS_KEY_BREW_BALANCE
import com.inno.coffee.utilities.PARAMS_KEY_BREW_PRE_HEATING
import com.inno.coffee.utilities.PARAMS_KEY_COLD_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_GRINDER_PURGE_FUNCTION
import com.inno.coffee.utilities.PARAMS_KEY_GROUNDS_QUANTITY
import com.inno.coffee.utilities.PARAMS_KEY_NTC_LEFT
import com.inno.coffee.utilities.PARAMS_KEY_NTC_RIGHT
import com.inno.coffee.utilities.PARAMS_KEY_NUMBER_OF_CYCLES_RINSE
import com.inno.coffee.utilities.PARAMS_KEY_STEAM_BOILER_PRESSURE
import com.inno.coffee.utilities.PARAMS_KEY_WARM_RINSE
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_FIVE_KG
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_IGNORE
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_ONE_KG
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_ONLY
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_SEVEN_FIVE_KG
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_TEN_KG
import com.inno.coffee.utilities.PARAMS_VALUE_DRAWER_TWO_FIVE_KG
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_10
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_15
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_e
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_r
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_2
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_20
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_25
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_3
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_4
import com.inno.coffee.utilities.PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_5
import com.inno.coffee.utilities.PARAMS_VALUE_PRE_HEATING_AUTOMATIC
import com.inno.coffee.utilities.PARAMS_VALUE_PRE_HEATING_FORCE_10
import com.inno.coffee.utilities.PARAMS_VALUE_PRE_HEATING_FORCE_5
import com.inno.coffee.utilities.PARAMS_VALUE_PRE_HEATING_OFF
import com.inno.coffee.utilities.PARAMS_VALUE_PRE_HEATING_REMINDER_10
import com.inno.coffee.utilities.PARAMS_VALUE_PRE_HEATING_REMINDER_5
import com.inno.coffee.utilities.PARAMS_VALUE_PURGE_FUNCTION_ETC
import com.inno.coffee.utilities.PARAMS_VALUE_PURGE_FUNCTION_GRINDER_PURGE
import com.inno.coffee.utilities.PARAMS_VALUE_PURGE_FUNCTION_NONE
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.params.MachineParamsViewModel

@Composable
fun MachineParamsLayout(
    viewModel: MachineParamsViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val scrollAccuracy = remember { mutableStateOf(1) }
    val titleValue = remember { mutableStateOf("") }

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
    val unit = viewModel.temperatureUnit.collectAsState()

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val drawerOnly = stringResource(R.string.params_grounds_drawer_only)
    val ignore = stringResource(R.string.params_grounds_drawer_ignore)
    val oneKg = stringResource(R.string.params_grounds_drawer_one_kg)
    val twoFiveKg = stringResource(R.string.params_grounds_drawer_two_five_kg)
    val fiveKg = stringResource(R.string.params_grounds_drawer_five_kg)
    val sevenFiveKg = stringResource(R.string.params_grounds_drawer_seven_five_kg)
    val tenKg = stringResource(R.string.params_grounds_drawer_ten_kg)
    val automatic = stringResource(R.string.params_pre_heating_automatic_drawer)
    val force5 = stringResource(R.string.params_pre_heating_force_manual_5)
    val force10 = stringResource(R.string.params_pre_heating_force_manual_10)
    val reminder5 = stringResource(R.string.params_pre_heating_manual_reminder_5)
    val reminder10 = stringResource(R.string.params_pre_heating_manual_reminder_10)
    val grinderPurge = stringResource(R.string.params_purge_function_grinder_purge)
    val etc = stringResource(R.string.params_purge_function_etc)
    val regular = stringResource(R.string.params_screen_rinse_regular)
    val extra = stringResource(R.string.params_screen_rinse_extra)

    val drawerString = stringResource(R.string.params_grounds_drawer_quantity)
    val balanceString = stringResource(R.string.params_brew_group_load_balancing)
    val heatingString = stringResource(R.string.params_brew_group_pre_heating)
    val purgeString = stringResource(R.string.params_grinder_purge_function)
    val screenRinseString = stringResource(R.string.params_number_of_cycles_rinse)

    val drawerQuantityValue = when (groundsDrawerQuantity.value) {
        PARAMS_VALUE_DRAWER_ONLY -> {
            drawerOnly
        }
        PARAMS_VALUE_DRAWER_IGNORE -> {
            ignore
        }
        PARAMS_VALUE_DRAWER_ONE_KG -> {
            oneKg
        }
        PARAMS_VALUE_DRAWER_TWO_FIVE_KG -> {
            twoFiveKg
        }
        PARAMS_VALUE_DRAWER_FIVE_KG -> {
            fiveKg
        }
        PARAMS_VALUE_DRAWER_SEVEN_FIVE_KG -> {
            sevenFiveKg
        }
        PARAMS_VALUE_DRAWER_TEN_KG -> {
            tenKg
        }
        else -> {
            drawerOnly
        }
    }
    val balanceValue = if (brewGroupLoadBalancing.value) on else off
    val preHeatingValue = when (brewGroupPreHeating.value) {
        PARAMS_VALUE_PRE_HEATING_OFF -> {
            off
        }
        PARAMS_VALUE_PRE_HEATING_AUTOMATIC -> {
            automatic
        }
        PARAMS_VALUE_PRE_HEATING_FORCE_5 -> {
            force5
        }
        PARAMS_VALUE_PRE_HEATING_FORCE_10 -> {
            force10
        }
        PARAMS_VALUE_PRE_HEATING_REMINDER_5 -> {
            reminder5
        }
        PARAMS_VALUE_PRE_HEATING_REMINDER_10 -> {
            reminder10
        }
        else -> {
            off
        }
    }
    val purgeValue = when (grinderPurgeFunction.value) {
        PARAMS_VALUE_PURGE_FUNCTION_NONE -> {
            off
        }
        PARAMS_VALUE_PURGE_FUNCTION_GRINDER_PURGE -> {
            grinderPurge
        }
        PARAMS_VALUE_PURGE_FUNCTION_ETC -> {
            etc
        }
        else -> {
            ""
        }
    }
    val screenRinseValue = when (numberOfCyclesRinse.value) {
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_r -> {
            "1-$regular"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_e -> {
            "1-$extra"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_2 -> {
            "2"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_3 -> {
            "3"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_4 -> {
            "4"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_5 -> {
            "5"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_10 -> {
            "10"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_15 -> {
            "15"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_20 -> {
            "20"
        }
        PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_25 -> {
            "25"
        }
        else -> {
            ""
        }
    }
    val unitValue = if (unit.value) "°F" else "°C"

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
                "${boilerTemp.value}  [$unitValue]", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_BOILER_TEMP
                scrollDefaultValue.value = viewModel.temperatureDisplay(boilerTemp.value)
                scrollRangeStart.value = viewModel.temperatureDisplay(80)
                scrollRangeEnd.value = viewModel.temperatureDisplay(100)
                scrollUnit.value = "[$unitValue]"
                scrollAccuracy.value = 1
            }
            DisplayItemLayout(stringResource(R.string.params_cold_rinse_quantity),
                "${coldRinseQuantity.value}", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = PARAMS_KEY_COLD_RINSE
                scrollDefaultValue.value = coldRinseQuantity.value.toFloat()
                scrollRangeStart.value = 100f
                scrollRangeEnd.value = 2000f
                scrollUnit.value = "[tick]"
                scrollAccuracy.value = 1
            }
            DisplayItemLayout(stringResource(R.string.params_warm_rinse_quantity),
                "${warmRinseQuantity.value}", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_WARM_RINSE
                scrollDefaultValue.value = warmRinseQuantity.value.toFloat()
                scrollRangeStart.value = 20f
                scrollRangeEnd.value = 200f
                scrollUnit.value = "[tick]"
                scrollAccuracy.value = 1
            }
            DisplayItemLayout(drawerString,
                drawerQuantityValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = PARAMS_KEY_GROUNDS_QUANTITY
                titleValue.value = drawerString
                defaultValue.value = drawerQuantityValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(drawerOnly, PARAMS_VALUE_DRAWER_ONLY),
                        Pair(ignore, PARAMS_VALUE_DRAWER_IGNORE),
                        Pair(oneKg, PARAMS_VALUE_DRAWER_ONE_KG),
                        Pair(twoFiveKg, PARAMS_VALUE_DRAWER_TWO_FIVE_KG),
                        Pair(fiveKg, PARAMS_VALUE_DRAWER_FIVE_KG),
                        Pair(sevenFiveKg, PARAMS_VALUE_DRAWER_SEVEN_FIVE_KG),
                        Pair(tenKg, PARAMS_VALUE_DRAWER_TEN_KG)
                    )
                )
            }
            DisplayItemLayout(balanceString,
                balanceValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_BREW_BALANCE
                titleValue.value = balanceString
                defaultValue.value = balanceValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, true),
                        Pair(off, false)
                    )
                )
            }
            DisplayItemLayout(heatingString,
                preHeatingValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = PARAMS_KEY_BREW_PRE_HEATING
                titleValue.value = heatingString
                defaultValue.value = preHeatingValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(off, PARAMS_VALUE_PRE_HEATING_OFF),
                        Pair(automatic, PARAMS_VALUE_PRE_HEATING_AUTOMATIC),
                        Pair(force5, PARAMS_VALUE_PRE_HEATING_FORCE_5),
                        Pair(force10, PARAMS_VALUE_PRE_HEATING_FORCE_10),
                        Pair(reminder5, PARAMS_VALUE_PRE_HEATING_REMINDER_5),
                        Pair(reminder10, PARAMS_VALUE_PRE_HEATING_REMINDER_10)
                    )
                )
            }
            DisplayItemLayout(purgeString,
                purgeValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_GRINDER_PURGE_FUNCTION
                titleValue.value = purgeString
                defaultValue.value = purgeValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(off, PARAMS_VALUE_PURGE_FUNCTION_NONE),
                        Pair(grinderPurge, PARAMS_VALUE_PURGE_FUNCTION_GRINDER_PURGE),
                        Pair(etc, PARAMS_VALUE_PURGE_FUNCTION_ETC)
                    )
                )
            }
            DisplayItemLayout(screenRinseString,
                screenRinseValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = PARAMS_KEY_NUMBER_OF_CYCLES_RINSE
                titleValue.value = screenRinseString
                defaultValue.value = screenRinseValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair("1-$regular", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_r),
                        Pair("1-$extra", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_e),
                        Pair("2", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_2),
                        Pair("3", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_3),
                        Pair("4", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_4),
                        Pair("5", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_5),
                        Pair("10", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_10),
                        Pair("15", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_15),
                        Pair("20", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_20),
                        Pair("25", PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_25)
                    )
                )
            }
            DisplayItemLayout(stringResource(R.string.params_steam_boiler_pressure),
                "${steamBoilerPressure.value}", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_STEAM_BOILER_PRESSURE
                scrollDefaultValue.value = steamBoilerPressure.value.toFloat()
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 2f
                scrollUnit.value = "[bar]"
                scrollAccuracy.value = 2
            }
            DisplayItemLayout(stringResource(R.string.params_ntc_correction_steam_left),
                "${ntcCorrectionSteamLeft.value}  [$unitValue]", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = PARAMS_KEY_NTC_LEFT
                scrollDefaultValue.value = viewModel.temperatureDisplay(ntcCorrectionSteamLeft
                    .value)
                scrollRangeStart.value = viewModel.temperatureDisplay(-10)
                scrollRangeEnd.value = viewModel.temperatureDisplay(10)
                scrollUnit.value = "[$unitValue]"
                scrollAccuracy.value = 1
            }
            DisplayItemLayout(stringResource(R.string.params_ntc_correction_steam_right),
                "${ntcCorrectionSteamRight.value}  [$unitValue]", Color(0xFF191A1D)) {
                itemSelectIndex.value = PARAMS_KEY_NTC_RIGHT
                scrollDefaultValue.value = viewModel.temperatureDisplay(ntcCorrectionSteamRight
                    .value)
                scrollRangeStart.value = viewModel.temperatureDisplay(-10)
                scrollRangeEnd.value = viewModel.temperatureDisplay(10)
                scrollUnit.value = "[$unitValue]"
                scrollAccuracy.value = 1
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            if (itemSelectIndex.value == PARAMS_KEY_COLD_RINSE ||
                    itemSelectIndex.value == PARAMS_KEY_WARM_RINSE ||
                    itemSelectIndex.value == PARAMS_KEY_BOILER_TEMP ||
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
                        value = scrollDefaultValue.value,
                        rangeStart = scrollRangeStart.value,
                        rangeEnd = scrollRangeEnd.value,
                        unit = scrollUnit.value,
                        accuracy = scrollAccuracy.value
                    ) { changeValue ->
                        viewModel.saveMachineParamsValue(itemSelectIndex.value, changeValue.toInt())
                    }
                }
            } else {
                ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                    { _, value ->
                        viewModel.saveMachineParamsValue(itemSelectIndex.value, value)
                        itemSelectIndex.value = INVALID_INT
                    }, {
                        itemSelectIndex.value = INVALID_INT
                    }
                )
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineParams() {
    MachineParamsLayout()
}