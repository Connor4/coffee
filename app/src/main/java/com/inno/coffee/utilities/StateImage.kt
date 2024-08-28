package com.inno.coffee.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun StateImage(
    normalImage: Painter,
    pressedImage: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    var isPressed by remember {
        mutableStateOf(false)
    }

    val image = if (isPressed) pressedImage else normalImage

    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                    onClick()
                }
            )
        }
    )
}