package com.inno.coffee.ui.settings.maintenance

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.machinetest.MachineTestButton
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.MAINTENANCE_KEY_MANUAL
import com.inno.coffee.utilities.MAINTENANCE_KEY_SAVE_RESTORE
import com.inno.coffee.utilities.MAINTENANCE_KEY_SERVICE_FUNCTIONS
import com.inno.coffee.utilities.MAINTENANCE_KEY_SERVICE_PARAM
import com.inno.coffee.utilities.MAINTENANCE_KEY_TEST_FUNCTIONS
import com.inno.coffee.utilities.MAINTENANCE_KEY_WATER_FILTER
import com.inno.coffee.utilities.nsp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MaintenanceActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                MaintenanceScreen {
                    finish()
                }
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaintenanceScreen(
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val items = arrayOf(
        Pair(MAINTENANCE_KEY_SERVICE_PARAM, R.string.maintenance_service_param),
        Pair(MAINTENANCE_KEY_WATER_FILTER, R.string.maintenance_water_filter),
        Pair(MAINTENANCE_KEY_SERVICE_FUNCTIONS, R.string.maintenance_service_functions),
        Pair(MAINTENANCE_KEY_TEST_FUNCTIONS, R.string.maintenance_test_functions),
        Pair(MAINTENANCE_KEY_SAVE_RESTORE, R.string.maintenance_save_restore),
        Pair(MAINTENANCE_KEY_MANUAL, R.string.maintenance_manuals),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.info_steam_status),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )

        FlowRow(
            modifier = Modifier
                .padding(start = 50.dp, top = 221.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 8,
        ) {
            items.forEach { name ->
                MachineTestButton(title = name.second) {
                    jump(name.first, context)
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

private fun jump(index: Int, context: Context) {
    when (index) {
        MAINTENANCE_KEY_SERVICE_PARAM -> {
            ScreenDisplayManager.autoRoute(context, ServiceParamActivity::class.java)
        }
        MAINTENANCE_KEY_WATER_FILTER -> {
            ScreenDisplayManager.autoRoute(context, FilterGuideActivity::class.java)
        }
        MAINTENANCE_KEY_SERVICE_FUNCTIONS -> {
            ScreenDisplayManager.autoRoute(context, ServiceFunctionActivity::class.java)
        }
        MAINTENANCE_KEY_TEST_FUNCTIONS -> {
        }
        MAINTENANCE_KEY_SAVE_RESTORE -> {
        }
        MAINTENANCE_KEY_MANUAL -> {
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMaintenanceScreen() {
    MaintenanceScreen()
}