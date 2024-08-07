package com.inno.coffee.ui.settings.statistics.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R
import com.inno.common.utils.Logger


private const val PRODUCT = "product"
private const val WASH_MACHINE = "wash_machine"
private const val RINSE = "rinse"
private const val FAULT = "fault"
private const val MACHINE_HISTORY = "machine_history"

@Composable
fun StatisticsMainScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBarMenu(navController) },
        containerColor = Color.White
    ) {
        Surface(modifier = Modifier.padding(it), color = Color.White) {
            NavHost(navController = navController, startDestination = PRODUCT) {
                composable(PRODUCT) { ProductScreen(navController) }
                composable(WASH_MACHINE) { WashMachineScreen(navController) }
                composable(RINSE) { RinseScreen(navController) }
                composable(FAULT) { FaultScreen(navController) }
                composable(MACHINE_HISTORY) { MachineChangeScreen(navController) }
            }
        }
    }
}

@Composable
private fun TopBarMenu(navController: NavHostController) {
    val context = LocalContext.current
    val menuItems = listOf(
        context.getString(R.string.statistic_product_history),
        context.getString(R.string.statistic_machine_service_history),
        context.getString(R.string.statistic_clean_history),
        context.getString(R.string.statistic_rinse_history),
        context.getString(R.string.statistic_error_history),
    )
    var selectedMenuItem by remember { mutableStateOf(menuItems[0]) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        menuItems.forEach { item ->
            TextButton(
                onClick = {
                    selectedMenuItem = item
                    val route = when (item) {
                        context.getString(R.string.statistic_product_history) -> PRODUCT
                        context.getString(
                            R.string.statistic_machine_service_history) -> MACHINE_HISTORY
                        context.getString(R.string.statistic_clean_history) -> WASH_MACHINE
                        context.getString(R.string.statistic_rinse_history) -> RINSE
                        context.getString(R.string.statistic_error_history) -> FAULT
                        else -> PRODUCT
                    }
                    navController.navigate(route) {
                        popUpTo(PRODUCT) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (selectedMenuItem == item) Color(
                        0XFFBCECE6) else Color.Transparent,
                    contentColor = Color.Black
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = item)
            }
        }
    }
}

@Composable
private fun ProductScreen(navController: NavHostController) {
    Logger.d("ProductScreen")
    ProductStatistic()
}

@Composable
private fun WashMachineScreen(navController: NavHostController) {
    Logger.d("WashMachineScreen")
    BackHandler {
        navController.popBackStack()
    }
    WashMachineStatistic()
}

@Composable
private fun RinseScreen(navController: NavHostController) {
    Logger.d("RinseScreen")
    BackHandler {
        navController.popBackStack()
    }
    RinseStatistic()
}

@Composable
private fun FaultScreen(navController: NavHostController) {
    Logger.d("FaultScreen")
    BackHandler {
        navController.popBackStack()
    }
    FaultStatistic()
}

@Composable
private fun MachineChangeScreen(navController: NavHostController) {
    Logger.d("MachineHistoryScreen")
    BackHandler {
        navController.popBackStack()
    }
    MachineChangeStatistic()
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewMainScreen() {
    StatisticsMainScreen()
}