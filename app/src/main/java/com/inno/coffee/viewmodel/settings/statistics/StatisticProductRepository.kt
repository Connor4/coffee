package com.inno.coffee.viewmodel.settings.statistics

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.viewmodel.home.HomeLocalDataSource
import com.inno.common.db.dao.ProductCountDao
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.ProductCount
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType
import javax.inject.Inject

class StatisticProductRepository @Inject constructor(
    private val localDataSource: HomeLocalDataSource,
    private val productCountDao: ProductCountDao,
) {
    val drinkType: List<DrinksModel> = localDataSource.drinksTypes

    suspend fun deleteAllProductCount() {
        productCountDao.deleteAll()
    }

    suspend fun incrementProductCount(model: Formula) {
        val productCount = productCountDao.getProductCountByProductId(model.productId)
        if (productCount != null) {
            productCount.count += 1
            productCountDao.updateProductCount(productCount)
        } else {
            val type = ProductType.fromValue(model.productType?.type ?: "")
            val p = ProductCount(productId = model.productId, type = type, count = 1)
            productCountDao.insert(p)
        }
    }

//    suspend fun getAllProductCounts(): List<ProductCount> {
//        return productCountDao.getAllProductCounts()
//    }
//
//    suspend fun getProductCountsByType(type: ProductType): List<ProductCount>{
//        return productCountDao.getProductCountsByType(type)
//    }

    suspend fun getProductCountByProductId(productId: Int): ProductCount? {
        return productCountDao.getProductCountByProductId(productId)
    }

    suspend fun getTypeCounts(): List<ProductTypeCount> {
        return productCountDao.getTypeCounts()
    }

}