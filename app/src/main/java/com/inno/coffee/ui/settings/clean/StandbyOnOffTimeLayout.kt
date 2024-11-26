package com.inno.coffee.ui.settings.clean

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.SwitchButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.clean.CleanViewModel

@Composable
fun StandbyOnOffTimeLayout(
    viewModel: CleanViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val mondayStart = "08:00"
    val mondayEnd = "18:00"
    val tuesdayStart = "08:00"
    val tuesdayEnd = "18:00"
    val wednesdayStart = "08:00"
    val wednesdayEnd = "18:00"
    val thursdayStart = "08:00"
    val thursdayEnd = "18:00"
    val fridayStart = "08:00"
    val fridayEnd = "18:00"
    val saturdayStart = "08:00"
    val saturdayEnd = "18:00"
    val sundayStart = "08:00"
    val sundayEnd = "18:00"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
    ) {
        Text(
            text = stringResource(id = R.string.clean_standby_time_settings),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .padding(top = 265.dp)
                .align(Alignment.TopCenter),
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 348.dp, bottom = 4.dp)
                    .width(800.dp)
                    .height(40.dp)
                    .background(Color(0xFF2A2B2D)),
            ) {
                Text(text = stringResource(R.string.clean_switch_on), color = Color.White,
                    fontSize = 6.nsp(),
                    modifier = Modifier.padding(start = 140.dp, top = 5.dp))
                Text(text = stringResource(R.string.clean_switch_off), color = Color.White,
                    fontSize = 6.nsp(),
                    modifier = Modifier.padding(start = 471.dp, top = 5.dp))
            }
            StandbyOnOffTimeItem(stringResource(R.string.clean_monday), viewModel.isFlagSet(0),
                mondayStart, mondayEnd, { viewModel.setFlag(0, it) }, {}, {})
            StandbyOnOffTimeItem(stringResource(R.string.clean_tuesday), viewModel.isFlagSet(1),
                tuesdayStart, tuesdayEnd, { viewModel.setFlag(1, it) }, {}, {})
            StandbyOnOffTimeItem(stringResource(R.string.clean_wednesday), viewModel.isFlagSet(2),
                wednesdayStart, wednesdayEnd, { viewModel.setFlag(2, it) }, {}, {})
            StandbyOnOffTimeItem(stringResource(R.string.clean_thursday), viewModel.isFlagSet(3),
                thursdayStart, thursdayEnd, { viewModel.setFlag(3, it) }, {}, {})
            StandbyOnOffTimeItem(stringResource(R.string.clean_friday), viewModel.isFlagSet(4),
                fridayStart, fridayEnd, { viewModel.setFlag(4, it) }, {}, {})
            StandbyOnOffTimeItem(stringResource(R.string.clean_saturday), viewModel.isFlagSet(5),
                saturdayStart, saturdayEnd, { viewModel.setFlag(5, it) }, {}, {})
            StandbyOnOffTimeItem(stringResource(R.string.clean_sunday), viewModel.isFlagSet(6),
                sundayStart, sundayEnd, { viewModel.setFlag(6, it) }, {}, {})
        }

    }

}

@Composable
private fun StandbyOnOffTimeItem(
    day: String,
    isOn: Boolean,
    startTime: String,
    endTime: String,
    onToggle: (Boolean) -> Unit = {},
    onClickStart: () -> Unit = {},
    onClickEnd: () -> Unit = {},
) {
    var state by remember { mutableStateOf(isOn) }
    val textColor = if (state) Color.White else Color(0xFF3E3F44)

    Row(
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Text(text = day, color = Color.White, fontSize = 6.nsp(),
            modifier = Modifier
                .width(200.dp)
                .height(40.dp)
                .background(Color(0xFF191A1D))
                .padding(start = 26.dp, top = 5.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        SwitchButton(state) { newState ->
            onToggle(newState)
            state = newState
        }
        Spacer(modifier = Modifier.width(14.dp))

        Text(text = startTime, color = textColor, fontSize = 6.nsp(),
            modifier = Modifier
                .width(400.dp)
                .height(40.dp)
                .background(Color(0xFF191A1D))
                .padding(start = 140.dp, top = 5.dp)
                .fastclick(enabled = state) { onClickStart() }
        )
        Text(text = endTime, color = textColor, fontSize = 6.nsp(),
            modifier = Modifier
                .width(400.dp)
                .height(40.dp)
                .background(Color(0xFF191A1D))
                .padding(start = 70.dp, top = 5.dp)
                .fastclick(enabled = state) { onClickEnd() }
        )
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewStandbyOnOffTime() {
    StandbyOnOffTimeLayout()
}