package com.inno.coffee.ui.settings.info

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.INFO_KEY_ACTIVITY
import com.inno.coffee.utilities.INFO_VALUE_COFFEE_ACTIVITY
import com.inno.coffee.utilities.INFO_VALUE_STEAM_ACTIVITY
import com.inno.coffee.viewmodel.settings.info.MachineInfoViewModel
import com.inno.common.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineInfoStatusActivity : CoffeeActivity() {
    private val viewModel: MachineInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index =
            intent.extras?.getString(INFO_KEY_ACTIVITY) ?: INFO_VALUE_COFFEE_ACTIVITY
        Logger.d("MachineInfoStatusActivity", "onCreate() index = $index")
        setContent {
            CoffeeTheme {
                when (index) {
                    INFO_VALUE_COFFEE_ACTIVITY -> {
                        CoffeeStatusLayout(viewModel) {
                            finish()
                        }
                    }
                    INFO_VALUE_STEAM_ACTIVITY -> {
                        SteamStatusLayout(viewModel) {
                            finish()
                        }
                    }
                }
            }
        }
    }

}