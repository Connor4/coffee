package com.inno.coffee.viewmodel.settings.info

import androidx.lifecycle.ViewModel
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MachineInfoViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {
}