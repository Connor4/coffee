package com.inno.coffee.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity

open class CoffeeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}