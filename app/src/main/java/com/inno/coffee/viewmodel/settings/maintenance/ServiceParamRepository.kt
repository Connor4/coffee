package com.inno.coffee.viewmodel.settings.maintenance

import com.inno.common.db.dao.ProductHistoryDao
import javax.inject.Inject

class ServiceParamRepository @Inject constructor(
    private val historyDao: ProductHistoryDao,
) {

    suspend fun getBrewProductCount(left: Boolean): Int {
        return historyDao.getBrewProductCount(left)
    }


}