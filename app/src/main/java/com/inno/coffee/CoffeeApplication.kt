package com.inno.coffee

import android.app.Application
import com.inno.coffee.ui.notice.GlobalDialogManager
import com.inno.coffee.ui.presentation.PresentationDisplayManager
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
        init()
        delayInit()
    }

    private fun init() {
        Logger.d(TAG, "CoffeeApplication init() call")
        CoffeeSharedPreferences.init(this)
        PresentationDisplayManager.init(this)
    }

    private fun delayInit() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            Logger.d(TAG, "CoffeeApplication delayInit start")
            GlobalDialogManager.init(this@CoffeeApplication)
        }
    }

}