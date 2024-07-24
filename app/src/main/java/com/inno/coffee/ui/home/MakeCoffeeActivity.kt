package com.inno.coffee.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint


fun launchMakeCoffeeActivity(context: Context) {
    context.startActivity(Intent(context, MakeCoffeeActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
    (context as? Activity)?.finish()
}


/**
 * 饮品制作页
 */
@AndroidEntryPoint
class MakeCoffeeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                MakeCoffeeContent()
            }
        }
    }

}
