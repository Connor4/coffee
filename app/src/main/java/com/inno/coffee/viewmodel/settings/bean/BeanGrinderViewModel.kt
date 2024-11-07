package com.inno.coffee.viewmodel.settings.bean

import androidx.lifecycle.ViewModel
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BeanGrinderViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun init() {
    }
}