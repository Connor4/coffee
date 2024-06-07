package com.inno.common.db

import androidx.annotation.WorkerThread
import com.inno.common.db.dao.DrinksHistoryDao
import com.inno.common.db.entity.DrinksHistory
import kotlinx.coroutines.flow.Flow

class DrinksHistoryRepository(private val drinksHistoryDao: DrinksHistoryDao) {

    @WorkerThread
    suspend fun getAllDrinksHistory(): Flow<List<DrinksHistory>> {
        return drinksHistoryDao.getAllDrinksHistory()
    }

    @WorkerThread
    suspend fun insertDrinkHistory(drinksHistory: DrinksHistory) {
        drinksHistoryDao.insert(drinksHistory)
    }

    @WorkerThread
    suspend fun deleteAll() {
        drinksHistoryDao.deleteAll()
    }

}