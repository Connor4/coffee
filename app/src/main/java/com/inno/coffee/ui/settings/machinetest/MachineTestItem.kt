package com.inno.coffee.ui.settings.machinetest

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.utilities.nsp

@Composable
fun MachineTestItem(
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressed by interactionSource.collectIsPressedAsState()
    val boarderColor = if (pressed) {
        Color(0xFF00DE93)
    } else {
        Color(0xFF484848)
    }

    Button(
        modifier = Modifier
            .width(280.dp)
            .height(73.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF191A1D)),
        border = BorderStroke(2.dp, boarderColor),
        shape = RoundedCornerShape(10.dp),
        onClick = composeClick {
            onClick()
        },
    ) {
        Text(
            text = stringResource(id = title),
            fontSize = 5.nsp(),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}