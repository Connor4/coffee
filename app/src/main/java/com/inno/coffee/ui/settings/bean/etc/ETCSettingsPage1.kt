package com.inno.coffee.ui.settings.bean.etc

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.inno.coffee.utilities.nsp

@Composable
fun ETCSettingsPage1() {
    Box() {
        Text(text = "", fontSize = 6.nsp(), color = Color.White)
    }
}

@Preview
@Composable
private fun ETCSettingOnePreview() {
    ETCSettingsPage1()
}