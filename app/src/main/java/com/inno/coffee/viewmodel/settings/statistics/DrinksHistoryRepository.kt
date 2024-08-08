package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.DrinksHistoryDao
import com.inno.common.db.entity.ProductHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DrinksHistoryRepository @Inject constructor(
    private val drinksHistoryDao: DrinksHistoryDao
) {

    fun getAllDrinksHistory(): Flow<List<ProductHistory>> {
        return drinksHistoryDao.getAllDrinksHistory()
    }

    suspend fun insertDrinksHistory(drinksHistory: ProductHistory) {
        drinksHistoryDao.insert(drinksHistory)
    }

    suspend fun deleteAllDrinksHistory() {
        drinksHistoryDao.deleteAll()
    }

}