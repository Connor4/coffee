package com.inno.coffee.ui.settings.machinetest

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.MACHINE_TEST_KEY_ACTIVITY
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_COFFEE_INPUTS
import com.inno.coffee.utilities.MACHINE_TEST_VALUE_STEAM_INPUTS
import com.inno.coffee.viewmodel.settings.machinetest.MachineTestInputViewModel
import com.inno.common.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineTestInputsActivity : CoffeeActivity() {
    private val viewModel: MachineTestInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index =
            intent.extras?.getString(MACHINE_TEST_KEY_ACTIVITY) ?: MACHINE_TEST_VALUE_COFFEE_INPUTS
        Logger.d("MachineTestInputsActivity", "onCreate() index = $index")
        setContent {
            CoffeeTheme {
                when (index) {
                    MACHINE_TEST_VALUE_COFFEE_INPUTS -> {
                        MachineTestCoffeeInputLayout(viewModel) {
                            finish()
                        }
                    }
                    MACHINE_TEST_VALUE_STEAM_INPUTS -> {
                        MachineTestSteamInputLayout(viewModel) {
                            finish()
                        }
                    }
                }
            }
        }
    }

}