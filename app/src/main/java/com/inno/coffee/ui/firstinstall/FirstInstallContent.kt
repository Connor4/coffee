package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InstallSetting(onSetComplete: () -> Unit) {
    Column {
        Text(text = "Settings Page", modifier = Modifier.padding(start = 10.dp, top = 20.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onSetComplete) {
            Text(text = "Complete Settings")
        }
    }
}