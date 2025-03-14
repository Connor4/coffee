package com.inno.coffee.ui.settings.machinetest.serialtest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.viewmodel.settings.machinetest.serialport.SerialPortViewModel
import com.inno.serialport.utilities.ReceivedData

@Composable
fun SerialTest(viewModel: SerialPortViewModel = hiltViewModel(), onCloseClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        val receivedData: ReceivedData? by viewModel.receivedDataFlow.collectAsState(initial = null)
        val path by viewModel.serialPortPath.collectAsState()
        val devices by viewModel.serialPortDevices.collectAsState()

        var sendData by remember {
            mutableStateOf("")
        }
        Column {
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 37.dp, end = 37.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 输入框
                OutlinedTextField(
                    value = sendData,
                    onValueChange = { sendData = it },
                    label = { Text("Send Data") },
                    modifier = Modifier.fillMaxWidth()
                )
                // 发送按钮
                Button(
                    onClick = {
                        viewModel.sendCommand(sendData)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Send", fontSize = 18.sp)
                }

                Button(
                    onClick = {
                        viewModel.openSerialPort()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "open serial port", fontSize = 18.sp)
                }

                Button(
                    onClick = {
                        viewModel.closeSerialPort()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "close serial port", fontSize = 18.sp)
                }

                // 接收数据显示
                Text(
                    text = "Received Data: ${
                        receivedData?.let {
                            var info = ""
                            when (it) {
                                is ReceivedData.SerialErrorData -> {
//                                info = "ErrorData: ${it.info}, need reboot ${it.reboot}"
                                }
                                is ReceivedData.HeartBeat -> {
//                                info = "HeartBeatData: ${it.info}, need reboot ${it.reboot}, " +
//                                        "status ${it.heartbeatStatus}"
                                }
                                else -> {}
                            }
                            info
                        } ?: "no data yet"
                    }",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.findSerialPort()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Find SerialPort", fontSize = 18.sp)
                }
                Text(
                    text = "path: $path",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "devices: $devices",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewSerialTest() {
    SerialTest() {}
}