package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ItemWithImageLayout
import com.inno.coffee.ui.settings.formula.FormulaValueItem
import com.inno.coffee.utilities.getImageResId
import com.inno.coffee.utilities.getStringResId
import com.inno.coffee.utilities.nsp
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem

@Composable
fun ETCSettingsPage4(
    blade: Float = 0f,
    adjust: Float = 0f,
    isFahrenheit: Boolean = false,
    isFront: Boolean,
    formula: Formula?,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.bean_etc_settings_4_check_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 70.dp, top = 10.dp)
                .width(269.dp)
                .wrapContentHeight()
        )

        Text(text = stringResource(R.string.bean_etc_settings_4_press_notice),
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 70.dp, top = 70.dp)
                .width(200.dp)
                .height(190.dp)
        )

        ItemWithImageLayout(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 70.dp, top = 130.dp),
            drawableRes = getImageResId(formula?.imageRes ?: "drink_item_empty_ic"),
            stringRes = getStringResId(formula?.productName?.nameRes ?: ""),
            width = 200, height = 180, imageSize = 60
        ) {}
        ItemWithImageLayout(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 70.dp, top = 330.dp),
            drawableRes = getImageResId("etc_setting_auto_adjust_ic"),
            stringRes = getStringResId("bean_etc_settings_4_auto_adjust"),
            width = 200, height = 180, imageSize = 60
        ) {}

        val hopperDrawable = if (isFront) {
            R.drawable.etc_setting_front_bean_hopper_ic
        } else {
            R.drawable.etc_setting_rear_bean_hopper_ic
        }
        Image(painter = painterResource(id = hopperDrawable),
            modifier = Modifier
                .padding(start = 400.dp, top = 200.dp)
                .width(180.dp)
                .height(120.dp),
            contentDescription = null
        )

        Text(text = stringResource(R.string.bean_etc_settings_4_blade_grinder_capacity) + blade +
                " [mm/s]",
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 320.dp, top = 400.dp)
                .wrapContentSize()
        )

        Text(text = stringResource(R.string.bean_etc_settings_4_adjust_thickness) + adjust,
            fontSize = 5.nsp(), color = Color.White, modifier = Modifier
                .padding(start = 320.dp, top = 440.dp)
                .wrapContentSize()
        )

        FormulaValueItem(isFahrenheit = isFahrenheit, selectFormula = formula,
            fromETCSetting = true,
            onValueChange = {
            }, onProductTest = {
            }, onLearn = { index ->
            })
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun ETCSetting4Preview() {
    ETCSettingsPage4(3.21f, 2f, false, true,
        Formula(productId = 4, imageRes = "operate_rinse_ic",
            productName = FormulaItem.FormulaProductName(name = "",
                nameRes = "home_item_rinse")))
}