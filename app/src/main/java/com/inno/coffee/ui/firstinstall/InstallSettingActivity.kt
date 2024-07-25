package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.home.MakeCoffeeActivity
import com.inno.coffee.ui.presentation.PresentationDisplayManager
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstallSettingActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstInstall = CoffeeSharedPreferences.getInstance().isFirstInstall
        if (firstInstall) {
            setContent {
                CoffeeTheme {
                    InstallSetting(onSetComplete = { date, hour, min ->
                        CoffeeSharedPreferences.getInstance().isFirstInstall = false
                        TimeUtils.setDateAndTime(this, date, hour, min)
                        startCoffee()
                    })
                }
            }
        } else {
            startCoffee()
        }
    }

    private fun startCoffee() {
        PresentationDisplayManager.autoRoute(this, MakeCoffeeActivity::class.java)
        PresentationDisplayManager.manualRoute(this, MakeCoffeeActivity::class.java, false)
        finish()
    }

}