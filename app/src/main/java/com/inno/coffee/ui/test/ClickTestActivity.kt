package com.inno.coffee.ui.test

import android.os.Bundle
import android.widget.Button
import com.inno.coffee.R
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.common.utils.Logger

class ClickTestActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.click_layout)
        findViewById<Button>(R.id.test_button).setOnClickListener {
            Logger.d("onCreate() called")
        }
    }
}