package com.inno.coffee.ui.settings.remote

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.SingleInputLayout
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.DisplayItemLayout
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.REMOTE_MACHINE_ID
import com.inno.coffee.utilities.REMOTE_TELEMETRY_SWITCH
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.remote.RemoteMonitorViewModel

@Composable
fun RemoteMonitorLayout(
    viewModel: RemoteMonitorViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val titleValue = remember { mutableStateOf("") }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }

    val telemetrySwitch = viewModel.telemetrySwitch.collectAsState()
    val telemetryId = viewModel.telemetryId.collectAsState()

    val telemetryTitle = stringResource(R.string.remote_telemetry)
    val remoteIdTitle = stringResource(R.string.remote_id)
    val openString = stringResource(R.string.remote_telemetry_open)
    val closeString = stringResource(R.string.remote_telemetry_close)
    val emptyNoticeString = stringResource(R.string.statistic_clean_enter_description)

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    val switchValue = if (telemetrySwitch.value) {
        openString
    } else {
        closeString
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = telemetryTitle,
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

        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 172.dp, end = 95.dp)
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.remote_check_connection)
        ) {
            viewModel.checkConnection()
        }

        Column(
            modifier = Modifier.padding(top = 256.dp, start = 50.dp, end = 95.dp)
        ) {
            DisplayItemLayout(telemetryTitle, switchValue,
                Color(0xFF191A1D)
            ) {
                itemSelectIndex.value = REMOTE_TELEMETRY_SWITCH
                titleValue.value = telemetryTitle
                defaultValue.value = switchValue
                dataMap.clear()
                dataMap.putAll(
                    mapOf(
                        Pair(openString, true),
                        Pair(closeString, false)
                    )
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            DisplayItemLayout(remoteIdTitle, telemetryId.value,
                Color(0xFF2A2B2D)
            ) {
                itemSelectIndex.value = REMOTE_MACHINE_ID
                titleValue.value = remoteIdTitle
                defaultValue.value = telemetryId.value
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            when (itemSelectIndex.value) {
                REMOTE_TELEMETRY_SWITCH -> {
                    ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                        { _, value ->
                            viewModel.saveRemoteMonitorValue(itemSelectIndex.value, value)
                            itemSelectIndex.value = INVALID_INT
                        }, {
                            itemSelectIndex.value = INVALID_INT
                        }
                    )
                }
                REMOTE_MACHINE_ID -> {
                    SingleInputLayout(
                        defaultInput = defaultValue.value,
                        title = titleValue.value,
                        tips = stringResource(R.string.statistic_maintenance_enter_description),
                        maxInputLimitSize = 5,

                        onEnterClick = { description ->
                            if (description.isEmpty()) {
                                Toast.makeText(context, emptyNoticeString, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                viewModel.saveRemoteMonitorValue(itemSelectIndex.value, description)
                                itemSelectIndex.value = INVALID_INT
                            }
                        },
                        onCloseClick = {
                            itemSelectIndex.value = INVALID_INT
                        })
                }
            }
        }

    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewRemoteMonitor() {
    RemoteMonitorLayout()
}