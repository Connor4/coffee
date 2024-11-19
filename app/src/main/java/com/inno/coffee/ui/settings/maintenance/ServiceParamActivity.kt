package com.inno.coffee.ui.settings.maintenance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ConfirmDialogLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.MAINTENANCE_VALUE_CUPS
import com.inno.coffee.utilities.MAINTENANCE_VALUE_SCHEDULE
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.maintenance.ServiceParamViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceParamActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                ServiceParamLayout {
                    finish()
                }
            }
        }
    }

}

@Composable
fun ServiceParamLayout(
    viewModel: ServiceParamViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val openConfirmDialog = remember { mutableStateOf(false) }

    val cups = viewModel.cups.collectAsState()
    val schedule = viewModel.schedule.collectAsState()
    val totalCount = viewModel.totalCount.collectAsState()
    val leftCount = viewModel.leftCount.collectAsState()
    val rightCount = viewModel.rightCount.collectAsState()
    val nextTime = viewModel.maintenanceDate.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.maintenance_service_param),
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 95.dp)
        ) {
            DisplayItemLayout(stringResource(R.string.maintenance_count_cups),
                "${cups.value}", Color(0xFF191A1D), unit = "[#]") {
                itemSelectIndex.value = MAINTENANCE_VALUE_CUPS
                scrollDefaultValue.value = cups.value.toFloat()
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 100000f
                scrollUnit.value = ""
            }
            DisplayItemLayout(stringResource(R.string.maintenance_count_schedule),
                "${schedule.value}", Color(0xFF2A2B2D), unit = "[Month]") {
                itemSelectIndex.value = MAINTENANCE_VALUE_SCHEDULE
                scrollDefaultValue.value = schedule.value.toFloat()
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 12f
                scrollUnit.value = "[Month]"
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 50.dp, top = 348.dp, end = 95.dp)
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF191A1D))
        ) {
            Text(text = stringResource(R.string.maintenance_countdown_point) +
                    " ${totalCount.value} " + stringResource(R.string.maintenance_products),
                fontSize = 7.nsp(), color = Color(0xFF32C5FF),
                modifier = Modifier.padding(start = 30.dp, top = 20.dp)
            )
            Text(
                text = stringResource(R.string.maintenance_left_brew_unit) +
                        " ${leftCount.value} " + stringResource(R.string.maintenance_products),
                fontSize = 6.nsp(), color = Color(0xFF32C5FF),
                modifier = Modifier.padding(start = 30.dp, top = 80.dp)
            )
            Text(
                text = stringResource(R.string.maintenance_right_brew_unit) +
                        " ${rightCount.value} " + stringResource(R.string.maintenance_products),
                fontSize = 6.nsp(), color = Color(0xFF32C5FF),
                modifier = Modifier.padding(start = 460.dp, top = 80.dp)
            )
            Text(
                text = stringResource(R.string.maintenance_next_time) + " ${nextTime.value}",
                fontSize = 7.nsp(), color = Color(0xFF32C5FF),
                modifier = Modifier.padding(start = 30.dp, top = 130.dp)
            )
        }

        if (itemSelectIndex.value != INVALID_INT) {
            key(scrollDefaultValue.value) {
                UnitValueScrollBar(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 172.dp, end = 90.dp)
                        .width(550.dp)
                        .wrapContentHeight(),
                    value = scrollDefaultValue.value,
                    rangeStart = scrollRangeStart.value,
                    rangeEnd = scrollRangeEnd.value,
                    unit = scrollUnit.value,
                ) { changeValue ->
                    viewModel.saveServiceParam(itemSelectIndex.value, changeValue.toInt())
                }
            }
        }

        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 692.dp),
            text = stringResource(R.string.bean_grinder_reset)
        ) {
            openConfirmDialog.value = true
        }

        if (openConfirmDialog.value) {
            ConfirmDialogLayout(
                stringResource(R.string.formula_product_cups_title),
                stringResource(R.string.maintenance_confirm_reset_count), {
                    openConfirmDialog.value = false
//                    viewModel.setFormulaCups(selectedCups.value, selectFormula)
                }, {
                    openConfirmDialog.value = false
                }
            )
        }

    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewServiceParam() {
    ServiceParamLayout()
}