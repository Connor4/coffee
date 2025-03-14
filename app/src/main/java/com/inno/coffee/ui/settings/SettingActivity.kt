package com.inno.coffee.ui.settings

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.UserSessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                MachineSettingLayout() {
                    this@SettingActivity.finish()
                    UserSessionManager.clearUser()
                }
            }
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@SettingActivity.finish()
                UserSessionManager.clearUser()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

}

