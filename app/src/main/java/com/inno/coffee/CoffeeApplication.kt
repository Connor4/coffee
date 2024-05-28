package com.inno.coffee

import android.app.Application
import com.inno.common.utils.Logger
import com.inno.common.utils.PreferencesManager
import dagger.hilt.android.HiltAndroidApp

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