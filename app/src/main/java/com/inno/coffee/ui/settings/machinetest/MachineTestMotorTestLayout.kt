package com.inno.coffee.ui.settings.machinetest

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
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_CURRENT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_SPEED
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_STEP
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestOutputViewModel

@Composable
fun MachineTestMotorLayout(
    viewModel: MachineTestOutputViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }

    val step = viewModel.step.collectAsState()
    val speed = viewModel.speed.collectAsState()
    val current = viewModel.current.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_motor_test),
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
            DisplayItemLayout(stringResource(R.string.machine_test_motor_steps),
                "${step.value}", Color(0xFF191A1D), unit = "[step]") {
                itemSelectIndex.value = MACHINE_TEST_MOTOR_STEP
                scrollDefaultValue.value = step.value.toFloat()
                scrollRangeStart.value = 0f
                scrollRangeEnd.value = 2500f
                scrollUnit.value = "[step]"
            }
            DisplayItemLayout(stringResource(R.string.machine_test_motor_speed),
                "${speed.value}", Color(0xFF2A2B2D), unit = "[rpm]") {
                itemSelectIndex.value = MACHINE_TEST_MOTOR_SPEED
                scrollDefaultValue.value = speed.value.toFloat()
                scrollRangeStart.value = 200f
                scrollRangeEnd.value = 2000f
                scrollUnit.value = "[rpm]"
            }
            DisplayItemLayout(stringResource(R.string.machine_test_maximum_current),
                "${current.value}", Color(0xFF191A1D), unit = "[eUnit]") {
                itemSelectIndex.value = MACHINE_TEST_MOTOR_CURRENT
                scrollDefaultValue.value = current.value.toFloat()
                scrollRangeStart.value = 0f
                scrollRangeEnd.value = 200f
                scrollUnit.value = "[eUnit]"
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
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
                ) { changeValue ->
                    viewModel.saveMotorTestValue(itemSelectIndex.value, changeValue.toInt())
                }
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMotorTest() {
    MachineTestMotorLayout()
}