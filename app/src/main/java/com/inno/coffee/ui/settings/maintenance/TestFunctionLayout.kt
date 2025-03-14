package com.inno.coffee.ui.settings.maintenance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.maintenance.TestFunctionViewModel
import com.inno.serialport.utilities.MAINTENANCE_CLEANING_BALL_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_FLOW_RATE_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_GRINDER_SENSOR_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_MILK_SENSOR_LEFT_TEST_ID
import com.inno.serialport.utilities.MAINTENANCE_MILK_SENSOR_RIGHT_TEST_ID

@Composable
fun TestFunctionLayout(
    viewModel: TestFunctionViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    var itemSelectIndex by remember { mutableIntStateOf(INVALID_INT) }
    var titleValue by remember { mutableStateOf("") }
    val grinderLeft by viewModel.grinderLeftResult.collectAsState()
    val grinderRight by viewModel.grinderRightResult.collectAsState()
    val flowRateLeft by viewModel.flowRateLeftResult.collectAsState()
    val flowRateRight by viewModel.flowRateRightResult.collectAsState()
    val status by viewModel.status.collectAsState()
    val min by viewModel.min.collectAsState()
    val max by viewModel.max.collectAsState()
    val average by viewModel.average.collectAsState()
    val offset by viewModel.offset.collectAsState()

    val milkLeftString = stringResource(R.string.maintenance_milk_sensor_left_test)
    val milkRightString = stringResource(R.string.maintenance_milk_sensor_right_test)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.maintenance_test_functions),
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

        Row(
            modifier = Modifier.padding(start = 160.dp, top = 260.dp)
        ) {
            Column(
                modifier = Modifier.width(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.config_steam_wand_left), color = Color.White,
                    fontSize = 5.nsp())
                Text(text = "$grinderLeft", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 20.dp))
                Text(text = "$flowRateLeft", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 65.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.padding(top = 35.dp)
            ) {
                ChangeColorButton(
                    modifier = Modifier
                        .width(280.dp)
                        .height(73.dp),
                    text = stringResource(R.string.maintenance_grinder_test)
                ) {
                    viewModel.sendTestCommand(MAINTENANCE_GRINDER_SENSOR_TEST_ID)
                }
                Spacer(modifier = Modifier.height(40.dp))
                ChangeColorButton(
                    modifier = Modifier
                        .width(280.dp)
                        .height(73.dp),
                    text = stringResource(R.string.maintenance_flow_rate_test)
                ) {
                    viewModel.sendTestCommand(MAINTENANCE_FLOW_RATE_TEST_ID)
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.width(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.config_steam_wand_right), color = Color.White,
                    fontSize = 5.nsp())
                Text(text = "$grinderRight", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 20.dp))
                Text(text = "$flowRateRight", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 65.dp))
            }
        }
        Column(
            modifier = Modifier.padding(start = 750.dp, top = 295.dp)
        ) {
            ChangeColorButton(modifier = Modifier
                .width(280.dp)
                .height(73.dp),
                milkLeftString
            ) {
                itemSelectIndex = 1
                titleValue = milkLeftString
                viewModel.sendTestCommand(MAINTENANCE_MILK_SENSOR_LEFT_TEST_ID)
            }
            Spacer(modifier = Modifier.height(40.dp))
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                milkRightString
            ) {
                itemSelectIndex = 2
                titleValue = milkRightString
                viewModel.sendTestCommand(MAINTENANCE_MILK_SENSOR_RIGHT_TEST_ID)
            }
            Spacer(modifier = Modifier.height(40.dp))
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_ball_dispenser_test)
            ) {
                viewModel.sendTestCommand(MAINTENANCE_CLEANING_BALL_TEST_ID)
            }
        }

        if (itemSelectIndex != INVALID_INT) {
            TestResultLayout(title = titleValue, min = min, max = max, average = average,
                offset = offset) {
                itemSelectIndex = INVALID_INT
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewTestFunction() {
    TestFunctionLayout()
}