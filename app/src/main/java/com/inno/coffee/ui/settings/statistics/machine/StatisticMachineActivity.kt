package com.inno.coffee.ui.settings.statistics.machine

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticMachineActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                Text(text = "*未开发")
            }
        }
    }

}