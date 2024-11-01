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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun FormulaValuesDisplay(
    coffee: Int = 0,
    milk: Int = 0,
    wand: Int = 0,
    steam: Int = 0,
    flow: Int = 0,
    extract: Float = 0.0f,
    bar: Float = 0.0f,
    press: Int = 0,
    temperature: Int = 0,
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 657.dp, start = 719.dp)
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
                    text = " $coffee°C",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = " $milk°C",
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
                    text = " $wand°C",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = " $steam°C",
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
                    text = "$extract s",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "$bar bar",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$press mm",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$temperature°C/°F",
                    fontSize = 4.nsp(),
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewValuesDisplay() {
    FormulaValuesDisplay()
}