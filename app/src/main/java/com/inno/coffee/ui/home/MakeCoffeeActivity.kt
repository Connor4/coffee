package com.inno.coffee.ui.home

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Display
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.presentation.PresentationDisplayManager
import com.inno.coffee.ui.settings.statistics.StatisticActivity
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
class MakeCoffeeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PresentationDisplayManager.init(this)
        setContent {
            CoffeeTheme {
                MakeCoffeeContent()
            }
        }

        displaySecond()
    }

    private fun displaySecond() {
        val display = PresentationDisplayManager.getDisplay()
        val options = ActivityOptions.makeBasic().apply {
            launchDisplayId = display?.displayId ?: Display.DEFAULT_DISPLAY
        }
        val intent = Intent(this, StatisticActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent, options.toBundle())
    }

}
