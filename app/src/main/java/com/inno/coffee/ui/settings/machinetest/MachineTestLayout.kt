package com.inno.coffee.ui.settings.machinetest

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.machinetest.serialtest.SerialPortActivity
import com.inno.coffee.utilities.MACHINE_TEST_KEY_ACTIVITY
import com.inno.coffee.utilities.MACHINE_TEST_KEY_COFFEE_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_KEY_COFFEE_OUTPUTS
import com.inno.coffee.utilities.MACHINE_TEST_KEY_MILK_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_KEY_MILK_OUTPUTS
import com.inno.coffee.utilities.MACHINE_TEST_KEY_MOTOR_TEST
import com.inno.coffee.utilities.MACHINE_TEST_KEY_SERIAL_TEST
import com.inno.coffee.utilities.MACHINE_TEST_KEY_STEAM_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_KEY_STEAM_OUTPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_COFFEE_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_COFFEE_OUTPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_MILK_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_MILK_OUTPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_MOTOR_TEST
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_STEAM_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_STEAM_OUTPUTS
import com.inno.coffee.utilities.nsp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MachineTestLayout(
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val items = arrayOf(
        Pair(MACHINE_TEST_KEY_COFFEE_INPUTS, R.string.machine_test_coffee_inputs),
        Pair(MACHINE_TEST_KEY_COFFEE_OUTPUTS, R.string.machine_test_coffee_output),
        Pair(MACHINE_TEST_KEY_STEAM_INPUTS, R.string.machine_test_steam_inputs),
        Pair(MACHINE_TEST_KEY_STEAM_OUTPUTS, R.string.machine_test_steam_output),
        Pair(MACHINE_TEST_KEY_MILK_INPUTS, R.string.machine_test_milk_inputs),
        Pair(MACHINE_TEST_KEY_MILK_OUTPUTS, R.string.machine_test_milk_output),
        Pair(MACHINE_TEST_KEY_MOTOR_TEST, R.string.machine_test_motor_test),
        Pair(MACHINE_TEST_KEY_SERIAL_TEST, R.string.common_serial_test),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.common_machine_test),
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

        FlowRow(
            modifier = Modifier
                .padding(start = 50.dp, top = 221.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 8,
        ) {
            items.forEach { name ->
                MachineTestButton(title = name.second) {
                    jump(name.first, context)
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }

}

private fun jump(index: Int, context: Context) {
    when (index) {
        MACHINE_TEST_KEY_COFFEE_INPUTS -> {
            ScreenDisplayManager.autoRoute(context, MachineTestInputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_COFFEE_INPUTS)
                })
        }
        MACHINE_TEST_KEY_COFFEE_OUTPUTS -> {
            ScreenDisplayManager.autoRoute(context, MachineTestOutputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_COFFEE_OUTPUTS)
                })
        }
        MACHINE_TEST_KEY_STEAM_INPUTS -> {
            ScreenDisplayManager.autoRoute(context, MachineTestInputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_STEAM_INPUTS)
                })
        }
        MACHINE_TEST_KEY_STEAM_OUTPUTS -> {
            ScreenDisplayManager.autoRoute(context, MachineTestOutputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_STEAM_OUTPUTS)
                })
        }
        MACHINE_TEST_KEY_MILK_INPUTS -> {
            ScreenDisplayManager.autoRoute(context, MachineTestInputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_MILK_INPUTS)
                })
        }
        MACHINE_TEST_KEY_MILK_OUTPUTS -> {
            ScreenDisplayManager.autoRoute(context, MachineTestOutputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_MILK_OUTPUTS)
                })
        }
        MACHINE_TEST_KEY_MOTOR_TEST -> {
            ScreenDisplayManager.autoRoute(context, MachineTestOutputsActivity::class.java,
                Bundle().apply {
                    putString(MACHINE_TEST_KEY_ACTIVITY, MACHINE_TEST_VALUE_MOTOR_TEST)
                })
        }
        MACHINE_TEST_KEY_SERIAL_TEST -> {
            ScreenDisplayManager.autoRoute(context, SerialPortActivity::class.java)
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineTest() {
    MachineTestLayout()
}