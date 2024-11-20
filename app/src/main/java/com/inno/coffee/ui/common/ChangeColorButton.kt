package com.inno.coffee.ui.common

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.utilities.nsp

@Composable
fun ChangeColorButton(
    modifier: Modifier = Modifier,
    text: String = "",
    pressedTextColor: Color = Color.White,
    normalTextColor: Color = Color.White,
    pressedBoarderColor: Color = Color(0xFF00DE93),
    normalBoarderColor: Color = Color(0xFF484848),
    onClick: () -> Unit = {},
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressed by interactionSource.collectIsPressedAsState()
    val textColor: Color?
    val boarderColor: Color?
    if (pressed) {
        textColor = pressedTextColor
        boarderColor = pressedBoarderColor
    } else {
        textColor = normalTextColor
        boarderColor = normalBoarderColor
    }

    Button(
        modifier = modifier,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF191A1D)),
        border = BorderStroke(2.dp, boarderColor),
        shape = RoundedCornerShape(10.dp),
        onClick = composeClick {
            onClick()
        },
    ) {
        Text(
            text = text,
            fontSize = 5.nsp(),
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PreviewChangeColorButton() {
    ChangeColorButton()
}