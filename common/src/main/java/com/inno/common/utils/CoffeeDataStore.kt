package com.inno.common.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

object CoffeeDataStore {

    private const val USER_PREFERENCES_NAME = "settings"

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    private const val FIRST_INSTALL_KEY = "first_install"
    private const val MACHINE_LANGUAGE = "machine_language"

    suspend fun getFirstInstall(context: Context): Boolean {
        return getCoffeePreference(context, FIRST_INSTALL_KEY, false)
    }

    suspend fun saveFirstInstall(context: Context) {
        saveCoffeePreference(context, FIRST_INSTALL_KEY, false)
    }

    suspend fun getMachineLanguage(context: Context): String {
        return getCoffeePreference(context, MACHINE_LANGUAGE, "English")
    }

    suspend fun saveMachineLanguage(context: Context, language: String) {
        saveCoffeePreference(context, MACHINE_LANGUAGE, language)
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T> getCoffeePreference(context: Context, key: String, defaultValue: T): T {
        val preferences = context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()
        return when (defaultValue) {
            is Boolean -> preferences[booleanPreferencesKey(key)] as T? ?: defaultValue
            is Int -> preferences[intPreferencesKey(key)] as T? ?: defaultValue
            is Long -> preferences[longPreferencesKey(key)] as T? ?: defaultValue
            is Float -> preferences[floatPreferencesKey(key)] as T? ?: defaultValue
            is String -> preferences[stringPreferencesKey(key)] as T? ?: defaultValue
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    private suspend fun <T> saveCoffeePreference(context: Context, key: String, value: T) {
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
