package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstInstall = CoffeeSharedPreferences.getInstance().isFirstInstall
        if (firstInstall) {
            setContent {
                CoffeeTheme {
                    InstallSetting(onSetComplete = { date, hour, min ->
                        launchMakeCoffeeActivity(this)
                        CoffeeSharedPreferences.getInstance().isFirstInstall = false
                        TimeUtils.setDateAndTime(this, date, hour, min)
                    })
                }
            }
        } else {
            launchMakeCoffeeActivity(this)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}