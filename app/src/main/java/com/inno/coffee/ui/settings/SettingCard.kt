package com.inno.coffee.ui.settings

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.inno.common.annotation.DISPLAY
import com.inno.common.annotation.FORMULA
import com.inno.common.annotation.HOME
import com.inno.common.annotation.MACHINE_OPERATION
import com.inno.common.annotation.MACHINE_SETTING
import com.inno.common.annotation.MAINTENANCE
import com.inno.common.annotation.PERMISSION
import com.inno.common.annotation.SERIAL_TEST
import com.inno.common.annotation.STATISTIC
import com.inno.common.annotation.VAT_AND_GRIND
import com.inno.common.annotation.WASH_MACHINE


@Composable
fun SettingCardLayout(
    modifier: Modifier = Modifier
) {
    val names = arrayOf(
        Pair(STATISTIC, R.string.common_statistic), Pair(FORMULA, R.string.common_formula),
        Pair(DISPLAY, R.string.common_display),
        Pair(MACHINE_SETTING, R.string.common_machine_setting),
        Pair(MACHINE_OPERATION, R.string.common_machine_operation),
        Pair(VAT_AND_GRIND, R.string.common_vat_and_grind),
        Pair(WASH_MACHINE, R.string.common_wash_machine),
        Pair(PERMISSION, R.string.common_permission),
        Pair(MAINTENANCE, R.string.common_maintenance),
        Pair(SERIAL_TEST, R.string.common_serial_test)
    )
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
                    itemsIndexed(names) { _, item ->
                        val title = stringResource(id = item.second)
                        CardItem(title) {
                            navController.navigate("detail/${item.first}")
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
            STATISTIC -> StatisticsMainScreen(navHostController)
            FORMULA -> FormulaPage()
            PERMISSION -> PermissionPage()
            MACHINE_SETTING -> {}
            SERIAL_TEST -> SerialTest()
            else -> {}
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewCardItem() {
    SettingCardLayout()
}