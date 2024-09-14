package com.inno.coffee.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun VerticalScrollList(
    modifier: Modifier = Modifier,
    singularItemColor: Color = Color(0xFF191A1D),
    evenItemColor: Color = Color(0xFF2A2B2D),
) {

}

@Preview
@Composable
private fun PreviewVerticalScrollList() {
    VerticalScrollList()
}