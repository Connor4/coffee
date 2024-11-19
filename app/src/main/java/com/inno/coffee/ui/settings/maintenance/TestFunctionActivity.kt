package com.inno.coffee.ui.settings.maintenance

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestFunctionActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                TestFunctionLayout {
                    finish()
                }
            }
        }
    }

}