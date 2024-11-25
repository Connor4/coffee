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
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.CLEAN_MILK_WEEKEND_CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_MODE
import com.inno.coffee.utilities.CLEAN_MODE_AUTO
import com.inno.coffee.utilities.CLEAN_MODE_MANUAL
import com.inno.coffee.utilities.CLEAN_MODE_PERIOD
import com.inno.coffee.utilities.CLEAN_SET_TIME
import com.inno.coffee.utilities.CLEAN_STANDBY_AFTER_CLEANING
import com.inno.coffee.utilities.CLEAN_STANDBY_BUTTON
import com.inno.coffee.utilities.CLEAN_STANDBY_ON_OFF_TIME
import com.inno.coffee.utilities.CLEAN_TIME_TOLERANCE
import com.inno.coffee.utilities.CLEAN_WEEKEND_CLEAN_MODE
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp

@Composable
fun CleanLayout(
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

    val mode = 1
    val cleanPeriod = 1f
    val cleanTime = 1f
    val timeTolerance = 1f
    val weekendCleanMode = 1f
    val milkWeekendCleanMode = 1f
    val afterCleaning = false
    val standbyButton = false

    val modeTitle = stringResource(R.string.clean_mode)

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val modeString1 = stringResource(R.string.clean_mode_periodic)
    val modeString2 = stringResource(R.string.clean_specified_time_auto)
    val modeString3 = stringResource(R.string.clean_specified_time_manual)

    val modeValue = when (mode) {
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
                .width(220.dp)
                .height(50.dp)
                .padding(top = 172.dp, end = 95.dp),
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
            if (mode == CLEAN_MODE_PERIOD) {
                DisplayItemLayout(key = stringResource(R.string.clean_cleaning_period),
                    value = "2", backgroundColor = Color(0xFF2A2B2D), unit = "[h]"
                ) {
                    itemSelectIndex.value = CLEAN_MODE
                    scrollDefaultValue.value = cleanTime
                    scrollRangeStart.value = 1f
                    scrollRangeEnd.value = 26f
                    scrollUnit.value = "[h]"
                }
            } else {
                DisplayItemLayout(stringResource(R.string.clean_set_time),
                    "$cleanTime",
                    Color(0xFF2A2B2D)
                ) {
                    itemSelectIndex.value = CLEAN_SET_TIME
                }
            }
            DisplayItemLayout(stringResource(R.string.clean_time_tolerance),
                "$timeTolerance", Color(0xFF191A1D), "[h]"
            ) {
                itemSelectIndex.value = CLEAN_TIME_TOLERANCE
                scrollDefaultValue.value = timeTolerance
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 5f
                scrollUnit.value = "[h]"
            }
            DisplayItemLayout(stringResource(R.string.clean_weekend_clean_mode),
                "1",
                Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = CLEAN_WEEKEND_CLEAN_MODE
            }
            DisplayItemLayout(stringResource(R.string.clean_milk_weekend_clean_mode),
                "2",
                Color(0xFF191A1D)
            ) {
                itemSelectIndex.value = CLEAN_MILK_WEEKEND_CLEAN_MODE
            }
            DisplayItemLayout(stringResource(R.string.clean_standby_after_cleaning),
                "1",
                Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = CLEAN_STANDBY_AFTER_CLEANING
            }
            DisplayItemLayout(stringResource(R.string.clean_standby_time_settings),
                "2",
                Color(0xFF191A1D)
            ) {
                itemSelectIndex.value = CLEAN_STANDBY_ON_OFF_TIME
            }
            DisplayItemLayout(stringResource(R.string.clean_standby_button),
                "1",
                Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = CLEAN_STANDBY_BUTTON
            }
        }
        if (itemSelectIndex.value != INVALID_INT) {
            if (itemSelectIndex.value == CLEAN_MODE) {
                ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                    { _, value ->
                        itemSelectIndex.value = INVALID_INT
                    }, {
                        itemSelectIndex.value = INVALID_INT
                    }
                )
            } else {
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
                    }
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