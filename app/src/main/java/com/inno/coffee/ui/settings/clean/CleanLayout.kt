package com.inno.coffee.ui.settings.clean

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.ui.settings.display.groupone.DisplaySettingTimeLayout
import com.inno.coffee.utilities.CLEAN_MILK_WEEKEND_CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_MODE_AUTO
import com.inno.coffee.utilities.CLEAN_MODE_MANUAL
import com.inno.coffee.utilities.CLEAN_MODE_PERIOD
import com.inno.coffee.utilities.CLEAN_PERIOD_TIME
import com.inno.coffee.utilities.CLEAN_SET_TIME
import com.inno.coffee.utilities.CLEAN_STANDBY_AFTER_CLEANING
import com.inno.coffee.utilities.CLEAN_STANDBY_BUTTON
import com.inno.coffee.utilities.CLEAN_STANDBY_ON_OFF_TIME
import com.inno.coffee.utilities.CLEAN_TIME_TOLERANCE
import com.inno.coffee.utilities.CLEAN_WEEKEND_CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_WEEKEND_NO_FRI_SAT
import com.inno.coffee.utilities.CLEAN_WEEKEND_NO_SAT_SUN
import com.inno.coffee.utilities.CLEAN_WEEKEND_OFF
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.clean.CleanViewModel

@Composable
fun CleanLayout(
    viewModel: CleanViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val titleValue = remember { mutableStateOf("") }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val scrollAccuracy = remember { mutableStateOf(1) }

    val mode = viewModel.mode.collectAsState()
    val cleanPeriod = viewModel.cleanPeriod.collectAsState()
    val cleanTime = viewModel.cleanTime.collectAsState()
    val timeTolerance = viewModel.timeTolerance.collectAsState()
    val weekendCleanMode = viewModel.weekendCleanMode.collectAsState()
    val milkWeekendCleanMode = viewModel.milkWeekendCleanMode.collectAsState()
    val afterCleaning = viewModel.afterCleaning.collectAsState()
    val standbyButton = viewModel.standbyButton.collectAsState()
    val switchValue = viewModel.switchValue.collectAsState()

    val modeTitle = stringResource(R.string.clean_mode)
    val weekendTitle = stringResource(R.string.clean_weekend_clean_mode)
    val milkTitle = stringResource(R.string.clean_milk_weekend_clean_mode)
    val afterTitle = stringResource(R.string.clean_standby_after_cleaning)
    val buttonTitle = stringResource(R.string.clean_standby_button)

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val modeString1 = stringResource(R.string.clean_mode_periodic)
    val modeString2 = stringResource(R.string.clean_specified_time_auto)
    val modeString3 = stringResource(R.string.clean_specified_time_manual)
    val weekendString1 = stringResource(R.string.clean_no_sat_sun)
    val weekendString2 = stringResource(R.string.clean_no_fri_sat)

    val modeValue = when (mode.value) {
        CLEAN_MODE_PERIOD -> {
            modeString1
        }
        CLEAN_MODE_AUTO -> {
            modeString2
        }
        CLEAN_MODE_MANUAL -> {
            modeString3
        }
        else -> {
            ""
        }
    }
    val weekendValue = when (weekendCleanMode.value) {
        CLEAN_WEEKEND_OFF -> {
            off
        }
        CLEAN_WEEKEND_NO_SAT_SUN -> {
            weekendString1
        }
        CLEAN_WEEKEND_NO_FRI_SAT -> {
            weekendString2
        }
        else -> {
            ""
        }
    }
    val milkValue = if (milkWeekendCleanMode.value) {
        on
    } else {
        off
    }
    val afterValue = if (afterCleaning.value) {
        on
    } else {
        off
    }
    val buttonValue = if (standbyButton.value) {
        on
    } else {
        off
    }
    val showSwitchValue = if (switchValue.value != 0) {
        on
    } else {
        off
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.common_machine_clean),
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

        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 692.dp)
                .width(254.dp)
                .height(50.dp),
            text = stringResource(id = R.string.clean_reset_next_clean_date)
        ) {
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 95.dp)
        ) {
            DisplayItemLayout(modeTitle, modeValue, Color(0xFF191A1D)
            ) {
                itemSelectIndex.value = CLEAN_MODE
                titleValue.value = modeTitle
                defaultValue.value = modeValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(modeString1, CLEAN_MODE_PERIOD),
                        Pair(modeString2, CLEAN_MODE_AUTO),
                        Pair(modeString3, CLEAN_MODE_MANUAL),
                    )
                )
            }
            if (mode.value == CLEAN_MODE_PERIOD) {
                DisplayItemLayout(key = stringResource(R.string.clean_cleaning_period),
                    value = "${cleanPeriod.value}", backgroundColor = Color(0xFF2A2B2D),
                    unit = "[h]"
                ) {
                    itemSelectIndex.value = CLEAN_PERIOD_TIME
                    scrollDefaultValue.value = cleanPeriod.value.toFloat()
                    scrollRangeStart.value = 1f
                    scrollRangeEnd.value = 26f
                    scrollUnit.value = "[h]"
                }
            } else {
                DisplayItemLayout(stringResource(R.string.clean_cleaning_time),
                    cleanTime.value,
                    Color(0xFF2A2B2D)
                ) {
                    itemSelectIndex.value = CLEAN_SET_TIME
                }
            }
            DisplayItemLayout(stringResource(R.string.clean_time_tolerance),
                "${timeTolerance.value}", Color(0xFF191A1D), "[h]"
            ) {
                itemSelectIndex.value = CLEAN_TIME_TOLERANCE
                scrollDefaultValue.value = timeTolerance.value.toFloat()
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 5f
                scrollUnit.value = "[h]"
            }
            DisplayItemLayout(weekendTitle, weekendValue, Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = CLEAN_WEEKEND_CLEAN_MODE
                titleValue.value = weekendTitle
                defaultValue.value = weekendValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(off, CLEAN_WEEKEND_OFF),
                        Pair(weekendString1, CLEAN_WEEKEND_NO_SAT_SUN),
                        Pair(weekendString2, CLEAN_WEEKEND_NO_FRI_SAT),
                    )
                )
            }
            DisplayItemLayout(milkTitle, milkValue, Color(0xFF191A1D)
            ) {
                itemSelectIndex.value = CLEAN_MILK_WEEKEND_CLEAN_MODE
                titleValue.value = milkTitle
                defaultValue.value = milkValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, true),
                        Pair(off, false)
                    )
                )
            }
            DisplayItemLayout(afterTitle, afterValue, Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = CLEAN_STANDBY_AFTER_CLEANING
                titleValue.value = afterTitle
                defaultValue.value = afterValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, true),
                        Pair(off, false)
                    )
                )
            }
            DisplayItemLayout(stringResource(R.string.clean_standby_time_settings),
                showSwitchValue, Color(0xFF191A1D)
            ) {
                itemSelectIndex.value = CLEAN_STANDBY_ON_OFF_TIME
            }
            DisplayItemLayout(buttonTitle, buttonValue, Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = CLEAN_STANDBY_BUTTON
                titleValue.value = buttonTitle
                defaultValue.value = buttonValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, true),
                        Pair(off, false)
                    )
                )
            }
        }
        if (itemSelectIndex.value != INVALID_INT) {
            when (itemSelectIndex.value) {
                CLEAN_TIME_TOLERANCE, CLEAN_PERIOD_TIME -> {
                    key(scrollDefaultValue.value) {
                        UnitValueScrollBar(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 172.dp, end = 95.dp)
                                .width(550.dp)
                                .wrapContentHeight(),
                            value = scrollDefaultValue.value,
                            rangeStart = scrollRangeStart.value,
                            rangeEnd = scrollRangeEnd.value,
                            unit = scrollUnit.value,
                            accuracy = scrollAccuracy.value
                        ) { changeValue ->
                            viewModel.setCleanValue(itemSelectIndex.value, changeValue.toInt())
                        }
                    }
                }
                CLEAN_SET_TIME -> {
                    val (hour, minute) = viewModel.getHourAndMinute(14)
                    DisplaySettingTimeLayout(
                        { selectedHour, selectedMin ->
                            viewModel.saveCleanTime(selectedHour, selectedMin)
                            itemSelectIndex.value = INVALID_INT
                        },
                        {
                            itemSelectIndex.value = INVALID_INT
                        },
                        {
                            itemSelectIndex.value = INVALID_INT
                        },
                        defaultHour = hour, defaultMinute = minute,
                    )
                }
                CLEAN_STANDBY_ON_OFF_TIME -> {
                    StandbyOnOffTimeLayout(viewModel) {
                        itemSelectIndex.value = INVALID_INT
                    }
                }
                else -> {
                    ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                        { _, value ->
                            viewModel.setCleanValue(itemSelectIndex.value, value)
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

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewClean() {
    CleanLayout()
}