package com.inno.coffee.ui.base

import androidx.activity.ComponentActivity

open class CoffeeActivity : ComponentActivity() {

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}