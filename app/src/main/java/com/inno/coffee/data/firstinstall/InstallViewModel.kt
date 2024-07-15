package com.inno.coffee.data.firstinstall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.CoffeeDataStore
import com.inno.common.db.entity.defaultUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InstallViewModel @Inject constructor(
    private val repository: InstallRepository,
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun insertDefaultUsers() {
        viewModelScope.launch {
            defaultUsers.forEach {
                repository.registerUser(it.username, it.passwordHash, it.role, it.permission,
                    it.remark)
            }
        }
    }

    fun finishSetting(language: String, date: Long, hour: Int, min: Int) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                dataStore.saveMachineLanguage(language)
            }
        }
    }

}