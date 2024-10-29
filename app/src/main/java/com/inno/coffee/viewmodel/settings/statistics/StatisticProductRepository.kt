package com.inno.coffee.viewmodel.settings.statistics

import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.dao.ProductCountDao
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.ProductCount
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType
import javax.inject.Inject

class StatisticProductRepository @Inject constructor(
    private val productCountDao: ProductCountDao,
    private val formulaDao: FormulaDao
) {
    suspend fun deleteAllProductCount() {
        productCountDao.deleteAll()
    }

    suspend fun incrementProductCount(model: Formula) {
        val redirect = ProductType.redirectToCoffee(model.productType?.type)
        val type = ProductType.fromValue(redirect ?: "")
        val time = System.currentTimeMillis()
        val p = ProductCount(productId = model.productId, type = type, count = 1, time = time)
        productCountDao.insert(p)
    }

//    suspend fun getAllProductCounts(): List<ProductCount> {
//        return productCountDao.getAllProductCounts()
//    }
//
//    suspend fun getProductCountsByType(type: ProductType): List<ProductCount>{
//        return productCountDao.getProductCountsByType(type)
//    }

    suspend fun getProductCountByProductId(productId: Int): Int {
        return productCountDao.getProductCountByProductId(productId)
    }

    suspend fun getTypeCounts(): List<ProductTypeCount> {
        return productCountDao.getTypeCounts()
    }

    suspend fun getAllFormula(): List<Formula> {
        return formulaDao.getAllFormula()
    }

    suspend fun getFormulaByProductId(productId: Int): Formula? {
        return formulaDao.getFormulaByProductId(productId)
    }

    suspend fun getProductCountsForDate(startTime: Long, endTime: Long,
        productId: Int): Int {
        return productCountDao.getProductCountsForDate(startTime, endTime, productId)
    }

    suspend fun getDayTypeCounts(startTime: Long, endTime: Long): List<ProductTypeCount> {
        return productCountDao.getDayTypeCounts(startTime, endTime)

    }

}