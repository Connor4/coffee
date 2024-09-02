package com.inno.coffee.utilities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color

@Composable
fun MaskBoxWithContent(enableMask: Boolean = false, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                if (enableMask) {
                    drawRect(
                        color = Color(0xCC000000),
                        size = this.size,
                    )
                }
            }
    ) {
        content()
    }
}