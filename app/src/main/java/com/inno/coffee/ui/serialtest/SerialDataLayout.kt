package com.inno.coffee.ui.serialtest

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inno.serialport.bean.PullBufInfo
import com.inno.serialport.core.SerialPortFinder
import com.inno.serialport.function.SerialPortDataManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SerialTest() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        var sendData by remember {
            mutableStateOf("")
        }
        var receivedData by remember {
            mutableStateOf<PullBufInfo?>(null)
        }
        var path by remember {
            mutableStateOf("path: ")
        }
        var devices by remember {
            mutableStateOf("devices: ")
        }
        val coroutineScope = rememberCoroutineScope()

        val context = LocalContext.current
        val serialPortFinder = SerialPortFinder()
        val onFindClick = {
            val allDevicesPath = serialPortFinder.getAllDevicesPath()
            allDevicesPath.forEach {
                path += "$it "
            }
            val allDevices = serialPortFinder.getAllDevices()
            allDevices.forEach {
                devices += "$it "
            }
        }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                while (true) {
                    receivedData = SerialPortDataManager.instance.processData()
                    delay(500L)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    SerialPortDataManager.instance.init()
                    Toast.makeText(context, "init", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "init", fontSize = 18.sp)
            }
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
                    coroutineScope.launch {
                        SerialPortDataManager.instance.sendCommand("")
                    }
                    Toast.makeText(context, "send", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Send", fontSize = 18.sp)
            }
            // 接收数据显示
            Text(
                text = "Received Data: $receivedData",
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onFindClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Find SerialPort", fontSize = 18.sp)
            }
            Text(
                text = path,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = devices,
                fontSize = 14.sp,
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