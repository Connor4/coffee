package com.inno.coffee.ui.settings.bean

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.ACCURACY_3
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.SingleInputLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.BEAN_KEY_INDEX_ETC_FRONT
import com.inno.coffee.utilities.BEAN_KEY_INDEX_ETC_REAR
import com.inno.coffee.utilities.BEAN_KEY_INDEX_FRONT_HOPPER
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_FRONT
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_REAR
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDING_CAPACITY_FRONT
import com.inno.coffee.utilities.BEAN_KEY_INDEX_GRINDING_CAPACITY_REAR
import com.inno.coffee.utilities.BEAN_KEY_INDEX_LEVELLING
import com.inno.coffee.utilities.BEAN_KEY_INDEX_PQC
import com.inno.coffee.utilities.BEAN_KEY_INDEX_REAR_HOPPER
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.bean.BeanGrinderViewModel

@Composable
fun BeanGrinderLayout(
    viewModel: BeanGrinderViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val scrollRangeStart = remember { mutableStateOf(0f) }
    val scrollRangeEnd = remember { mutableStateOf(0f) }
    val scrollUnit = remember { mutableStateOf("") }
    val scrollAccuracy = remember { mutableStateOf(1) }
    val titleValue = remember { mutableStateOf("") }

    val pqc = viewModel.pqc.collectAsState()
    val etcRear = viewModel.etcRear.collectAsState()
    val etcFront = viewModel.etcFront.collectAsState()
    val levelling = viewModel.levelling.collectAsState()
    val grindingCapacityRear = viewModel.grindingCapacityRear.collectAsState()
    val grindingCapacityFront = viewModel.grindingCapacityFront.collectAsState()
    val rearName = viewModel.rearHopperName.collectAsState()
    val frontName = viewModel.frontHopperName.collectAsState()
    val rearLimitCapacity = viewModel.rearGrinderLimitCapacity.collectAsState()
    val frontLimitCapacity = viewModel.frontGrinderLimitCapacity.collectAsState()
    val rearReference = viewModel.etcRearReference.collectAsState()
    val frontReference = viewModel.etcFrontReference.collectAsState()

    val on = stringResource(R.string.display_value_on)
    val off = stringResource(R.string.display_value_off)
    val yes = stringResource(R.string.statistic_yes)
    val no = stringResource(R.string.statistic_no)
    val levellingString = stringResource(R.string.bean_levelling)
    val pqcString = stringResource(R.string.bean_powder_quantity_control)
    val etcFrontString = stringResource(R.string.bean_extraction_time_control_front)
    val etcRearString = stringResource(R.string.bean_extraction_time_control_rear)
    val rearNameString = stringResource(R.string.bean_hopper_rear_name)
    val frontNameString = stringResource(R.string.bean_hopper_front_name)
    val emptyNoticeString = stringResource(R.string.statistic_clean_enter_description)

    val pqcValue = if (pqc.value) on else off
    val etcRearValue = if (etcRear.value) on else off
    val etcFrontValue = if (etcFront.value) on else off
    val levellingValue = if (levelling.value) yes else no
    val rearReferenceValue = if (etcRear.value && rearReference.value == 0f) {
        stringResource(R.string.bean_etc_value_no_reference)
    } else {
        ""
    }

    val frontReferenceValue = if (etcFront.value && frontReference.value == 0f) {
        stringResource(R.string.bean_etc_value_no_reference)
    } else {
        ""
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.common_beans_and_grinder),
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

        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 172.dp, end = 95.dp)
                .width(230.dp)
                .height(50.dp),
            text = stringResource(id = R.string.bean_grinder_adjustment)
        ) {
            itemSelectIndex.value = INVALID_INT
            ScreenDisplayManager.autoRoute(context, GrinderAdjustmentActivity::class.java)
        }

        if ((etcRear.value || etcFront.value) && pqc.value) {
            ChangeColorButton(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 692.dp)
                    .width(230.dp)
                    .height(50.dp),
                text = stringResource(id = R.string.bean_etc_configuration)
            ) {
                itemSelectIndex.value = INVALID_INT
//                ScreenDisplayManager.autoRoute(context, GrinderAdjustmentActivity::class.java)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 95.dp)
        ) {
            DisplayItemLayout(rearNameString,
                rearName.value, Color(0xFF191A1D)) {
                itemSelectIndex.value = BEAN_KEY_INDEX_REAR_HOPPER
                defaultValue.value = rearName.value
                titleValue.value = rearNameString
            }
            DisplayItemLayout(frontNameString,
                frontName.value, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = BEAN_KEY_INDEX_FRONT_HOPPER
                defaultValue.value = frontName.value
                titleValue.value = frontNameString
            }
            DisplayItemLayout(levellingString, levellingValue, Color(0xFF191A1D)) {
                itemSelectIndex.value = BEAN_KEY_INDEX_LEVELLING
                titleValue.value = levellingString
                defaultValue.value = levellingValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(yes, true),
                        Pair(no, false)
                    )
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

            DisplayItemLayout(pqcString, pqcValue, Color(0xFF2A2B2D)) {
                itemSelectIndex.value = BEAN_KEY_INDEX_PQC
                titleValue.value = pqcString
                defaultValue.value = pqcValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(on, true),
                        Pair(off, false)
                    )
                )
            }

            DisplayItemLayout(stringResource(R.string.bean_grinding_capacity_hopper_rear),
                "${grindingCapacityRear.value}", unit = "[mm/s]",
                backgroundColor = Color(0xFF191A1D), enable = !pqc.value
            ) {
                itemSelectIndex.value = BEAN_KEY_INDEX_GRINDING_CAPACITY_REAR
                scrollDefaultValue.value = grindingCapacityRear.value
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 10f
                scrollUnit.value = "[mm/s]"
                scrollAccuracy.value = ACCURACY_3
            }
            DisplayItemLayout(stringResource(R.string.bean_grinding_capacity_hopper_front),
                "${grindingCapacityFront.value}", unit = "[mm/s]",
                backgroundColor = Color(0xFF2A2B2D), enable = !pqc.value
            ) {
                itemSelectIndex.value = BEAN_KEY_INDEX_GRINDING_CAPACITY_FRONT
                scrollDefaultValue.value = grindingCapacityFront.value
                scrollRangeStart.value = 1f
                scrollRangeEnd.value = 10f
                scrollUnit.value = "[mm/s]"
                scrollAccuracy.value = ACCURACY_3
            }

            if (pqc.value) {
                DisplayItemLayout(etcRearString, etcRearValue, unit = rearReferenceValue,
                    backgroundColor = Color(0xFF191A1D)
                ) {
                    itemSelectIndex.value = BEAN_KEY_INDEX_ETC_REAR
                    titleValue.value = etcRearString
                    defaultValue.value = etcRearValue
                    dataMap.clear()
                    dataMap.putAll(
                        mapOf(
                            Pair(on, true),
                            Pair(off, false)
                        )
                    )
                }
                DisplayItemLayout(etcFrontString, etcFrontValue, unit = frontReferenceValue,
                    backgroundColor = Color(0xFF2A2B2D)
                ) {
                    itemSelectIndex.value = BEAN_KEY_INDEX_ETC_FRONT
                    titleValue.value = etcFrontString
                    defaultValue.value = etcFrontValue
                    dataMap.clear()
                    dataMap.putAll(
                        mapOf(
                            Pair(on, true),
                            Pair(off, false)
                        )
                    )
                }
                if (etcRear.value) {
                    DisplayItemLayout(
                        stringResource(R.string.bean_lower_limit_grinding_capacity_rear),
                        "${rearLimitCapacity.value}", unit = "[mm/s]",
                        backgroundColor = Color(0xFF191A1D)
                    ) {
                        itemSelectIndex.value = BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_REAR
                        scrollDefaultValue.value = rearLimitCapacity.value
                        scrollRangeStart.value = 1f
                        scrollRangeEnd.value = 10f
                        scrollUnit.value = "[mm/s]"
                        scrollAccuracy.value = ACCURACY_3
                    }
                }
                if (etcFront.value) {
                    DisplayItemLayout(
                        stringResource(R.string.bean_lower_limit_grinding_capacity_front),
                        "${frontLimitCapacity.value}", unit = "[mm/s]",
                        backgroundColor =
                        if (!etcRear.value) Color(0xFF191A1D) else Color(0xFF2A2B2D)
                    ) {
                        itemSelectIndex.value = BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_FRONT
                        scrollDefaultValue.value = frontLimitCapacity.value
                        scrollRangeStart.value = 1f
                        scrollRangeEnd.value = 10f
                        scrollUnit.value = "[mm/s]"
                        scrollAccuracy.value = ACCURACY_3
                    }
                }

            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            when (itemSelectIndex.value) {
                BEAN_KEY_INDEX_GRINDING_CAPACITY_REAR, BEAN_KEY_INDEX_GRINDING_CAPACITY_FRONT,
                BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_FRONT, BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_REAR,
                    -> {
                    key(scrollDefaultValue.value) {
                        UnitValueScrollBar(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 172.dp, end = 335.dp)
                                .width(550.dp)
                                .wrapContentHeight(),
                            value = scrollDefaultValue.value,
                            rangeStart = scrollRangeStart.value,
                            rangeEnd = scrollRangeEnd.value,
                            unit = scrollUnit.value,
                            accuracy = scrollAccuracy.value
                        ) { changeValue ->
                            viewModel.saveBeanGrinderValue(itemSelectIndex.value, changeValue)
                        }
                    }
                }
                BEAN_KEY_INDEX_REAR_HOPPER, BEAN_KEY_INDEX_FRONT_HOPPER -> {
                    SingleInputLayout(
                        defaultInput = defaultValue.value,
                        title = titleValue.value,
                        tips = stringResource(R.string.statistic_maintenance_enter_description),

                        onEnterClick = { description ->
                            if (description.isEmpty()) {
                                Toast.makeText(context, emptyNoticeString, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                viewModel.saveBeanGrinderValue(itemSelectIndex.value, description)
                                itemSelectIndex.value = INVALID_INT
                            }
                        },
                        onCloseClick = {
                            itemSelectIndex.value = INVALID_INT
                        })
                }
                else -> {
                    ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                        { _, value ->
                            viewModel.saveBeanGrinderValue(itemSelectIndex.value, value)
                            itemSelectIndex.value = INVALID_INT
                        }, {
                            itemSelectIndex.value = INVALID_INT
                        }
                    )
                }
            }
        }
    }

}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewBeanContainer() {
    BeanGrinderLayout()
}