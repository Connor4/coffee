package com.inno.coffee.ui.settings.machinetest

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp

@Composable
fun MachineTestItem(
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(73.dp)
            .border(1.dp, Color(0xFF484848), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF191A1D))
            .debouncedClickable({ onClick() })
    ) {
        Text(
            text = stringResource(id = title),
            fontSize = 5.nsp(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}