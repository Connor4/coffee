package com.inno.coffee.ui.settings.config

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MILK_REVERSE_FLUSHING_LEFT
import com.inno.coffee.utilities.MILK_REVERSE_FLUSHING_RIGHT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.config.MilkSettingViewModel

@Composable
fun MilkSettingPage3(
    viewModel: MilkSettingViewModel = hiltViewModel(),
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val scrollAccuracy = remember { mutableStateOf(1) }

    val flushLeft by viewModel.reverseFlushingLeft.collectAsState()
    val flushRight by viewModel.reverseFlushingRight.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = stringResource(R.string.config_milk_source_1_title), color = Color(0xFF32C5FF),
            fontSize = 6.nsp(), fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 54.dp, top = 20.dp))

        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 90.dp)
                .width(300.dp)
                .height(50.dp),
            text = stringResource(id = R.string.config_milk_check_flush_volume)
        ) {

        }

        Text(text = stringResource(R.string.config_milk_flush_warning), color = Color(0xFFE02020),
            fontSize = 6.nsp(), textAlign = TextAlign.Center, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 300.dp)
                .width(660.dp)
                .wrapContentHeight())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 54.dp, top = 160.dp, end = 95.dp)
        ) {
            DisplayItemLayout(stringResource(R.string.config_milk_flush_left), flushLeft.toString(),
                Color(0xFF191A1D)) {
                itemSelectIndex.value = MILK_REVERSE_FLUSHING_LEFT
                scrollDefaultValue.value = flushLeft
            }
            DisplayItemLayout(stringResource(R.string.config_milk_flush_right),
                flushRight.toString(),
                Color(0xFF2A2B2D)) {
                itemSelectIndex.value = MILK_REVERSE_FLUSHING_RIGHT
                scrollDefaultValue.value = flushRight
            }
        }
        if (itemSelectIndex.value != INVALID_INT) {
            key(scrollDefaultValue.value) {
                UnitValueScrollBar(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 80.dp, end = 90.dp)
                        .width(550.dp)
                        .wrapContentHeight(),
                    value = scrollDefaultValue.value,
                    rangeStart = 0f,
                    rangeEnd = 5000f,
                    unit = "[ms]",
                    accuracy = scrollAccuracy.value
                ) { changeValue ->
                    viewModel.savePageThreeValue(itemSelectIndex.value, changeValue)
                }
            }
        }
    }
}