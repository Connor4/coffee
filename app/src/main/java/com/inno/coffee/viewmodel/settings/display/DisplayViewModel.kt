package com.inno.coffee.viewmodel.settings.display

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.SystemLocaleHelper
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun selectLanguage(context: Context, language: String) {
        viewModelScope.launch(defaultDispatcher) {
            SystemLocaleHelper.changeSystemLocale(context, language)
            dataStore.saveSystemLanguage(language)
        }
    }

    fun setSystemTime(context: Context, date: Long, hour: Int, min: Int) {
        TimeUtils.setDateAndTime(context, date, hour, min)
    }

}