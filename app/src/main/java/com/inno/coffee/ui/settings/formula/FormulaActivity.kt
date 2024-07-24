package com.inno.coffee.ui.settings.formula

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchFormulaActivity(context: Context) {
    context.startActivity(Intent(context, FormulaActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
}

@AndroidEntryPoint
class FormulaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {

            }
        }
    }
}