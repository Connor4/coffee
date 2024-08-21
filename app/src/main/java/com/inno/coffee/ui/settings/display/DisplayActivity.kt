package com.inno.coffee.ui.settings.display

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayMain()
        }
    }

}