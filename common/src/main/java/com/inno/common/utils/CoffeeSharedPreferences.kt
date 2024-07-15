package com.inno.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class CoffeeSharedPreferences private constructor(context: Context) {

    @SuppressLint("StaticFieldLeak")
    companion object {
        private const val PREFS_NAME = "coffee_prefs"
        private const val KEY_FIRST_INSTALL = "first_install"

        @Volatile
        private var instance: CoffeeSharedPreferences? = null
        private var context: Context? = null

        fun init(ctx: Context) {
            context = ctx
            getInstance()
        }

        fun getInstance(): CoffeeSharedPreferences =
            instance ?: synchronized(this) {
                instance ?: CoffeeSharedPreferences(context!!).also { instance = it }
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