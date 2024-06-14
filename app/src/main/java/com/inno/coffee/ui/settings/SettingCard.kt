package com.inno.coffee.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inno.coffee.R
import com.inno.coffee.ui.settings.formula.FormulaPage
import com.inno.coffee.ui.settings.permissions.PermissionPage
import com.inno.coffee.ui.settings.statistics.DrinksHistoryList

const val HOME = "home"
const val STATISTICS = "statistic"
const val FORMULA = "formula"
const val DISPLAY = "display"
const val MACHINE_SETTING = "machine_setting"
const val MACHINE_OPERATION = "machine_operation"
const val VAT_AND_GRIND = "vat_and_grind"
const val WASH_MACHINE = "wash_machine"
const val PERMISSION = "permission"
const val MAINTENANCE = "maintenance"

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
            Surface {
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
            DetailScreen(item = item)
        }
    }

}

@Composable
fun CardItem(onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.coffee6),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DetailScreen(item: String?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text(text = "Detail of $item", modifier = Modifier.padding(16.dp))
        when (item) {
            STATISTICS -> DrinksHistoryList()
            FORMULA -> FormulaPage()
            PERMISSION -> PermissionPage()
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewCardItem() {
    SettingCardLayout()
}