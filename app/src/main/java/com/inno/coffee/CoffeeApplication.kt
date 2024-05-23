package com.inno.coffee

import android.app.Application
import com.inno.coffee.data.PreferencesManager

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