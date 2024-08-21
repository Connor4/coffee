package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayMain(
    viewModel: DisplayViewModel = hiltViewModel(),
) {
    Column {

    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewDisplayMain() {
    DisplayMain()
}