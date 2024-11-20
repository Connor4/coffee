package com.inno.coffee.ui.settings.maintenance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun TestFunctionLayout(
//    viewModel: TestFunctionViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.maintenance_test_functions),
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

        Row(
            modifier = Modifier.padding(start = 200.dp, top = 260.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.config_steam_wand_left), color = Color.White,
                    fontSize = 5.nsp())
                Text(text = "0", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 20.dp))
                Text(text = "0", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 65.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.padding(top = 35.dp)
            ) {
                TestFunctionButton(stringResource(R.string.maintenance_grinder_test)) {

                }
                Spacer(modifier = Modifier.height(40.dp))
                TestFunctionButton(stringResource(R.string.maintenance_flow_rate_test)) {

                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.config_steam_wand_right), color = Color.White,
                    fontSize = 5.nsp())
                Text(text = "0", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 20.dp))
                Text(text = "0", color = Color.White, fontSize = 10.nsp(),
                    modifier = Modifier.padding(top = 65.dp))
            }
        }
        Column(
            modifier = Modifier.padding(start = 750.dp, top = 295.dp)
        ) {
            TestFunctionButton(stringResource(R.string.maintenance_milk_test_left)) {

            }
            Spacer(modifier = Modifier.height(40.dp))
            TestFunctionButton(stringResource(R.string.maintenance_milk_test_right)) {

            }
            Spacer(modifier = Modifier.height(40.dp))
            TestFunctionButton(stringResource(R.string.maintenance_ball_dispenser_test)) {

            }
        }
    }
}

@Composable
private fun TestFunctionButton(
    title: String,
    onClick: (Boolean) -> Unit,
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
    var selected by remember { mutableStateOf(false) }
    val bgColor = if (selected) Color(0xFF00DE93) else Color(0xFF191A1D)

    Button(
        modifier = Modifier
            .width(280.dp)
            .height(73.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        border = BorderStroke(2.dp, boarderColor),
        shape = RoundedCornerShape(10.dp),
        onClick = composeClick {
            selected = !selected
            onClick(selected)
        },
    ) {
        Text(
            text = title,
            fontSize = 5.nsp(),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewTestFunction() {
    TestFunctionLayout()
}