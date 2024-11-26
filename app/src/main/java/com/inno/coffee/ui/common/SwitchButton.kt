package com.inno.coffee.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun SwitchButton(
    isOn: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    val switchColor = if (isOn) Color(0xFF00DE93) else Color(0xFF2C2C2C) // Toggle colors
    val textColor = if (isOn) Color.Black else Color(0xFFC0C0C0)
    val knobColor = if (isOn) Color(0xFF2C2C2C) else Color(0xB3FFFFFF) // Knob colors

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(switchColor)
            .clickable { onToggle(!isOn) }
            .padding(horizontal = 4.dp),
        contentAlignment = if (isOn) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Knob (circle)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(knobColor, CircleShape)
            )
        }

        // Label (On/Off text)
        Text(
            text = if (isOn) stringResource(R.string.statistic_on)
            else stringResource(R.string.statistic_off),
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 5.nsp(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = if (isOn) 20.dp else 0.dp, start = if (isOn) 0.dp else 20.dp)
        )
    }
}