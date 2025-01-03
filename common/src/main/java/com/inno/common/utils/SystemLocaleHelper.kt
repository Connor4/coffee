package com.inno.common.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.backup.BackupManager
import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale

object SystemLocaleHelper {

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    fun changeSystemLocale(context: Context, language: String) {
        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val amClass = Class.forName("android.app.ActivityManager")
            val getServiceMethod = amClass.getDeclaredMethod("getService")
            getServiceMethod.isAccessible = true
            val activityManagerNative = getServiceMethod.invoke(am)

            val configuration = Configuration()
            val localeParts = language.split("_")
            val locale = if (localeParts.size == 2) {
                Locale(localeParts[0], localeParts[1])
            } else {
                Locale(localeParts[0])
            }
            val localeList = LocaleList(locale)
            configuration.setLocales(localeList)

            val userSetLocaleField = configuration.javaClass.getDeclaredField("userSetLocale")
            userSetLocaleField.isAccessible = true
            userSetLocaleField.setBoolean(configuration, true)

            val updatePersistentConfigurationMethod =
                activityManagerNative.javaClass.getDeclaredMethod(
                    "updatePersistentConfiguration",
                    Configuration::class.java,
                )
            updatePersistentConfigurationMethod.invoke(activityManagerNative, configuration)

            BackupManager.dataChanged("com.android.providers.settings")
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("SystemLocaleHelper", "changeSystemLocale: $e")
        }
    }

}