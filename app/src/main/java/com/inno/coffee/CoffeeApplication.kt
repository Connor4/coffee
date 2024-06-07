package com.inno.coffee

import android.app.Application
import com.inno.coffee.data.settings.DrinksHistoryRepository
import com.inno.common.db.CoffeeRoomDatabase
import com.inno.common.utils.Logger
import com.inno.common.utils.PreferencesManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class CoffeeApplication : Application() {
    companion object {
        private const val TAG = "CoffeeApplication"
    }

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { CoffeeRoomDatabase.getDatabase(this, applicationScope) }
    private val repository by lazy { DrinksHistoryRepository(database.drinksHistoryDao()) }

    override fun onCreate() {
        super.onCreate()
        Logger.d(TAG, "onCreate() called")
        init()
        delayInit()
        dependenceInit()
    }

    private fun init() {
        PreferencesManager.getInstance(this)
        Logger.isDebuggable(true)
    }

    private fun delayInit() {

    }

    private fun dependenceInit() {

    }

}