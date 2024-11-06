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
        private const val DEFAULT_LAST_RESET_PRODUCT_TIME = 1704038400000 // 2024/01/01
        private const val BACK_TO_FIRST_PAGE = "back_to_first_page"
        private const val NUMBER_OF_PRODUCT_PER_PAGE = "number_of_product_per_page"
        private const val FRONT_LIGHT_COLOR = "front_light_color"
        private const val FRONT_LIGHT_BRIGHTNESS = "front_light_brightness"
        private const val SCREEN_BRIGHTNESS = "screen_brightness"
        private const val SHOW_EXTRACTION_TIME = "show_extraction_time"
        private const val LOW_POWER_MODE = "low_power_mode"
        private const val MACHINE_TYPE = "machine_type"
        private const val TEMPERATURE_UNIT = "temperature_unit"
        private const val OPERATION_MODE = "operation_mode"
        private const val SMART_MODE = "smart_mode"
        private const val WATER_TANK_SURVEILLANCE = "water_tank_surveillance"
        private const val DIFFERENT_BOILER = "different_boiler"
        private const val AMERICANO_TEMP_ADJUST = "americano_temp_adjust"
        private const val HOT_WATER_OUTPUT = "hot_water_output"
        private const val STEAM_WAND_POSITION = "steam_wand_position"

        private const val COFFEE_BOILER_TEMP = "coffee_boiler_temp"
        private const val COLD_RINSE = "code_rinse"
        private const val WARM_RINSE = "warm_rinse"
        private const val GROUNDS_QUANTITY = "grounds_quantity"
        private const val BREW_BALANCE = "brew_balance"
        private const val BREW_PRE_HEATING = "brew_pre_heating"
        private const val GRINDER_PURGE_FUNCTION = "grinder_purge_function"
        private const val NUMBER_OF_CYCLES_RINSE = "number_of_cycles_rinse"
        private const val STEAM_BOILER_PRESSURE = "steam_boiler_pressure"
        private const val NTC_LEFT = "ntc_left"
        private const val NTC_RIGHT = "ntc_right"
    }

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    suspend fun getCoffeeBoilerTemp(): Int {
        return getCoffeePreference(COFFEE_BOILER_TEMP, 90)
    }

    suspend fun saveCoffeeBoilerTemp(coffeeBoilerTemp: Int) {
        saveCoffeePreference(COFFEE_BOILER_TEMP, coffeeBoilerTemp)
    }

    suspend fun getColdRinseQuantity(): Int {
        return getCoffeePreference(COLD_RINSE, 500)
    }

    suspend fun saveColdRinseQuantity(coldRinseQuantity: Int) {
        saveCoffeePreference(COLD_RINSE, coldRinseQuantity)
    }

    suspend fun getWarmRinseQuantity(): Int {
        return getCoffeePreference(WARM_RINSE, 100)
    }

    suspend fun saveWarmRinseQuantity(warmRinseQuantity: Int) {
        saveCoffeePreference(WARM_RINSE, warmRinseQuantity)
    }

    suspend fun getGroundsDrawerQuantity(): Int {
        return getCoffeePreference(GROUNDS_QUANTITY, 0)
    }

    suspend fun saveGroundsDrawerQuantity(groundsDrawerQuantity: Int) {
        saveCoffeePreference(GROUNDS_QUANTITY, groundsDrawerQuantity)
    }

    suspend fun getBrewGroupLoadBalancing(): Boolean {
        return getCoffeePreference(BREW_BALANCE, false)
    }

    suspend fun saveBrewGroupLoadBalancing(brewGroupLoadBalancing: Boolean) {
        saveCoffeePreference(BREW_BALANCE, brewGroupLoadBalancing)
    }

    suspend fun getBrewGroupPreHeating(): Int {
        return getCoffeePreference(BREW_PRE_HEATING, 0)
    }

    suspend fun saveBrewGroupPreHeating(brewGroupPreHeating: Int) {
        saveCoffeePreference(BREW_PRE_HEATING, brewGroupPreHeating)
    }

    suspend fun getGrinderPurgeFunction(): Int {
        return getCoffeePreference(GRINDER_PURGE_FUNCTION, 0)
    }

    suspend fun saveGrinderPurgeFunction(grinderPurgeFunction: Int) {
        saveCoffeePreference(GRINDER_PURGE_FUNCTION, grinderPurgeFunction)
    }

    suspend fun getNumberOfCyclesRinse(): Int {
        return getCoffeePreference(NUMBER_OF_CYCLES_RINSE, 0)
    }

    suspend fun saveNumberOfCyclesRinse(numberOfCyclesRinse: Int) {
        saveCoffeePreference(NUMBER_OF_CYCLES_RINSE, numberOfCyclesRinse)
    }

    suspend fun getSteamBoilerPressure(): Int {
        return getCoffeePreference(STEAM_BOILER_PRESSURE, 1)
    }

    suspend fun saveSteamBoilerPressure(steamBoilerPressure: Int) {
        saveCoffeePreference(STEAM_BOILER_PRESSURE, steamBoilerPressure)
    }

    suspend fun getNtcCorrectionSteamLeft(): Int {
        return getCoffeePreference(NTC_LEFT, 0)
    }

    suspend fun saveNtcCorrectionSteamLeft(ntcCorrectionSteamLeft: Int) {
        saveCoffeePreference(NTC_LEFT, ntcCorrectionSteamLeft)
    }

    suspend fun getNtcCorrectionSteamRight(): Int {
        return getCoffeePreference(NTC_RIGHT, 0)
    }

    suspend fun saveNtcCorrectionSteamRight(ntcCorrectionSteamRight: Int) {
        saveCoffeePreference(NTC_RIGHT, ntcCorrectionSteamRight)
    }

    suspend fun getAmericanoTempAdjust(): Int {
        return getCoffeePreference(AMERICANO_TEMP_ADJUST, 0)
    }

    suspend fun saveAmericanoTempAdjust(americanoTempAdjust: Int) {
        saveCoffeePreference(AMERICANO_TEMP_ADJUST, americanoTempAdjust)
    }

    suspend fun getHotWaterOutput(): Int {
        return getCoffeePreference(HOT_WATER_OUTPUT, 0)
    }

    suspend fun saveHotWaterOutput(hotWaterOutput: Int) {
        saveCoffeePreference(HOT_WATER_OUTPUT, hotWaterOutput)
    }

    suspend fun getDifferentBoiler(): Boolean {
        return getCoffeePreference(DIFFERENT_BOILER, false)
    }

    suspend fun saveDifferentBoiler(differentBoiler: Boolean) {
        saveCoffeePreference(DIFFERENT_BOILER, differentBoiler)
    }

    suspend fun getSteamWandPosition(): Int {
        return getCoffeePreference(STEAM_WAND_POSITION, 0)
    }

    suspend fun saveSteamWandPosition(steamWandPosition: Int) {
        saveCoffeePreference(STEAM_WAND_POSITION, steamWandPosition)
    }

    suspend fun getWaterTankSurveillance(): Boolean {
        return getCoffeePreference(WATER_TANK_SURVEILLANCE, false)
    }

    suspend fun saveWaterTankSurveillance(waterTankSurveillance: Boolean) {
        saveCoffeePreference(WATER_TANK_SURVEILLANCE, waterTankSurveillance)
    }

    suspend fun getSmartMode(): Int {
        return getCoffeePreference(SMART_MODE, 0)
    }

    suspend fun saveSmartMode(smartMode: Int) {
        saveCoffeePreference(SMART_MODE, smartMode)
    }

    suspend fun getOperationMode(): Int {
        return getCoffeePreference(OPERATION_MODE, 0)
    }

    suspend fun saveOperationMode(operationMode: Int) {
        saveCoffeePreference(OPERATION_MODE, operationMode)
    }

    fun getTemperatureUnitFlow(): Flow<Boolean> {
        return getCoffeePreferenceFlow(TEMPERATURE_UNIT, false)
    }

    suspend fun getTemperatureUnit(): Boolean {
        return getCoffeePreference(TEMPERATURE_UNIT, false)
    }

    suspend fun saveTemperatureUnit(temperatureUnit: Boolean) {
        saveCoffeePreference(TEMPERATURE_UNIT, temperatureUnit)
    }

    suspend fun getMachineType(): String {
        return getCoffeePreference(MACHINE_TYPE, "CM01")
    }

    suspend fun saveMachineType(machineType: String) {
        saveCoffeePreference(MACHINE_TYPE, machineType)
    }

    suspend fun getLowPowerMode(): Boolean {
        return getCoffeePreference(LOW_POWER_MODE, false)
    }

    suspend fun saveLowPowerMode(low: Boolean) {
        saveCoffeePreference(LOW_POWER_MODE, low)
    }

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

    suspend fun getFrontLightColor(): Int {
        return getCoffeePreference(FRONT_LIGHT_COLOR, 1)
    }

    suspend fun saveFrontLightColor(frontLightColor: Int) {
        saveCoffeePreference(FRONT_LIGHT_COLOR, frontLightColor)
    }

    suspend fun getNumberOfProductPerPage(): Int {
        return getCoffeePreference(NUMBER_OF_PRODUCT_PER_PAGE, 12)
    }

    suspend fun saveNumberOfProductPerPage(numberOfProductPerPage: Int) {
        saveCoffeePreference(NUMBER_OF_PRODUCT_PER_PAGE, numberOfProductPerPage)
    }

    fun getShowExtractionTimeFlow(): Flow<Boolean> {
        return getCoffeePreferenceFlow(SHOW_EXTRACTION_TIME, true)
    }

    suspend fun getShowExtractionTime(): Boolean {
        return getCoffeePreference(SHOW_EXTRACTION_TIME, true)
    }

    suspend fun saveShowExtractionTime(showExtractionTime: Boolean) {
        saveCoffeePreference(SHOW_EXTRACTION_TIME, showExtractionTime)
    }

    fun getBackToFirstPageFlow(): Flow<Boolean> {
        return getCoffeePreferenceFlow(BACK_TO_FIRST_PAGE, false)
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
    private fun <T> getCoffeePreferenceFlow(key: String, defaultValue: T): Flow<T> {
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
