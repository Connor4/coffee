package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.ProductHistoryDao
import com.inno.common.db.entity.ProductHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductHistoryRepository @Inject constructor(
    private val drinksHistoryDao: ProductHistoryDao
) {

    fun getAllProductHistory(): Flow<List<ProductHistory>> {
        return drinksHistoryDao.getAllProductHistory()
    }

    suspend fun insertProductHistory(drinksHistory: ProductHistory) {
        drinksHistoryDao.insert(drinksHistory)
    }

    suspend fun deleteAllProductHistory() {
        drinksHistoryDao.deleteAll()
    }

}