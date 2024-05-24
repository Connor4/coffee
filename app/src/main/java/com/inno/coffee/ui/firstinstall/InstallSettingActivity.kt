package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utils.PreferencesManager

/**
 * 首次安装页面
 */
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferencesManager = PreferencesManager.getInstance(this@InstallSettingActivity)
        val firstInstall = preferencesManager.isFirstInstall
        if (true) {
            setContent {
                CoffeeTheme {
                    InstallSetting {
                        preferencesManager.isFirstInstall = false
                        launchMakeCoffeeActivity(this)
                        finish()
                    }
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