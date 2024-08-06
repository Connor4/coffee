package com.inno.coffee.ui.settings.statistics.product

import android.os.Bundle
import com.inno.coffee.R
import com.inno.coffee.ui.base.CoffeeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticProductActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistic_product_activity)
    }

}