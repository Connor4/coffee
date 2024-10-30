package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.CleanHistoryDao
import com.inno.common.db.dao.ErrorHistoryDao
import com.inno.common.db.dao.MaintenanceHistoryDao
import com.inno.common.db.dao.ProductHistoryDao
import com.inno.common.db.dao.RinseHistoryDao
import com.inno.common.db.entity.CleanMachineHistory
import com.inno.common.db.entity.ErrorHistory
import com.inno.common.db.entity.MaintenanceHistory
import com.inno.common.db.entity.ProductHistory
import com.inno.common.db.entity.RinseHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductHistoryRepository @Inject constructor(
    private val drinksHistoryDao: ProductHistoryDao,
    private val rinseHistoryDao: RinseHistoryDao,
    private val cleanHistoryDao: CleanHistoryDao,
    private val errorHistoryDao: ErrorHistoryDao,
    private val maintenanceHistoryDao: MaintenanceHistoryDao,
) {

    fun getAllProductHistory(): Flow<List<ProductHistory>> {
        return drinksHistoryDao.getAllProductHistory()
    }

    suspend fun insertProductHistory(drinksHistory: ProductHistory) {
        drinksHistoryDao.insertProductHistory(drinksHistory)
    }

    suspend fun deleteAllProductHistory() {
        drinksHistoryDao.deleteAllProductHistory()
    }

    fun getAllRinseHistory(): Flow<List<RinseHistory>> {
        return rinseHistoryDao.getAllRinseHistory()
    }

    fun getAllCleanHistory(): Flow<List<CleanMachineHistory>> {
        return cleanHistoryDao.getAllCleanHistory()
    }

    fun getAllErrorHistory(): Flow<List<ErrorHistory>> {
        return errorHistoryDao.getAllErrorHistory()
    }

    fun getAllMaintenanceHistoryDao(): Flow<List<MaintenanceHistory>> {
        return maintenanceHistoryDao.getAllMaintenanceHistory()
    }

    suspend fun addMaintenanceHistory(maintenanceHistory: MaintenanceHistory) {
        maintenanceHistoryDao.insertMaintenanceHistory(maintenanceHistory)
    }

    suspend fun insertErrorHistory(errorHistory: ErrorHistory) {
        errorHistoryDao.insertErrorHistory(errorHistory)
    }

    suspend fun insertCleanHistory(cleanHistory: CleanMachineHistory) {
        cleanHistoryDao.insertCleanHistory(cleanHistory)
    }

    suspend fun insertRinseHistory(rinseHistory: RinseHistory) {
        rinseHistoryDao.insertRinseHistory(rinseHistory)
    }

}