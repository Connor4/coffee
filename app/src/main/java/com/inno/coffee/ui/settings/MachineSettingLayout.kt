package com.inno.coffee.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.formula.FormulaActivity
import com.inno.coffee.ui.settings.permissions.PermissionActivity
import com.inno.coffee.ui.settings.serialtest.SerialPortActivity
import com.inno.coffee.ui.settings.statistics.StatisticActivity
import com.inno.coffee.utilities.nsp
import com.inno.common.annotations.BEANS_AND_GRINDER
import com.inno.common.annotations.DISPLAY
import com.inno.common.annotations.FORMULA
import com.inno.common.annotations.MACHINE_OPERATION
import com.inno.common.annotations.MACHINE_SETTING
import com.inno.common.annotations.MAINTENANCE
import com.inno.common.annotations.PERMISSION
import com.inno.common.annotations.SERIAL_TEST
import com.inno.common.annotations.STATISTIC
import com.inno.common.annotations.WASH_MACHINE

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MachineSettingLayout(
    onCloseClick: () -> Unit = {},
) {
    val names = arrayOf(
        Pair(STATISTIC, R.string.common_statistic),
        Pair(FORMULA, R.string.common_formula),
        Pair(DISPLAY, R.string.common_display),
        Pair(MACHINE_SETTING, R.string.common_machine_config),
        Pair(MACHINE_OPERATION, R.string.common_machine_params),
        Pair(BEANS_AND_GRINDER, R.string.common_beans_and_grinder),
        Pair(WASH_MACHINE, R.string.common_machine_clean),
        Pair(PERMISSION, R.string.common_permission),
        Pair(MAINTENANCE, R.string.common_maintenance),
        Pair(SERIAL_TEST, R.string.common_serial_test)
    )
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
    ) {
        Text(
            text = stringResource(id = R.string.settings_title),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 116.dp, start = 54.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.home_entrance_close_ic),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 116.dp, end = 54.dp)
                .width(40.dp)
                .height(42.dp)
                .fastclick { onCloseClick() },
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 209.dp, end = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 4,
        ) {
            names.forEach { name ->
                MenuItem(title = name.second) {
                    jumpDetail(name.first, context)
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    val boarderColor = if (isPressed) Color(0xFF00DE93) else Color(0xFF484848)

    Box(
        modifier = Modifier
            .width(280.dp)
            .height(120.dp)
            .border(2.dp, boarderColor, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF191A1D))
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onClick()
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                })
            },
    ) {
        Text(
            text = stringResource(id = title),
            fontSize = 5.nsp(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun jumpDetail(name: String, context: Context) {
    when (name) {
        STATISTIC -> {
            ScreenDisplayManager.autoRoute(context, StatisticActivity::class.java)
        }
        FORMULA -> {
            ScreenDisplayManager.autoRoute(context, FormulaActivity::class.java)
        }
        DISPLAY -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        MACHINE_SETTING -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        MACHINE_OPERATION -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        BEANS_AND_GRINDER -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        WASH_MACHINE -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        PERMISSION -> {
            ScreenDisplayManager.autoRoute(context, PermissionActivity::class.java)
        }
        MAINTENANCE -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        SERIAL_TEST -> {
            ScreenDisplayManager.autoRoute(context, SerialPortActivity::class.java)
        }
        else -> {}
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewMachineSetting() {
    MachineSettingLayout()
}