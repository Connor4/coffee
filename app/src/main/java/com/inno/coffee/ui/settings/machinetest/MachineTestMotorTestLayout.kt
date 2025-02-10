package com.inno.coffee.ui.settings.machinetest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_CURRENT
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_LEFT_BOTTOM
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_LEFT_TOP
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_RIGHT_BOTTOM
import com.inno.coffee.utilities.MACHINE_TEST_MOTOR_RIGHT_TOP
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
    val selectMotor = remember { mutableIntStateOf(MACHINE_TEST_MOTOR_LEFT_TOP) }

    val step = viewModel.step.collectAsState()
    val speed = viewModel.speed.collectAsState()
    val current = viewModel.current.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.motorInit()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.motorInit()
        }
    }

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
                scrollRangeEnd.value = if (selectMotor.value == MACHINE_TEST_MOTOR_LEFT_TOP ||
                        selectMotor.value == MACHINE_TEST_MOTOR_RIGHT_TOP) 2500f else 1250f
                scrollUnit.value = "[step]"
            }
            DisplayItemLayout(stringResource(R.string.machine_test_motor_speed),
                "${speed.value}", Color(0xFF2A2B2D), unit = "[rpm]") {
                itemSelectIndex.value = MACHINE_TEST_MOTOR_SPEED
                scrollDefaultValue.value = speed.value.toFloat()
                scrollRangeStart.value = 200f
                scrollRangeEnd.value = if (selectMotor.value == MACHINE_TEST_MOTOR_LEFT_TOP ||
                        selectMotor.value == MACHINE_TEST_MOTOR_RIGHT_TOP) 2000f else 800f
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
                    viewModel.saveMotorTestValue(selectMotor.value, itemSelectIndex.value,
                        changeValue)
                }
            }
        }

        MotorItem(
            modifier = Modifier.padding(start = 370.dp, top = 360.dp),
            stringResource(R.string.machine_test_top_piston),
            selectMotor.value == MACHINE_TEST_MOTOR_LEFT_TOP
        ) {
            selectMotor.value = MACHINE_TEST_MOTOR_LEFT_TOP
            itemSelectIndex.value = INVALID_INT
            viewModel.selectMotor(MACHINE_TEST_MOTOR_LEFT_TOP)
        }

        MotorItem(
            modifier = Modifier.padding(start = 370.dp, top = 560.dp),
            stringResource(R.string.machine_test_bottom_piston),
            selectMotor.value == MACHINE_TEST_MOTOR_LEFT_BOTTOM
        ) {
            selectMotor.value = MACHINE_TEST_MOTOR_LEFT_BOTTOM
            itemSelectIndex.value = INVALID_INT
            viewModel.selectMotor(MACHINE_TEST_MOTOR_LEFT_BOTTOM)
        }

        MotorItem(
            modifier = Modifier.padding(start = 730.dp, top = 360.dp),
            stringResource(R.string.machine_test_top_piston),
            selectMotor.value == MACHINE_TEST_MOTOR_RIGHT_TOP
        ) {
            selectMotor.value = MACHINE_TEST_MOTOR_RIGHT_TOP
            itemSelectIndex.value = INVALID_INT
            viewModel.selectMotor(MACHINE_TEST_MOTOR_RIGHT_TOP)
        }

        MotorItem(
            modifier = Modifier.padding(start = 730.dp, top = 560.dp),
            stringResource(R.string.machine_test_bottom_piston),
            selectMotor.value == MACHINE_TEST_MOTOR_RIGHT_BOTTOM
        ) {
            selectMotor.value = MACHINE_TEST_MOTOR_RIGHT_BOTTOM
            itemSelectIndex.value = INVALID_INT
            viewModel.selectMotor(MACHINE_TEST_MOTOR_RIGHT_BOTTOM)
        }

        AdjustButton(
            modifier = Modifier
                .padding(start = 600.dp, top = 420.dp)
                .size(100.dp), {
                Image(
                    painterResource(R.drawable.machine_test_motor_add_ic),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }, {
                viewModel.sendMotorTest(selectMotor.value, true)
            }
        )
        AdjustButton(
            modifier = Modifier
                .padding(start = 600.dp, top = 560.dp)
                .size(100.dp), {
                Image(
                    painterResource(R.drawable.machine_test_motor_minus_ic),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }, {
                viewModel.sendMotorTest(selectMotor.value, false)
            }
        )
    }
}

@Composable
private fun MotorItem(
    modifier: Modifier = Modifier,
    text: String = "",
    selected: Boolean,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .width(202.dp)
            .height(182.dp)
            .debouncedClickable({ onClick() })
    ) {
        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.machine_test_motor_bg),
                contentDescription = null
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.machine_test_motor_normal_bg),
                contentDescription = null,
                modifier = Modifier
                    .width(180.dp)
                    .height(160.dp)
                    .align(Alignment.Center)
            )
        }
        Image(
            painter = painterResource(R.drawable.machine_test_motor_ic),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
                .width(40.dp)
                .height(60.dp)
        )
        Text(
            text = text, color = Color.White, fontSize = 5.nsp(), fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 125.dp)
        )
    }
}


@Composable
private fun AdjustButton(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit = {},
    pressedColor: Color = Color(0xFF00DE93),
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressed by interactionSource.collectIsPressedAsState()
    val boarderColor = if (pressed) {
        pressedColor
    } else {
        Color(0xFF484848)
    }

    Button(
        modifier = modifier,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF191A1D)),
        border = BorderStroke(2.dp, boarderColor),
        shape = RoundedCornerShape(20.dp),
        onClick = composeClick {
            onClick()
        },
    ) {
        content()
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMotorTest() {
    MachineTestMotorLayout()
//    MotorItem("Top Piston", true)
}