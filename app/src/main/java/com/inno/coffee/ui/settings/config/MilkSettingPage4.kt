package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ItemWithImageLayout
import com.inno.coffee.viewmodel.settings.config.MilkSettingViewModel

@Composable
fun MilkSettingPage4(
    viewModel: MilkSettingViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
        ) {
            ItemWithImageLayout(drawableRes = R.drawable.config_milk_wash_ic,
                stringRes = R.string.config_milk_start_wash)
            Spacer(modifier = Modifier.width(60.dp))
            ItemWithImageLayout(drawableRes = R.drawable.config_milk_cleaned_ic,
                stringRes = R.string.config_milk_confirm_cleaned)
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewMilkSettingPage4() {
    MilkSettingPage4()
}