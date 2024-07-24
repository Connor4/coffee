package com.inno.coffee.ui.settings.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchPermissionActivity(context: Context) {
    context.startActivity(Intent(context, PermissionActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
}

@AndroidEntryPoint
class PermissionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                PermissionEntrance()
            }
        }
    }
}