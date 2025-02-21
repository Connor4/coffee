package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.home.DrinkItem
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem

@Composable
fun ETCSettingsPage1(
    left1Flow: Float = 0f,
    left2Flow: Float = 0f,
    right1Flow: Float = 0f,
    right2Flow: Float = 0f,
    onClickRinse: () -> Unit = {},
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 54.dp)
    ) {
        Text(
            text = stringResource(R.string.bean_etc_settings_1_rinse_rate),
            fontSize = 6.nsp(), color = Color.White,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(text = stringResource(R.string.bean_etc_settings_1_notice),
            fontSize = 5.nsp(), color = Color.White,
            modifier = Modifier.padding(top = 60.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 155.dp)
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF191A1D))
        ) {
            Text(text = stringResource(R.string.bean_etc_settings_1_flow), fontSize = 5.nsp(),
                color = Color.White, modifier = Modifier.padding(start = 296.dp, top = 20.dp))
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 506.dp, top = 20.dp)
            ) {
                RateValueDisplay(left1Flow, right1Flow)
                Spacer(modifier = Modifier.height(10.dp))
                RateValueDisplay(left2Flow, right2Flow)
            }
        }

        DrinkItem(
            formula = Formula(productId = 4, imageRes = "operate_rinse_ic",
                productName = FormulaItem.FormulaProductName(name = "",
                    nameRes = "home_item_rinse")),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 307.dp),
            onDrinkClick = { onClickRinse() }
        )
    }
}

@Composable
private fun RateValueDisplay(left: Float, right: Float) {
    Row {
        Text(
            text = stringResource(R.string.statistic_left) + ": $left",
            fontSize = 5.nsp(), color = Color.White,
        )
        Spacer(modifier = Modifier.width(40.dp))
        Text(
            text = stringResource(R.string.statistic_right) + ": $right         [tick/s]",
            fontSize = 5.nsp(), color = Color.White,
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun ETCSettingOnePreview() {
    ETCSettingsPage1()
}