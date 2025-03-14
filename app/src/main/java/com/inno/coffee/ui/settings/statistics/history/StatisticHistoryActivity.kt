package com.inno.coffee.ui.settings.statistics.history

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.HISTORY_VALUE_CLEAN
import com.inno.coffee.utilities.HISTORY_VALUE_ERROR
import com.inno.coffee.utilities.HISTORY_VALUE_MAINTENANCE
import com.inno.coffee.utilities.HISTORY_VALUE_PRODUCT
import com.inno.coffee.utilities.HISTORY_VALUE_RINSE
import com.inno.coffee.utilities.KEY_HISTORY
import com.inno.common.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticHistoryActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.extras?.getString(KEY_HISTORY) ?: HISTORY_VALUE_PRODUCT
        Logger.d("StatisticHistoryActivity", "onCreate() index = $index")
        setContent {
            CoffeeTheme {
                when (index) {
                    HISTORY_VALUE_PRODUCT -> {
                        ProductHistoryLayout {
                            this@StatisticHistoryActivity.finish()
                        }
                    }
                    HISTORY_VALUE_RINSE -> {
                        RinseHistoryLayout {
                            this@StatisticHistoryActivity.finish()
                        }
                    }
                    HISTORY_VALUE_CLEAN -> {
                        CleanMachineHistoryLayout() {
                            this@StatisticHistoryActivity.finish()
                        }
                    }
                    HISTORY_VALUE_ERROR -> {
                        ErrorHistoryLayout {
                            this@StatisticHistoryActivity.finish()
                        }
                    }
                    HISTORY_VALUE_MAINTENANCE -> {
                        MaintenanceHistoryLayout {
                            this@StatisticHistoryActivity.finish()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}