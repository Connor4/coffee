package com.inno.coffee.ui.home

import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun MachineInfoLayout(
    sn: String = "",
    version: String = "",
    company: String = "",
    id: String = "",
    onCloseClick: () -> Unit,
) {
    val context = LocalContext.current
    val versionName = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
    val snNumber = Build.getSerial()

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .fillMaxWidth()
//            .height(800.dp)
//            .padding(top = 60.dp, bottom = 70.dp)
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_entrance_dialog_bg),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(834.dp)
                .height(394.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(340.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF191A1D))
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
                modifier = Modifier
                    .width(515.dp)
                    .wrapContentHeight()
                    .align(Alignment.Center),
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.home_entrance_info_sn),
                        fontSize = 5.nsp(),
                        color = Color.White,
                        modifier = Modifier.weight(6f)
                    )
                    Text(
                        text = snNumber, fontSize = 5.nsp(), color = Color(0xFF00AD72),
                        modifier = Modifier.weight(4f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = stringResource(id = R.string.home_entrance_info_version),
                        fontSize = 5.nsp(), color = Color.White,
                        modifier = Modifier.weight(6f)
                    )
                    Text(
                        text = "V$versionName", fontSize = 5.nsp(), color = Color(0xFF00AD72),
                        modifier = Modifier.weight(4f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = stringResource(id = R.string.home_entrance_info_service_company),
                        fontSize = 5.nsp(), color = Color.White, modifier = Modifier.weight(6f)
                    )
                    Text(
                        text = "<INNO Future>", fontSize = 5.nsp(), color = Color(0xFF00AD72),
                        modifier = Modifier.weight(4f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = stringResource(id = R.string.home_entrance_info_machine_id),
                        fontSize = 5.nsp(), color = Color.White, modifier = Modifier.weight(6f)
                    )
                    Text(
                        text = "255895", fontSize = 5.nsp(), color = Color(0xFF00AD72),
                        modifier = Modifier.weight(4f)
                    )
                }
            }
        }
    }
}


@Preview(device = Devices.TABLET)
@Composable
private fun PreviewEntrance() {
    MachineInfoLayout() {}
}