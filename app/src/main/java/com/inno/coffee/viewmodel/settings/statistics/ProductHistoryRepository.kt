package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.ProductHistoryDao
import com.inno.common.db.dao.RinseHistoryDao
import com.inno.common.db.entity.ProductHistory
import com.inno.common.db.entity.RinseHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductHistoryRepository @Inject constructor(
    private val drinksHistoryDao: ProductHistoryDao,
    private val rinseHistoryDao: RinseHistoryDao,
) {

    fun getAllProductHistory(): Flow<List<ProductHistory>> {
        return drinksHistoryDao.getAllProductHistory()
    }

    fun getAllRinseHistory(): Flow<List<RinseHistory>> {
        return rinseHistoryDao.getAllRinseHistory()
    }

    suspend fun insertProductHistory(drinksHistory: ProductHistory) {
        drinksHistoryDao.insertProductHistory(drinksHistory)
    }

    suspend fun deleteAllProductHistory() {
        drinksHistoryDao.deleteAllProductHistory()
    }


}