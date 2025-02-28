package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun FormulaValuesDisplay(
    leftCoffee: String = "0",
    rightCoffee: String = "0",
    wand: String = "0",
    steam: Float = 0f,
    flow: Float = 0f,
    extractTime: Float = 0f,
    coffeePressure: Float = 0f,
    thickness: Float = 0f,
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 657.dp, start = 700.dp)
    ) {
        Row {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.home_left_boiler_temperature),
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(id = R.string.home_right_boiler_temperature),
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = " $leftCoffee",
                    fontSize = 4.nsp(),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = " $rightCoffee",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.formula_steam_wand),
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(id = R.string.formula_steam_boiler),
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = " $wand",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = " $steam bar",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "$flow ticks/s",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$extractTime s",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "$coffeePressure bar",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$thickness mm",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewValuesDisplay() {
    FormulaValuesDisplay()
}