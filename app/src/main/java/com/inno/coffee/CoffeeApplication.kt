package com.inno.coffee

import android.app.Application
import com.inno.coffee.ui.notice.GlobalDialogManager
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.Logger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltAndroidApp
class CoffeeApplication : Application() {
    companion object {
        private const val TAG = "CoffeeApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Logger.d(TAG, "onCreate() called")
        init()
        delayInit()
    }

    private fun init() {

        CoffeeSharedPreferences.init(this)
    }

    private fun delayInit() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            GlobalDialogManager.init(this@CoffeeApplication)
        }
    }

}