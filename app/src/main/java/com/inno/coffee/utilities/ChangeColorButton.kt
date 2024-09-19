package com.inno.coffee.utilities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChangeColorButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressed by interactionSource.collectIsPressedAsState()
    val bgColor: Color?
    val textColor: Color?
    val boarderColor: Color?
    if (pressed) {
        bgColor = Color(0xFF00DE93)
        textColor = Color.Black
        boarderColor = Color(0xFF00DE93)
    } else {
        bgColor = Color(0xFF191A1D)
        textColor = Color.White
        boarderColor = Color(0xFF484848)
    }

    Button(
        modifier = modifier,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        border = BorderStroke(1.dp, boarderColor),
        shape = RoundedCornerShape(10.dp),
        onClick = composeClick {
            onClick()
        },
    ) {
        Text(
            text = text,
            fontSize = 5.nsp(),
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PreviewChangeColorButton() {
    ChangeColorButton()
}