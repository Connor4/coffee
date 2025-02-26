package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ACCURACY_1
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MILK_SOURCE_FOAM_CALIBRATION
import com.inno.coffee.utilities.MILK_SOURCE_MILK_CALIBRATION
import com.inno.coffee.utilities.MILK_SOURCE_NAME_LEFT
import com.inno.coffee.utilities.MILK_SOURCE_NAME_RIGHT
import com.inno.coffee.utilities.MILK_SOURCE_TEMP
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.config.MilkSettingViewModel

@Composable
fun MilkSettingPage2(
    viewModel: MilkSettingViewModel = hiltViewModel(),
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val scrollAccuracy = remember { mutableStateOf(1) }

    val nameLeft = stringResource(R.string.config_milk_source_name_left)
    val nameRight = stringResource(R.string.config_milk_source_name_right)
    val temp = stringResource(R.string.config_milk_tank_temperature)
    val milkCalibration = stringResource(R.string.config_milk_source_milk_calibration)
    val foamCalibration = stringResource(R.string.config_milk_source_foam_calibration)

    val tempValue by viewModel.sourceTemp.collectAsState()
    val milkCalibrationValue by viewModel.sourceMilkCalibration.collectAsState()
    val foamCalibrationValue by viewModel.sourceFoamCalibration.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = stringResource(R.string.config_milk_source_1_title), color = Color(0xFF32C5FF),
            fontSize = 6.nsp(), fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 50.dp, top = 20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 54.dp, top = 160.dp, end = 95.dp)
        ) {
            DisplayItemLayout(nameLeft, "Semi", Color(0xFF191A1D), enable = false) {
                itemSelectIndex.value = MILK_SOURCE_NAME_LEFT
            }
            DisplayItemLayout(nameRight, "Semi", Color(0xFF2A2B2D), enable = false) {
                itemSelectIndex.value = MILK_SOURCE_NAME_RIGHT
            }
            DisplayItemLayout(temp, tempValue.toString(),
                Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_SOURCE_TEMP
                scrollDefaultValue.value = tempValue
                scrollRangeStart.value = 60f
                scrollRangeEnd.value = 70f
                scrollUnit.value = "[°C]"
                scrollAccuracy.value = ACCURACY_1
            }
            DisplayItemLayout(milkCalibration, milkCalibrationValue.toString(),
                Color(0xFF2A2B2D)) {
                itemSelectIndex.value = MILK_SOURCE_MILK_CALIBRATION
                scrollDefaultValue.value = milkCalibrationValue
                scrollRangeStart.value = 10.0f
                scrollRangeEnd.value = 30.0f
                scrollUnit.value = "[s]"
                scrollAccuracy.value = ACCURACY_1
            }
            DisplayItemLayout(foamCalibration, foamCalibrationValue.toString(),
                Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_SOURCE_FOAM_CALIBRATION
                scrollDefaultValue.value = foamCalibrationValue
                scrollRangeStart.value = 1.0f
                scrollRangeEnd.value = 100.0f
                scrollUnit.value = ""
                scrollAccuracy.value = ACCURACY_1
            }
        }
        if (itemSelectIndex.value != INVALID_INT) {
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
                    viewModel.savePageTwoValue(itemSelectIndex.value, changeValue)
                }
            }
        }
    }
}