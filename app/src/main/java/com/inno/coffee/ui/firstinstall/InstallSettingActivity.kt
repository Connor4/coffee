package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.readFirstInstall
import com.inno.common.utils.saveFirstInstall
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 首次安装页面
 */
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firstInstall = runBlocking {
            this@InstallSettingActivity.readFirstInstall()
        }

        if (true) {
            setContent {
                CoffeeTheme {
                    InstallSetting {
                        launchMakeCoffeeActivity(this)
                        lifecycleScope.launch {
                            this@InstallSettingActivity.saveFirstInstall(false)
                        }
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