package com.inno.common.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoffeeDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val TAG = "CoffeeDataStore"
        private const val USER_PREFERENCES_NAME = "settings"
        private const val SYSTEM_LANGUAGE = "system_language"
        private const val DEFAULT_LANGUAGE_VALUE = "en"
        private const val LAST_RESET_PRODUCT_TIME = "last_reset_product_time"
        private const val DEFAULT_LAST_RESET_PRODUCT_TIME = "" // 2024/01/01
    }

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    suspend fun getLastResetProductTime(): String {
        return getCoffeePreference(LAST_RESET_PRODUCT_TIME, DEFAULT_LAST_RESET_PRODUCT_TIME)
    }

    suspend fun saveLastResetProductTime(time: String) {
        saveCoffeePreference(LAST_RESET_PRODUCT_TIME, time)
    }

    suspend fun getSystemLanguage(): String {
        return getCoffeePreference(SYSTEM_LANGUAGE, DEFAULT_LANGUAGE_VALUE)
    }

    suspend fun saveSystemLanguage(language: String) {
        saveCoffeePreference(SYSTEM_LANGUAGE, language)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getCoffeePreferenceFlow(key: String, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            when (defaultValue) {
                is Boolean -> preferences[booleanPreferencesKey(key)] as T? ?: defaultValue
                is Int -> preferences[intPreferencesKey(key)] as T? ?: defaultValue
                is Long -> preferences[longPreferencesKey(key)] as T? ?: defaultValue
                is Float -> preferences[floatPreferencesKey(key)] as T? ?: defaultValue
                is String -> preferences[stringPreferencesKey(key)] as T? ?: defaultValue
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> getCoffeePreference(key: String, defaultValue: T): T {
        Logger.d(TAG, "getCoffeePreference() called with: key = $key, defaultValue = $defaultValue")
        val preferences = context.dataStore.data.first()
        return when (defaultValue) {
            is Boolean -> preferences[booleanPreferencesKey(key)] as T? ?: defaultValue
            is Int -> preferences[intPreferencesKey(key)] as T? ?: defaultValue
            is Long -> preferences[longPreferencesKey(key)] as T? ?: defaultValue
            is Float -> preferences[floatPreferencesKey(key)] as T? ?: defaultValue
            is String -> preferences[stringPreferencesKey(key)] as T? ?: defaultValue
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    suspend fun <T> saveCoffeePreference(key: String, value: T) {
        Logger.d(TAG, "saveCoffeePreference() called with: key = $key, value = $value")
        context.dataStore.edit { preferences ->
            when (value) {
                is Boolean -> preferences[booleanPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is Long -> preferences[longPreferencesKey(key)] = value
                is Float -> preferences[floatPreferencesKey(key)] = value
                is String -> preferences[stringPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }
}
