package com.inno.coffee.viewmodel.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.CleanMachineHistory
import com.inno.common.db.entity.ErrorHistory
import com.inno.common.db.entity.MaintenanceHistory
import com.inno.common.db.entity.ProductHistory
import com.inno.common.db.entity.RinseHistory
import com.inno.common.enums.ProductType
import com.inno.common.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ProductHistoryViewModel @Inject constructor(
    private val repository: ProductHistoryRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val productHistory: StateFlow<List<ProductHistory>> =
        repository.getAllProductHistory().map { list ->
            list.map { history ->
                val time = LocalDateTime.parse(history.time)
                history.time = TimeUtils.getFullFormat(time)
                history
            }
        }.stateIn(
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
        repository.getAllErrorHistory().map { list ->
            list.map { error ->
                val time = LocalDateTime.parse(error.time)
                error.time = TimeUtils.getFullFormat(time)
                error
            }
        }.stateIn(
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
                val time = TimeUtils.getFullFormat()
                val maintenanceHistory = MaintenanceHistory(time = time, description = description)
                repository.addMaintenanceHistory(maintenanceHistory)
            }
        }
    }

    fun addFakeHistoryDataForTest() {
        viewModelScope.launch {
            val time = LocalDateTime.now().toString()
            repository.insertProductHistory(
                ProductHistory(time = time, pressFinal = 24.0f,
                    brewSide = Random.nextBoolean(), grindTime = 11.4f, pqc = true,
                    grindAdjust = 2,
                    extTime = 18.6f,
                    waterQuantity = 100, waterTemp = 80, milkTemp = 90, steamPressure = 19,
                    productType = ProductType.COFFEE, cups = 2))
            repository.insertErrorHistory(ErrorHistory(time = time,
                detail = "Regular maintenance and upkeep are required", code = "W-204"))
            repository.insertCleanHistory(CleanMachineHistory(
                time = time, startTime = "12:00",
                duration = "10:00", stopped = true, tabsL = true, tabsR = true,
            ))
            repository.insertRinseHistory(RinseHistory(time = time, rinseType = "Cold Water",
                systemFlowRateL = 6.9f, systemFlowRateR = 6.9f, systemWaterPressureL = 1.0f,
                systemWaterPressureR = 1.0f, nozzleFlowRateL = 6.9f, nozzleFlowRateR = 6.9f,
                nozzleWaterPressureL = 1.0f, nozzleWaterPressureR = 1.0f))
        }
    }

}