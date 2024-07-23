package com.inno.coffee.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.Display
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.inno.coffee.data.home.HomeViewModel
import com.inno.coffee.ui.presentation.DisplayPresentation
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
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                MakeCoffeeContent(viewModel)

                val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
                val displayCategory = DisplayManager.DISPLAY_CATEGORY_PRESENTATION
                val displays: Array<Display> = displayManager.getDisplays(displayCategory)
                val presentation = DisplayPresentation(this@MakeCoffeeActivity, displays[0]) {
                    MakeCoffeeContent(viewModel)
                }
                presentation.show()
            }
        }
    }

}
