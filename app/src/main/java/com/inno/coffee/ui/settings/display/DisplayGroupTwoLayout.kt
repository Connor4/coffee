package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.inno.coffee.R

@Composable
fun DisplayGroupTwoLayout(

) {
    Column {
        DisplayItemLayout(stringResource(R.string.display_auto_back_to_first_page),
            "OFF",
            Color(0xFF191A1D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_number_of_product_per_page),
            "12",
            Color(0xFF2A2B2D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_front_light_color),
            "AUTO",
            Color(0xFF191A1D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_front_light_brightness),
            "90",
            Color(0xFF2A2B2D)) {
        }
        DisplayItemLayout(stringResource(R.string.display_screen_brightness),
            "90",
            Color(0xFF191A1D)) {
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayGroupTwo() {
    DisplayGroupTwoLayout()
}