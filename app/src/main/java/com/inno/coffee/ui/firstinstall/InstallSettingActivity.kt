package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.inno.coffee.data.firstinstall.InstallViewModel
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InstallSettingActivity : ComponentActivity() {
    @Inject
    lateinit var dateStore: CoffeeDataStore
    private val viewModel: InstallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("onCreate")
        lifecycleScope.launch {
            Logger.d("before start")
            val firstInstall = dateStore.getFirstInstall()
            Logger.d("first $firstInstall")
            if (firstInstall) {
                setContent {
                    CoffeeTheme {
                        InstallSetting(onSetComplete = {
                            launchMakeCoffeeActivity(this@InstallSettingActivity)
                        })
                    }
                }
            } else {
                launchMakeCoffeeActivity(this@InstallSettingActivity)
            }
        }
        viewModel.insertDefaultUsers()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}