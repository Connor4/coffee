package com.inno.coffee.ui.settings.statistics

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchStatisticActivity(context: Context) {
    context.startActivity(Intent(context, StatisticActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
}


@AndroidEntryPoint
class StatisticActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                StatisticsMainScreen()
            }
        }
    }
}