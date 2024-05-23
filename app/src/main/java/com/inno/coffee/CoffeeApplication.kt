package com.inno.coffee

import android.app.Application
import com.inno.coffee.utils.PreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoffeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
        delayInit()
        dependenceInit()
    }

    private fun init() {
        PreferencesManager.getInstance(this)
    }

    private fun delayInit() {

    }

    private fun dependenceInit() {

    }

}