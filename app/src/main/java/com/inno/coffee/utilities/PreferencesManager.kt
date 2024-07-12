package com.inno.coffee.utilities

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "coffee_prefs"
        private const val KEY_FIRST_INSTALL = "first_install"

        @Volatile
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager =
            instance ?: synchronized(this) {
                instance ?: PreferencesManager(context.applicationContext).also { instance = it }
            }
    }

    private val mSharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun remove(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }

    fun clearAll() {
        mSharedPreferences.edit().clear().apply()
    }

    var isFirstInstall: Boolean
        get() = mSharedPreferences.getBoolean(KEY_FIRST_INSTALL, true)
        set(value) = mSharedPreferences.edit().putBoolean(KEY_FIRST_INSTALL, value).apply()


}