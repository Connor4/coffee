package com.inno.coffee

import android.app.Application
import com.inno.coffee.function.clean.CleanManager
import com.inno.coffee.function.defaultsetting.DefaultSettingManager
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.function.formula.ProductProfileManager
import com.inno.coffee.function.statistic.StatisticManager
import com.inno.coffee.ui.notice.ErrorDataManager
import com.inno.coffee.ui.notice.GlobalDialogLeftManager
import com.inno.coffee.ui.notice.GlobalDialogRightManager
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.Logger
import com.inno.serialport.function.CommunicationController
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class CoffeeApplication : Application() {
    companion object {
        private const val TAG = "CoffeeApplication"
    }

    private val applicationScope = CoroutineScope(Dispatchers.IO)
    @Inject
    lateinit var dataStore: CoffeeDataStore

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        Logger.d(TAG, "CoffeeApplication init() call")
        CoffeeSharedPreferences.init(this)
        ScreenDisplayManager.init(this)

        applicationScope.launch {
            Logger.d(TAG, "CoffeeApplication init() launch call")
            CommunicationController.instance.init()

            ErrorDataManager.init()
            GlobalDialogLeftManager.init(this@CoffeeApplication)
            GlobalDialogRightManager.init(this@CoffeeApplication)

//            delay(3000)
            Logger.d(TAG, "CoffeeApplication delayInit start")
            ProductProfileManager.init(this@CoffeeApplication)
            DefaultSettingManager.init(this@CoffeeApplication)
            StatisticManager.init(this@CoffeeApplication)
            CleanManager.initialize(dataStore, this@CoffeeApplication)

            ProductProfileManager.readFormulaFromAssets(this@CoffeeApplication)
        }
    }

}