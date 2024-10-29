package com.inno.coffee.ui.settings.statistics.product

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.KEY_COUNTER
import com.inno.coffee.utilities.KEY_DAY_COUNTER
import com.inno.coffee.utilities.KEY_TOTAL_COUNTER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticProductActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.extras?.getString(KEY_COUNTER) ?: KEY_DAY_COUNTER
        setContent {
            CoffeeTheme {
                when (index) {
                    KEY_DAY_COUNTER -> {
                        StatisticDayProductLayout() {
                            this@StatisticProductActivity.finish()
                        }
                    }
                    KEY_TOTAL_COUNTER -> {
                        StatisticTotalProductLayout() {
                            this@StatisticProductActivity.finish()
                        }
                    }
                }
            }
        }
    }

}