package com.inno.coffee.ui.settings.statistics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.settings.statistics.history.StatisticHistoryActivity
import com.inno.coffee.ui.settings.statistics.machine.StatisticMachineActivity
import com.inno.coffee.ui.settings.statistics.product.StatisticProductActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.debouncedClickable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticActivity : CoffeeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
//                StatisticMain()
                StatisticLayout() {
                    this@StatisticActivity.finish()
                }
            }
        }
    }
}

@Composable
fun StatisticMain() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally)
    ) {
        StatisticCardItem(text = stringResource(id = R.string.statistic_main_product_counter)) {
            ScreenDisplayManager.autoRoute(context, StatisticProductActivity::class.java)
        }
        StatisticCardItem(text = stringResource(id = R.string.statistic_main_machine_counter)) {
            ScreenDisplayManager.autoRoute(context, StatisticMachineActivity::class.java)
        }
        StatisticCardItem(text = stringResource(id = R.string.statistic_main_history_data)) {
            ScreenDisplayManager.autoRoute(context, StatisticHistoryActivity::class.java)
        }
    }
}

@Composable
fun StatisticCardItem(text: String, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(250.dp)
            .debouncedClickable({ onItemClick() }),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(180.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = text, style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewStatisticMain() {
    StatisticMain()
}