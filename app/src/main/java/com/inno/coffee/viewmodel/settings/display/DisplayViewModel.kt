package com.inno.coffee.viewmodel.settings.display

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
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
    private val _language = MutableStateFlow("")
    val language: StateFlow<String> = _language
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    fun initGroupOne() {
        viewModelScope.launch {
            val systemLanguage = dataStore.getSystemLanguage()
            _language.value = systemLanguage
            Logger.d(TAG, "initGroupOne() systemLanguage = $systemLanguage")
            val language = Locale.forLanguageTag(systemLanguage).language
            _time.value = TimeUtils.getNowTimeInYearAndHour(language = language)
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
        viewModelScope.launch {
            TimeUtils.setDateAndTime(context, date, hour, min)
            _time.value = TimeUtils.getNowTimeInYearAndHour(language = dataStore
                .getSystemLanguage())
            Logger.d(TAG, "setSystemTime() called time ${_time.value}")
        }
    }

}