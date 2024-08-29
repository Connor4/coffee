package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.home.MakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.CoffeeSharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstallSettingActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Coffee)
        super.onCreate(savedInstanceState)
        val firstInstall = CoffeeSharedPreferences.getInstance().isFirstInstall
        if (firstInstall) {
            setContent {
                CoffeeTheme {
                    InstallSetting(onSetComplete = {
                        startCoffee()
                    })
                }
            }
        } else {
            startCoffee()
        }
    }

    private fun startCoffee() {
        ScreenDisplayManager.manualRoute(this, MakeCoffeeActivity::class.java)
        ScreenDisplayManager.autoRoute(this, MakeCoffeeActivity::class.java)
        finish()
    }

}