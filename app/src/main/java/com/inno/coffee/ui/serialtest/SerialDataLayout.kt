package com.inno.coffee.ui.serialtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.data.home.SerialPortViewModel
import com.inno.serialport.bean.ReceivedData

@Composable
fun SerialTest(viewModel: SerialPortViewModel = hiltViewModel()) {
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
                            is ReceivedData.ErrorData -> {
                                info = "ErrorData: ${it.info}, need reboot ${it.reboot}"
                            }
                            is ReceivedData.PartData -> {
                                info = it.info
                            }
                            is ReceivedData.HeartBeat -> {
                                info = "HeartBeatData: ${it.info}, need reboot ${it.reboot}, " +
                                        "status ${it.heartbeatStatus}"
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

@Preview(device = Devices.TABLET)
@Composable
fun PreviewSerialTest() {
    SerialTest()
}