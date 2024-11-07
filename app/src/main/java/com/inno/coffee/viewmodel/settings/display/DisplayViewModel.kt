package com.inno.coffee.viewmodel.settings.display

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.DISPLAY_COLOR_MIX
import com.inno.coffee.utilities.DISPLAY_PER_PAGE_COUNT_12
import com.inno.coffee.utilities.INDEX_AUTO_BACK_TO_FIRST_PAGE
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_COLOR
import com.inno.coffee.utilities.INDEX_NUMBER_OF_PRODUCT_PER_PAGE
import com.inno.coffee.utilities.INDEX_SCREEN_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_SHOW_EXTRACTION_TIME
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.common.utils.SystemLocaleHelper
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val TAG = "DisplayViewModel"
    private val BACK_TO_FIRST_PAGE = "back_to_first_page"
    private val NUMBER_OF_PRODUCT_PER_PAGE = "number_of_product_per_page"
    private val FRONT_LIGHT_COLOR = "front_light_color"
    private val FRONT_LIGHT_BRIGHTNESS = "front_light_brightness"
    private val SCREEN_BRIGHTNESS = "screen_brightness"
    private val SHOW_EXTRACTION_TIME = "show_extraction_time"

    private val _language = MutableStateFlow("")
    val language: StateFlow<String> = _language
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    private val _backToFirstPage = MutableStateFlow(false)
    val backToFirstPage: StateFlow<Boolean> = _backToFirstPage
    private val _numberOfProductPerPage = MutableStateFlow(DISPLAY_PER_PAGE_COUNT_12)
    val numberOfProductPerPage: StateFlow<Int> = _numberOfProductPerPage
    private val _frontLightColor = MutableStateFlow(DISPLAY_COLOR_MIX)
    val frontLightColor: StateFlow<Int> = _frontLightColor
    private val _frontLightBrightness = MutableStateFlow(0)
    val frontLightBrightness: StateFlow<Int> = _frontLightBrightness
    private val _screenBrightness = MutableStateFlow(0)
    val screenBrightness: StateFlow<Int> = _screenBrightness

    private val _showExtractionTime = MutableStateFlow(true)
    val showExtractionTime: StateFlow<Boolean> = _showExtractionTime

    fun initGroup() {
        viewModelScope.launch {
            val systemLanguage = dataStore.getSystemLanguage()
            val language = Locale.forLanguageTag(systemLanguage).language
            val autoBackToFirstPage = dataStore.getCoffeePreference(BACK_TO_FIRST_PAGE, false)
            val numberOfProductPerPage =
                dataStore.getCoffeePreference(NUMBER_OF_PRODUCT_PER_PAGE, 12)
            val frontLightColor = dataStore.getCoffeePreference(FRONT_LIGHT_COLOR, 1)
            val frontLightBrightness = dataStore.getCoffeePreference(FRONT_LIGHT_BRIGHTNESS, 90)
            val screenBrightness = dataStore.getCoffeePreference(SCREEN_BRIGHTNESS, 90)
            val showExtractionTime = dataStore.getCoffeePreference(SHOW_EXTRACTION_TIME, true)

            _language.value = systemLanguage
            _time.value = TimeUtils.getNowTimeInYearAndHour(language = language)
            _backToFirstPage.value = autoBackToFirstPage
            _numberOfProductPerPage.value = numberOfProductPerPage
            _frontLightColor.value = frontLightColor
            _frontLightBrightness.value = frontLightBrightness
            _screenBrightness.value = screenBrightness
            _showExtractionTime.value = showExtractionTime
        }
    }

    suspend fun getLanguage(): String {
        val language = dataStore.getSystemLanguage()
        Logger.d(TAG, "getLanguage() language = $language")
        return language
    }

    fun selectLanguage(context: Context, locale: Locale) {
        viewModelScope.launch(defaultDispatcher) {
            SystemLocaleHelper.changeSystemLocale(context, locale.language)
            val languageTag = locale.toLanguageTag()
            dataStore.saveSystemLanguage(languageTag)
            _language.value = languageTag
            Logger.d(TAG, "selectLanguage() language = ${_language.value}")
        }
    }

    fun setSystemTime(context: Context, date: Long, hour: Int, min: Int) {
        viewModelScope.launch(defaultDispatcher) {
            TimeUtils.setDateAndTime(context, date, hour, min)
            _time.value = TimeUtils.getNowTimeInYearAndHour(language = dataStore
                .getSystemLanguage()
            )
            Logger.d(TAG, "setSystemTime() called time ${_time.value}")
        }
    }

    fun saveDisplayGroupTwoValue(key: Int, value: Any) {
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                INDEX_AUTO_BACK_TO_FIRST_PAGE -> {
                    dataStore.saveCoffeePreference(BACK_TO_FIRST_PAGE, value as Boolean)
                    _backToFirstPage.value = value
                }
                INDEX_NUMBER_OF_PRODUCT_PER_PAGE -> {
                    dataStore.saveCoffeePreference(NUMBER_OF_PRODUCT_PER_PAGE, value as Int)
                    _numberOfProductPerPage.value = value
                }
                INDEX_FRONT_LIGHT_COLOR -> {
                    dataStore.saveCoffeePreference(FRONT_LIGHT_COLOR, value as Int)
                    _frontLightColor.value = value
                }
                INDEX_FRONT_LIGHT_BRIGHTNESS -> {
                    dataStore.saveCoffeePreference(FRONT_LIGHT_BRIGHTNESS, value as Int)
                    _frontLightBrightness.value = value
                }
                INDEX_SCREEN_BRIGHTNESS -> {
                    dataStore.saveCoffeePreference(SCREEN_BRIGHTNESS, value as Int)
                    _screenBrightness.value = value
                }
                INDEX_SHOW_EXTRACTION_TIME -> {
                    dataStore.saveCoffeePreference(SHOW_EXTRACTION_TIME, value as Boolean)
                    _showExtractionTime.value = value
                }
            }
        }
    }

}