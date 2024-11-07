package com.inno.coffee.ui.settings.bean

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp

@Composable
fun BeanGrinderLayout(
    onCloseClick: () -> Unit = {},
) {
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }

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
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.bean_grinder_setting)
        ) {
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 95.dp)
        ) {
            DisplayItemLayout(stringResource(R.string.bean_hopper_rear_name),
                "", Color(0xFF191A1D)) {
                itemSelectIndex.value = 1
            }
            DisplayItemLayout(stringResource(R.string.bean_hopper_front_name),
                "", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = 1
            }
            DisplayItemLayout(stringResource(R.string.bean_levelling),
                "", Color(0xFF191A1D)) {
                itemSelectIndex.value = 1
            }
            Spacer(modifier = Modifier.height(40.dp))

            DisplayItemLayout(stringResource(R.string.bean_powder_quantity_control),
                "", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = 1
            }
            DisplayItemLayout(stringResource(R.string.bean_grinding_capacity_hopper_rear),
                "", Color(0xFF191A1D)) {
                itemSelectIndex.value = 1
            }
            DisplayItemLayout(stringResource(R.string.bean_grinding_capacity_hopper_front),
                "", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = 1
            }
            DisplayItemLayout(stringResource(R.string.bean_extraction_time_control_rear),
                "", Color(0xFF191A1D)) {
                itemSelectIndex.value = 1
            }
            DisplayItemLayout(stringResource(R.string.bean_extraction_time_control_front),
                "", Color(0xFF2A2B2D)) {
                itemSelectIndex.value = 1
            }
        }

    }

}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewBeanContainer() {
    BeanGrinderLayout()
}