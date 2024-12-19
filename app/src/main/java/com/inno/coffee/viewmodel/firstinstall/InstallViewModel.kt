package com.inno.coffee.viewmodel.firstinstall

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.defaultsetting.DefaultSettingManager
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.SystemLocaleHelper
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InstallViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val MAINTENANCE_DATE = "maintenance_date"
    }

    fun selectLanguage(context: Context, locale: Locale) {
        viewModelScope.launch(defaultDispatcher) {
            SystemLocaleHelper.changeSystemLocale(context, locale.language)
            dataStore.saveSystemLanguage(locale.toLanguageTag())
        }
    }

    fun finishSetting(context: Context, date: Long, hour: Int, min: Int) {
        viewModelScope.launch(defaultDispatcher) {
            CoffeeSharedPreferences.getInstance().isFirstInstall = false
            val now = LocalDateTime.now().toString()
            dataStore.saveCoffeePreference(MAINTENANCE_DATE, now)
            dataStore.saveLastResetProductTime(now)
            DefaultSettingManager.insertDefaultUser()
            TimeUtils.setDateAndTime(context, date, hour, min)
        }
    }

}