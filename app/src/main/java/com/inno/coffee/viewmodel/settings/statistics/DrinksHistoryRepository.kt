package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.DrinksHistoryDao
import com.inno.common.db.entity.DrinksHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DrinksHistoryRepository @Inject constructor(
    private val drinksHistoryDao: DrinksHistoryDao
) {

    fun getAllDrinksHistory(): Flow<List<DrinksHistory>> {
        return drinksHistoryDao.getAllDrinksHistory()
    }

    suspend fun insertDrinksHistory(drinksHistory: DrinksHistory) {
        drinksHistoryDao.insert(drinksHistory)
    }

    suspend fun deleteAllDrinksHistory() {
        drinksHistoryDao.deleteAll()
    }

}