package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstInstall = PreferencesManager.getInstance(this).isFirstInstall
        if (firstInstall) {
            setContent {
                CoffeeTheme {
                    InstallSetting(onSetComplete = {
                        PreferencesManager.getInstance(this).isFirstInstall = false
                        launchMakeCoffeeActivity(this)
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