package com.inno.coffee.ui.settings.statistics.history.deprecate

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


private const val PRODUCT = "product"
private const val CLEAN = "clean"
private const val RINSE = "rinse"
private const val ERROR = "error"
private const val SERVICE = "service"

@Composable
fun StatisticsMainScreen(
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBarMenu(navController) },
        containerColor = Color.White
    ) {
        Surface(modifier = Modifier.padding(it), color = Color.White) {
            NavHost(navController = navController, startDestination = PRODUCT) {
                composable(PRODUCT) { ProductStatistic() }
                composable(CLEAN) { CleanMachineStatistic() }
                composable(RINSE) { RinseStatistic() }
                composable(ERROR) { ErrorStatistic() }
                composable(SERVICE) { MaintenanceStatistic() }
            }
        }
    }
}

@Composable
private fun TopBarMenu(navController: NavHostController) {
    val context = LocalContext.current
    val menuItems = listOf(
        context.getString(R.string.statistic_product_history),
        context.getString(R.string.statistic_error_history),
        context.getString(R.string.statistic_clean_history),
        context.getString(R.string.statistic_rinse_history),
        context.getString(R.string.statistic_machine_service_history),
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
                            R.string.statistic_machine_service_history) -> SERVICE
                        context.getString(R.string.statistic_clean_history) -> CLEAN
                        context.getString(R.string.statistic_rinse_history) -> RINSE
                        context.getString(R.string.statistic_error_history) -> ERROR
                        else -> PRODUCT
                    }
                    navController.navigate(route) {
                        launchSingleTop = true
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


@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewMainScreen() {
}