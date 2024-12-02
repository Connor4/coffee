package com.inno.coffee.ui.settings.machinetest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.inno.coffee.ui.common.TextWithValue
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestInputViewModel

@Composable
fun MachineTestMilkInputLayout(
    viewModel: MachineTestInputViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {

    val okColor = Color(0xFF6DD400)
    val failColor = Color.Red
    val present = stringResource(R.string.machine_test_present)
    val missing = stringResource(R.string.machine_test_missing)

    val leftMilkTemp = viewModel.milkTempLeft.collectAsState()
    val rightMilkTemp = viewModel.milkTempRight.collectAsState()
    val leftMilkTankTemp = viewModel.milkTankTempLeft.collectAsState()
    val rightMilkTankTemp = viewModel.milkTankTempRight.collectAsState()
    val leftMilkSensor = viewModel.milkSensorLeft.collectAsState()
    val rightMilkSensor = viewModel.milkSensorRight.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMilkInputs()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.machine_test_input_test_milk),
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
        Box(
            modifier = Modifier
                .padding(top = 180.dp, start = 55.dp)
                .width(1153.dp)
                .height(166.dp)
                .background(Color(0xFF191A1D))
        ) {
            TextWithValue(
                stringResource(R.string.machine_test_milk_temperature_left),
                value = leftMilkTemp.value,
                modifier = Modifier.padding(start = 30.dp, top = 20.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_milk_temperature_right),
                value = rightMilkTemp.value,
                modifier = Modifier.padding(start = 600.dp, top = 20.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_milk_tank_temperature_left),
                value = leftMilkTankTemp.value,
                modifier = Modifier.padding(start = 30.dp, top = 55.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_milk_tank_temperature_right),
                value = rightMilkTankTemp.value,
                modifier = Modifier.padding(start = 600.dp, top = 55.dp)
            )

            TextWithValue(
                stringResource(R.string.machine_test_milk_detect_sensor_left),
                if (leftMilkSensor.value) present else missing,
                textColor = if (leftMilkSensor.value) okColor else failColor,
                modifier = Modifier.padding(start = 30.dp, top = 120.dp)
            )
            TextWithValue(
                stringResource(R.string.machine_test_milk_detect_sensor_right),
                if (rightMilkSensor.value) present else missing,
                textColor = if (rightMilkSensor.value) okColor else failColor,
                modifier = Modifier.padding(start = 600.dp, top = 120.dp)
            )
        }

    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineTestMilkInput() {
    MachineTestMilkInputLayout()
}