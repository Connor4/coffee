package com.inno.coffee.ui.settings.remote

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteMonitorActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                RemoteMonitorLayout {
                    finish()
                }
            }
        }
    }

}