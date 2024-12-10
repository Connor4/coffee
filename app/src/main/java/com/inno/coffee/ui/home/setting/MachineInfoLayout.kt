package com.inno.coffee.ui.home.setting

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
                modifier = Modifier.padding(start = 41.dp, top = 30.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 25.dp, end = 22.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 109.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(515.dp)
                        .height(160.dp)
                        .align(Alignment.TopCenter),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Row {
                        Text(
                            text = stringResource(id = R.string.home_entrance_info_sn),
                            fontSize = 5.nsp(),
                            color = Color.White,
                            modifier = Modifier
                                .weight(6f)
                                .align(Alignment.CenterVertically)
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
                            modifier = Modifier
                                .weight(6f)
                                .align(Alignment.CenterVertically)
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
                            fontSize = 5.nsp(), color = Color.White,
                            modifier = Modifier
                                .weight(6f)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "<INNO Future>", fontSize = 5.nsp(), color = Color(0xFF00AD72),
                            modifier = Modifier
                                .weight(4f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Text(
                            text = stringResource(id = R.string.home_entrance_info_machine_id),
                            fontSize = 5.nsp(), color = Color.White,
                            modifier = Modifier
                                .weight(6f)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "255895", fontSize = 5.nsp(), color = Color(0xFF00AD72),
                            modifier = Modifier
                                .weight(4f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}


@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewEntrance() {
    MachineInfoLayout() {}
}