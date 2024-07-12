package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.inno.coffee.data.firstinstall.InstallViewModel
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstallSettingActivity : ComponentActivity() {

    private val viewModel: InstallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.firstInstall.observe(this) {
            if (it) {
                setContent {
                    CoffeeTheme {
                        InstallSetting(onSetComplete = {
                            launchMakeCoffeeActivity(this)
                        })
                    }
                }
            } else {
                launchMakeCoffeeActivity(this)
            }
        }
        viewModel.insertDefaultUsers()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}