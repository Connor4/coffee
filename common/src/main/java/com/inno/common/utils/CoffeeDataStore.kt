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
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CoffeeDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val TAG = "CoffeeDataStore"
        private const val USER_PREFERENCES_NAME = "settings"
        private const val MACHINE_LANGUAGE = "machine_language"
        private const val DEFAULT_LANGUAGE_VALUE = "en"
        private const val COFFEE_COUNT = "coffee_count"
        private const val HOT_WATER_COUNT = "hot_water_count"
        private const val MILK_COUNT = "milk_count"
        private const val FOAM_COUNT = "foam_count"
        private const val STEAM_COUNT = "steam_count"
        private const val TOTAL_COUNT = "total_count"
        private const val DEFAULT_COUNT_VALUE = 0
    }

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    suspend fun addCoffeeCount() {
        var count = getCoffeePreference(COFFEE_COUNT, 0)
        saveCoffeePreference(COFFEE_COUNT, ++count)
    }

    suspend fun addHotWaterCount() {
        var count = getCoffeePreference(HOT_WATER_COUNT, 0)
        saveCoffeePreference(HOT_WATER_COUNT, ++count)
    }

    suspend fun addMilkCount() {
        var count = getCoffeePreference(MILK_COUNT, 0)
        saveCoffeePreference(MILK_COUNT, ++count)
    }

    suspend fun addFoamCount() {
        var count = getCoffeePreference(FOAM_COUNT, 0)
        saveCoffeePreference(FOAM_COUNT, ++count)
    }

    suspend fun addSteamCount() {
        var count = getCoffeePreference(STEAM_COUNT, 0)
        saveCoffeePreference(STEAM_COUNT, ++count)
    }

    suspend fun getMachineLanguage(): String {
        return getCoffeePreference(MACHINE_LANGUAGE, DEFAULT_LANGUAGE_VALUE)
    }

    suspend fun saveMachineLanguage(language: String) {
        saveCoffeePreference(MACHINE_LANGUAGE, language)
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T> getCoffeePreference(key: String, defaultValue: T): T {
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

    private suspend fun <T> saveCoffeePreference(key: String, value: T) {
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
