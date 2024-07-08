package com.inno.coffee.ui.settings

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R
import com.inno.coffee.ui.serialtest.SerialTest
import com.inno.coffee.ui.settings.formula.FormulaPage
import com.inno.coffee.ui.settings.permissions.PermissionPage
import com.inno.coffee.ui.settings.statistics.StatisticsMainScreen

private const val HOME = "home"
private const val STATISTICS = "statistic"
private const val FORMULA = "formula"
private const val DISPLAY = "display"
private const val MACHINE_SETTING = "machine_setting"
private const val MACHINE_OPERATION = "machine_operation"
private const val VAT_AND_GRIND = "vat_and_grind"
private const val WASH_MACHINE = "wash_machine"
private const val PERMISSION = "permission"
private const val MAINTENANCE = "maintenance"
private const val SERIAL_TEST = "serial_test"

private val items =
    mutableListOf(STATISTICS, FORMULA, DISPLAY, MACHINE_SETTING, MACHINE_OPERATION, VAT_AND_GRIND,
        WASH_MACHINE, PERMISSION, MAINTENANCE, SERIAL_TEST)

@Composable
fun SettingCardLayout(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        composable(HOME) {
            Surface(color = Color.Transparent) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ) {
                    itemsIndexed(items) { index, _ ->
                        val title = findTitle(index, context)
                        CardItem(title) {
                            navController.navigate("detail/$it")
                        }
                    }
                }
            }
        }
        composable("detail/{item}") {
            val item = it.arguments?.getString("item")
            DetailScreen(item, navController, modifier)
        }
    }

}

@Composable
fun CardItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(300.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Text(text = title, style = MaterialTheme.typography.displaySmall, modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun DetailScreen(item: String?, navHostController: NavHostController, modifier: Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        when (item) {
            STATISTICS -> StatisticsMainScreen(navHostController)
            FORMULA -> FormulaPage()
            PERMISSION -> PermissionPage()
            SERIAL_TEST -> SerialTest()
            else -> {}
        }
    }
}

private fun findTitle(index: Int, context: Context): String {
    when (index) {
        0 -> {
            context.getString(R.string.common_statistic)
        }
        1 -> {
            context.getString(R.string.common_formula)
        }
        2 -> {
            context.getString(R.string.common_display)
        }
        3 -> {
            context.getString(R.string.common_machine_setting)
        }
        4 -> {
            context.getString(R.string.common_machine_operation)
        }
        5 -> {
            context.getString(R.string.common_vat_and_grind)
        }
        6 -> {
            context.getString(R.string.common_wash_machine)
        }
        7 -> {
            context.getString(R.string.common_permission)
        }
        8 -> {
            context.getString(R.string.common_maintenance)
        }
        9 -> {
            context.getString(R.string.common_serial_test)
        }
    }
    return ""
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewCardItem() {
    SettingCardLayout()
}