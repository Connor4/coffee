package com.inno.coffee.viewmodel.settings.statistics

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.viewmodel.home.HomeLocalDataSource
import com.inno.common.db.dao.ProductCountDao
import javax.inject.Inject

class StatisticProductRepository @Inject constructor(
    private val localDataSource: HomeLocalDataSource,
    private val productCountDao: ProductCountDao,
) {
    val drinkType: List<DrinksModel> = localDataSource.drinksTypes

    suspend fun deleteAllProductCount() {
        productCountDao.deleteAll()
    }

}