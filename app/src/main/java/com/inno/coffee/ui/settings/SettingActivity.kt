package com.inno.coffee.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchSettingActivity(context: Context) {
    context.startActivity(Intent(context, SettingActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
}

/**
 * 后台操作页面
 */
@AndroidEntryPoint
class SettingActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                SettingCardLayout()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}

