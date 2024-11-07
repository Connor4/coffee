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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.bean.BeanGrinderActivity
import com.inno.coffee.ui.settings.config.MachineConfigActivity
import com.inno.coffee.ui.settings.display.DisplayActivity
import com.inno.coffee.ui.settings.formula.FormulaActivity
import com.inno.coffee.ui.settings.params.MachineParamsActivity
import com.inno.coffee.ui.settings.permissions.PermissionActivity
import com.inno.coffee.ui.settings.serialtest.SerialPortActivity
import com.inno.coffee.ui.settings.statistics.StatisticActivity
import com.inno.coffee.utilities.nsp
import com.inno.common.annotations.BEANS_AND_GRINDER
import com.inno.common.annotations.DISPLAY
import com.inno.common.annotations.FORMULA
import com.inno.common.annotations.INTERFACE
import com.inno.common.annotations.MACHINE_INFO
import com.inno.common.annotations.MACHINE_OPERATION
import com.inno.common.annotations.MACHINE_SETTING
import com.inno.common.annotations.MACHINE_TEST
import com.inno.common.annotations.MAINTENANCE
import com.inno.common.annotations.MANAGER
import com.inno.common.annotations.OPERATOR
import com.inno.common.annotations.PERMISSION
import com.inno.common.annotations.STATISTIC
import com.inno.common.annotations.TECHNICIAN
import com.inno.common.annotations.WASH_MACHINE
import com.inno.common.utils.UserSessionManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MachineSettingLayout(
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val level1 = arrayOf(
        Pair(STATISTIC, R.string.common_statistic),
        Pair(MAINTENANCE, R.string.common_maintenance),
    )
    val level2 = arrayOf(
        Pair(STATISTIC, R.string.common_statistic),
        Pair(FORMULA, R.string.common_formula),
        Pair(DISPLAY, R.string.common_display),
        Pair(BEANS_AND_GRINDER, R.string.common_beans_and_grinder),
        Pair(WASH_MACHINE, R.string.common_machine_clean),
        Pair(PERMISSION, R.string.common_password),
    )
    val level3 = arrayOf(
        Pair(STATISTIC, R.string.common_statistic),
        Pair(FORMULA, R.string.common_formula),
        Pair(DISPLAY, R.string.common_display),
        Pair(MACHINE_SETTING, R.string.common_machine_config),
        Pair(MACHINE_OPERATION, R.string.common_machine_params),
        Pair(BEANS_AND_GRINDER, R.string.common_beans_and_grinder),
        Pair(WASH_MACHINE, R.string.common_machine_clean),
        Pair(PERMISSION, R.string.common_password),
        Pair(INTERFACE, R.string.common_interface),
        Pair(MAINTENANCE, R.string.common_maintenance),
        Pair(MACHINE_INFO, R.string.common_machine_info),
        Pair(MACHINE_TEST, R.string.common_machine_test),
    )
    val modules = when (UserSessionManager.getUser()?.role) {
        OPERATOR -> level1
        MANAGER -> level2
        TECHNICIAN -> level3
        else -> level3
    }

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
            modules.forEach { module ->
                MenuItem(title = module.second) {
                    jumpDetail(module.first, context)
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
            ScreenDisplayManager.autoRoute(context, DisplayActivity::class.java)
        }
        MACHINE_SETTING -> {
            ScreenDisplayManager.autoRoute(context, MachineConfigActivity::class.java)
        }
        MACHINE_OPERATION -> {
            ScreenDisplayManager.autoRoute(context, MachineParamsActivity::class.java)
        }
        BEANS_AND_GRINDER -> {
            ScreenDisplayManager.autoRoute(context, BeanGrinderActivity::class.java)
        }
        WASH_MACHINE -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        PERMISSION -> {
            ScreenDisplayManager.autoRoute(context, PermissionActivity::class.java)
        }
        INTERFACE -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        MAINTENANCE -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        MACHINE_INFO -> {
            Toast.makeText(context, "还没做", Toast.LENGTH_SHORT).show()
        }
        MACHINE_TEST -> {
            ScreenDisplayManager.autoRoute(context, SerialPortActivity::class.java)
        }
        else -> {}
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMachineSetting() {
    MachineSettingLayout()
}