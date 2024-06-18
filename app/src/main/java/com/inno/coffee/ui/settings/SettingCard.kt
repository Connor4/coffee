package com.inno.coffee.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.inno.coffee.R
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

private val items =
    mutableListOf(STATISTICS, FORMULA, DISPLAY, MACHINE_SETTING, MACHINE_OPERATION, VAT_AND_GRIND,
        WASH_MACHINE, PERMISSION, MAINTENANCE)

@Composable
fun SettingCardLayout(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        composable(HOME) {
            Surface(color = Color.Transparent) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier.fillMaxSize()
                ) {
                    items(items) {
                        CardItem {
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
fun CardItem(onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        AsyncImage(
            model = R.drawable.coffee6,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DetailScreen(item: String?, navHostController: NavHostController, modifier: Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        when (item) {
            STATISTICS -> StatisticsMainScreen(navHostController)
            FORMULA -> FormulaPage()
            PERMISSION -> PermissionPage()
            else -> StatisticsMainScreen(navHostController)
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewCardItem() {
    SettingCardLayout()
}