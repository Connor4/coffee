package com.inno.common.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES_NAME = "settings"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

val FIRST_INSTALL_KEY = booleanPreferencesKey("first_install")

suspend fun Context.readFirstInstall(): Boolean {
    return dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[FIRST_INSTALL_KEY] ?: true
        }.first()
}

suspend fun Context.saveFirstInstall(isFirstInstall: Boolean) {
    dataStore.edit { preferences ->
        preferences[FIRST_INSTALL_KEY] = isFirstInstall
    }
}