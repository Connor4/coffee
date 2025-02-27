package com.inno.coffee.ui.settings.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ItemWithImageLayout
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.config.MilkSettingViewModel

@Composable
fun MilkSettingPage7(
    viewModel: MilkSettingViewModel = hiltViewModel(),
) {
    val steamPressure = 1.7f
    val normalPressure = 1.7f
    val leftMotor = 800
    val rightMotor = 700

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = stringResource(R.string.config_milk_add_milk), color = Color(0xFF32C5FF),
            fontSize = 6.nsp(), fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 54.dp, top = 20.dp)
        )

        Text(text = stringResource(R.string.config_milk_temp_adjust_press_button),
            color = Color.White,
            fontSize = 6.nsp(), textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 54.dp, top = 80.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 130.dp, top = 200.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemWithImageLayout(
                drawableRes = R.drawable.drink_milk_ic,
                stringRes = R.string.config_milk_source_left,
                width = 280, height = 180, imageSize = 70
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.config_milk_right_outlet), color = Color.White,
                fontSize = 6.nsp(), textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 210.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(
                    R.string.maintenance_steam_boiler_pressure) + "$steamPressure bar",
                color = Color.White,
                fontSize = 5.nsp(), textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.config_milk_normal_steam_boiler_temp) +
                        "$normalPressure bar",
                color = Color.White,
                fontSize = 5.nsp(), textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.config_milk_source1_motor_left) + "$leftMotor rpm",
                color = Color.White,
                fontSize = 5.nsp(), textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.config_milk_source1_motor_right) + "$rightMotor rpm",
                color = Color.White,
                fontSize = 5.nsp(), textAlign = TextAlign.Start,
            )
        }
    }
}