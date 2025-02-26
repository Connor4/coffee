package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ACCURACY_3
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

@Composable
fun MilkSettingLayout(
    onCloseClick: () -> Unit = {},
) {
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
                    Pair(no, false),
                    Pair(yes, true)
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
                    Pair(off, false),
                    Pair(on, true)
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
                        Pair(off, false),
                        Pair(on, true)
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
                        Pair(off, false),
                        Pair(on, true)
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
}

@Preview
@Composable
private fun PreviewMilkSettingLayout() {
    MilkSettingLayout()
}