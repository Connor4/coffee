package com.inno.coffee.data

import android.content.Context
import android.content.SharedPreferences

/**
 * 全局sp单例管理类
 */
class PreferencesManager private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "coffee_prefs"
        private const val KEY_FIRST_LAUNCH = "first_launch"

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

    var isFirstLaunch: Boolean
        get() = mSharedPreferences.getBoolean(KEY_FIRST_LAUNCH, false)
        set(value) = mSharedPreferences.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()


}