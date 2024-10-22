package com.inno.coffee.ui.settings.display.groupone

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.DISPLAY_SETTING_KEY
import com.inno.coffee.utilities.DISPLAY_SETTING_LANGUAGE
import com.inno.coffee.utilities.DISPLAY_SETTING_TIME
import com.inno.common.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplaySettingActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.extras?.getString(DISPLAY_SETTING_KEY) ?: DISPLAY_SETTING_LANGUAGE
        Logger.d("DisplaySettingActivity", "onCreate() index = $index")
        setContent {
            CoffeeTheme {
                when (index) {
                    DISPLAY_SETTING_LANGUAGE -> {
                        DisplayLanguageSettingLayout({
                            this@DisplaySettingActivity.finish()
                        })
                    }
                    DISPLAY_SETTING_TIME -> {
                        DisplaySettingDateLayout() {
                            this@DisplaySettingActivity.finish()
                        }
                    }
                }
            }
        }
    }

}
