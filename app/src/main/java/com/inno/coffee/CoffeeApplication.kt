package com.inno.coffee

import android.app.Application
import com.inno.coffee.function.defaultsetting.DefaultSettingManager
import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.function.presentation.PresentationDisplayManager
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

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        Logger.d(TAG, "CoffeeApplication init() call")
        CoffeeSharedPreferences.init(this)
        PresentationDisplayManager.init(this)

        applicationScope.launch {
            Logger.d(TAG, "CoffeeApplication init() launch call")
//            SerialPortDataManager.instance.open()
//            DataCenter.init()

            delay(3000)
            Logger.d(TAG, "CoffeeApplication delayInit start")
            GlobalDialogManager.init(this@CoffeeApplication)
            ProductProfileManager.init(this@CoffeeApplication)
            DefaultSettingManager.init(this@CoffeeApplication)
        }
    }

}