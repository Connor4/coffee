package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.draw9Patch
import com.inno.coffee.utilities.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun MachineInfoLayout(
    onCloseClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(800.dp)
            .padding(top = 60.dp, bottom = 70.dp)
            .background(Color(0xED000000)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(340.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF191A1D))
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg)
        ) {
            Text(
                text = stringResource(id = R.string.home_machine_info),
                fontSize = 7.nsp(), color = Color.White, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 47.dp, top = 37.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 37.dp, end = 37.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )
            Column(
                modifier = Modifier.padding(top = 115.dp, start = 136.dp)
            ) {
                Text(text = stringResource(id = R.string.home_entrance_info_sn), fontSize = 5.nsp(),
                    color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = stringResource(id = R.string.home_entrance_info_version),
                    fontSize = 5.nsp(), color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = stringResource(id = R.string.home_entrance_info_service_company),
                    fontSize = 5.nsp(), color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = stringResource(id = R.string.home_entrance_info_machine_id),
                    fontSize = 5.nsp(), color = Color.White)
            }
            Column(
                modifier = Modifier.padding(top = 115.dp, start = 444.dp)
            ) {
                Text(text = "22263 E0002", fontSize = 5.nsp(), color = Color(0xFF00AD72))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "V1.0", fontSize = 5.nsp(), color = Color(0xFF00AD72))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "<Service Company>", fontSize = 5.nsp(), color = Color(0xFF00AD72))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "255895", fontSize = 5.nsp(), color = Color(0xFF00AD72))
            }
        }
    }
}


@Preview(device = Devices.TABLET)
@Composable
private fun PreviewEntrance() {
    MachineInfoLayout() {}
}