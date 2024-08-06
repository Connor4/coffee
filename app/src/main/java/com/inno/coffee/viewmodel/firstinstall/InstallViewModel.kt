package com.inno.coffee.viewmodel.firstinstall

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.function.defaultsetting.DefaultSettingManager
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.SystemLocaleHelper
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstallViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
//    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun finishSetting(context: Context, date: Long, hour: Int, min: Int, language: String) {
        TimeUtils.setDateAndTime(context, date, hour, min)
        viewModelScope.launch {
            SystemLocaleHelper.changeSystemLocale(context, language)
            CoffeeSharedPreferences.getInstance().isFirstInstall = false
            dataStore.saveMachineLanguage(language)
            DefaultSettingManager.insertDefaultUser()
        }
    }

}