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
        private const val SYSTEM_LANGUAGE = "system_language"
        private const val DEFAULT_LANGUAGE_VALUE = "en"
        private const val LAST_RESET_PRODUCT_TIME = "last_reset_product_time"
        private const val DEFAULT_LAST_RESET_PRODUCT_TIME = 1704038400000 // 2024/01/01
        private const val BACK_TO_FIRST_PAGE = "back_to_first_page"
        private const val NUMBER_OF_PRODUCT_PER_PAGE = "number_of_product_per_page"
        private const val FRONT_LIGHT_COLOR = "front_light_color"
        private const val FRONT_LIGHT_BRIGHTNESS = "front_light_brightness"
        private const val SCREEN_BRIGHTNESS = "screen_brightness"
        private const val SHOW_EXTRACTION_TIME = "show_extraction_time"
        private const val SHOW_PRODUCT_NAME = "show_product_name"
    }

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    suspend fun getScreenBrightness(): Int {
        return getCoffeePreference(SCREEN_BRIGHTNESS, 90)
    }

    suspend fun saveScreenBrightness(screenBrightness: Int) {
        saveCoffeePreference(SCREEN_BRIGHTNESS, screenBrightness)
    }

    suspend fun getFrontLightBrightness(): Int {
        return getCoffeePreference(FRONT_LIGHT_BRIGHTNESS, 90)
    }

    suspend fun saveFrontLightBrightness(frontLightBrightness: Int) {
        saveCoffeePreference(FRONT_LIGHT_BRIGHTNESS, frontLightBrightness)
    }

    suspend fun getFrontLightColor(): String {
        return getCoffeePreference(FRONT_LIGHT_COLOR, "AUTO")
    }

    suspend fun saveFrontLightColor(frontLightColor: String) {
        saveCoffeePreference(FRONT_LIGHT_COLOR, frontLightColor)
    }

    suspend fun getNumberOfProductPerPage(): Int {
        return getCoffeePreference(NUMBER_OF_PRODUCT_PER_PAGE, 12)
    }

    suspend fun saveNumberOfProductPerPage(numberOfProductPerPage: Int) {
        saveCoffeePreference(NUMBER_OF_PRODUCT_PER_PAGE, numberOfProductPerPage)
    }

    suspend fun getShowProductName(): Boolean {
        return getCoffeePreference(SHOW_PRODUCT_NAME, true)
    }

    suspend fun saveShowProductName(showProductName: Boolean) {
        saveCoffeePreference(SHOW_PRODUCT_NAME, showProductName)
    }

    suspend fun getShowExtractionTime(): Boolean {
        return getCoffeePreference(SHOW_EXTRACTION_TIME, true)
    }

    suspend fun saveShowExtractionTime(showExtractionTime: Boolean) {
        saveCoffeePreference(SHOW_EXTRACTION_TIME, showExtractionTime)
    }

    suspend fun getBackToFirstPage(): Boolean {
        return getCoffeePreference(BACK_TO_FIRST_PAGE, false)
    }

    suspend fun saveBackToFirstPage(backToFirstPage: Boolean) {
        saveCoffeePreference(BACK_TO_FIRST_PAGE, backToFirstPage)
    }

    suspend fun getLastResetProductTime(): Long {
        return getCoffeePreference(LAST_RESET_PRODUCT_TIME, DEFAULT_LAST_RESET_PRODUCT_TIME)
    }

    suspend fun saveLastResetProductTime(time: Long) {
        saveCoffeePreference(LAST_RESET_PRODUCT_TIME, time)
    }

    suspend fun getSystemLanguage(): String {
        return getCoffeePreference(SYSTEM_LANGUAGE, DEFAULT_LANGUAGE_VALUE)
    }

    suspend fun saveSystemLanguage(language: String) {
        saveCoffeePreference(SYSTEM_LANGUAGE, language)
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
