package com.inno.coffee.ui.settings.statistics

import android.view.LayoutInflater
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.LegendLayout
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.inno.coffee.R
import com.inno.common.utils.Logger


private const val PRODUCT = "product"
private const val WASH_MACHINE = "wash_machine"
private const val RINSE = "rinse"
private const val FAULT = "fault"
private const val MACHINE_HISTORY = "machine_history"

@Composable
fun StatisticsMainScreen(
    hostNavController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    BackHandler {
        hostNavController.popBackStack()
    }

    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBarMenu(navController) },
        containerColor = Color.White
    ) {
        Surface(modifier = Modifier.padding(it), color = Color.White) {
            NavHost(navController = navController, startDestination = PRODUCT) {
                composable(PRODUCT) { ProductScreen(hostNavController) }
                composable(WASH_MACHINE) { WashMachineScreen(hostNavController) }
                composable(RINSE) { RinseScreen(hostNavController) }
                composable(FAULT) { FaultScreen(hostNavController) }
                composable(MACHINE_HISTORY) { MachineHistoryScreen(hostNavController) }
            }
        }
    }
}

@Composable
fun TopBarMenu(navController: NavHostController) {
    val context = LocalContext.current
    val menuItems = listOf(
        context.getString(R.string.statistic_product),
        context.getString(R.string.statistic_machine_history),
        context.getString(R.string.statistic_wash_machine),
        context.getString(R.string.statistic_rinse),
        context.getString(R.string.statistic_fault),
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
                        context.getString(R.string.statistic_product) -> PRODUCT
                        context.getString(R.string.statistic_machine_history) -> MACHINE_HISTORY
                        context.getString(R.string.statistic_wash_machine) -> WASH_MACHINE
                        context.getString(R.string.statistic_rinse) -> RINSE
                        context.getString(R.string.statistic_fault) -> FAULT
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
fun ProductScreen(navController: NavHostController) {
    Logger.d("ProductScreen")
    BackHandler {
        navController.popBackStack()
    }
    ProductStatistic()
}

@Composable
fun WashMachineScreen(navController: NavHostController) {
    Logger.d("WashMachineScreen")
    BackHandler {
        navController.popBackStack()
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {

        AndroidView(
            factory = {
                LayoutInflater.from(it).inflate(R.layout.statistic_product_layout, null)
            }, update = {
                val chartView = it.findViewById<AnyChartView>(R.id.any_chart_view1)
                val pie = AnyChart.pie()
                val data = mutableListOf<DataEntry>()
                data.add(ValueDataEntry("apple", 1))
                data.add(ValueDataEntry("pear", 2))
                data.add(ValueDataEntry("bananas", 3))
                data.add(ValueDataEntry("grapes", 4))
                data.add(ValueDataEntry("oranges", 5))
                pie.data(data)

                pie.title("Fruits imported in 2015 (in kg)")

                pie.labels().position("outside")

                pie.legend().title().enabled(true)
                pie.legend().title()
                    .text("Retail channels")
                    .padding(0.0, 0.0, 10.0, 0.0)

                pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER)
                    .align(Align.CENTER)
                chartView.setChart(pie)
                // ==============================================================
                val anyChartView: AnyChartView = it.findViewById(R.id.any_chart_view2)
                val cartesian = AnyChart.column()

                val data2: MutableList<DataEntry> = ArrayList()
                data2.add(ValueDataEntry("Rouge", 80540))
                data2.add(ValueDataEntry("Foundation", 94190))
                data2.add(ValueDataEntry("Mascara", 102610))
                data2.add(ValueDataEntry("Lip gloss", 110430))
                data2.add(ValueDataEntry("Lipstick", 128000))
                data2.add(ValueDataEntry("Nail polish", 143760))
                data2.add(ValueDataEntry("Eyebrow pencil", 170670))
                data2.add(ValueDataEntry("Eyeliner", 213210))
                data2.add(ValueDataEntry("Eyeshadows", 249980))

                val column = cartesian.column(data2)

                column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0.0)
                    .offsetY(5.0)
                    .format("\${%Value}{groupsSeparator: }")

                cartesian.animation(true)
                cartesian.title("Top 10 Cosmetic Products by Revenue")

                cartesian.yScale().minimum(0.0)

                cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
                cartesian.interactivity().hoverMode(HoverMode.BY_X)

                cartesian.xAxis(0).title("Product")
                cartesian.yAxis(0).title("Revenue")

                anyChartView.setChart(cartesian)
            }
        )
    }
}

@Composable
fun RinseScreen(navController: NavHostController) {
    Logger.d("RinseScreen")
    BackHandler {
        navController.popBackStack()
    }
}

@Composable
fun FaultScreen(navController: NavHostController) {
    Logger.d("FaultScreen")
    BackHandler {
        navController.popBackStack()
    }
}

@Composable
fun MachineHistoryScreen(navController: NavHostController) {
    Logger.d("MachineHistoryScreen")
    BackHandler {
        navController.popBackStack()
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewMainScreen() {
    StatisticsMainScreen()
}