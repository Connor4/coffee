package com.inno.coffee.ui.settings

import android.os.Bundle
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
//                SettingCardLayout()
                MachineSettingLayout() {
                    this@SettingActivity.finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserSessionManager.clearUser()
    }

}

