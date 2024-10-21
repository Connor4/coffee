package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.CleanMachineHistory
import com.inno.common.db.entity.ErrorHistory
import com.inno.common.db.entity.MaintenanceHistory
import com.inno.common.db.entity.ProductHistory
import com.inno.common.db.entity.RinseHistory
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductHistoryViewModel @Inject constructor(
    private val repository: ProductHistoryRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    val productHistory: StateFlow<List<ProductHistory>> =
        repository.getAllProductHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val rinseHistory: StateFlow<List<RinseHistory>> =
        repository.getAllRinseHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val cleanHistory: StateFlow<List<CleanMachineHistory>> =
        repository.getAllCleanHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val errorHistory: StateFlow<List<ErrorHistory>> =
        repository.getAllErrorHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val maintenanceHistory: StateFlow<List<MaintenanceHistory>> =
        repository.getAllMaintenanceHistoryDao().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun insertProductHistory(drinksHistory: ProductHistory) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.insertProductHistory(drinksHistory)
            }
        }
    }

    fun deleteAllProductHistory() {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.deleteAllProductHistory()
            }
        }
    }

    fun addMaintenanceHistory(description: String) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val nowTime = System.currentTimeMillis()
                val language = dataStore.getSystemLanguage()
                val nowTimeFormat = TimeUtils.getNowTimeInYearAndHour(nowTime, language)
                val maintenanceHistory =
                    MaintenanceHistory(time = nowTimeFormat, description = description)
                repository.addMaintenanceHistory(maintenanceHistory)
            }
        }
    }

}