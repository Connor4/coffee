package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.ProductHistoryDao
import javax.inject.Inject

class MachineCounterRepository @Inject constructor(
    private val drinksHistoryDao: ProductHistoryDao,
) {

    suspend fun getAllProductCount(): Int {
        return drinksHistoryDao.getAllProductCount()
    }

}