package com.inno.coffee.ui.settings.serialtest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint


fun launchSerialPortActivity(context: Context) {
    context.startActivity(Intent(context, SerialPortActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
}

@AndroidEntryPoint
class SerialPortActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                SerialTest()
            }
        }
    }
}