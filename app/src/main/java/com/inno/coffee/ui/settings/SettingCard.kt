package com.inno.coffee.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.presentation.ScreenDisplayManager
import com.inno.coffee.ui.settings.formula.FormulaActivity
import com.inno.coffee.ui.settings.permissions.PermissionActivity
import com.inno.coffee.ui.settings.serialtest.SerialPortActivity
import com.inno.coffee.ui.settings.statistics.StatisticActivity
import com.inno.coffee.utilities.fastclick
import com.inno.common.annotations.DISPLAY
import com.inno.common.annotations.FORMULA
import com.inno.common.annotations.MACHINE_OPERATION
import com.inno.common.annotations.MACHINE_SETTING
import com.inno.common.annotations.MAINTENANCE
import com.inno.common.annotations.PERMISSION
import com.inno.common.annotations.SERIAL_TEST
import com.inno.common.annotations.STATISTIC
import com.inno.common.annotations.VAT_AND_GRIND
import com.inno.common.annotations.WASH_MACHINE


@Composable
fun SettingCardLayout(
    modifier: Modifier = Modifier
) {
    val names = arrayOf(
        Pair(STATISTIC, R.string.common_statistic), Pair(FORMULA, R.string.common_formula),
        Pair(DISPLAY, R.string.common_display),
        Pair(MACHINE_SETTING, R.string.common_machine_config),
        Pair(MACHINE_OPERATION, R.string.common_machine_params),
        Pair(VAT_AND_GRIND, R.string.common_vat_and_grind),
        Pair(WASH_MACHINE, R.string.common_machine_clean),
        Pair(PERMISSION, R.string.common_permission),
        Pair(MAINTENANCE, R.string.common_maintenance),
        Pair(SERIAL_TEST, R.string.common_serial_test)
    )
    val context = LocalContext.current
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
            items(names) { name ->
                val title = stringResource(id = name.second)
                CardItem(title = title) {
                    jumpDetail(name.first, context)
                }
            }
        }
    }

}

@Composable
fun CardItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(300.dp)
            .fastclick { onClick() },
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
        VAT_AND_GRIND -> {
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
fun PreviewCardItem() {
    SettingCardLayout()
}