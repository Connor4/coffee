package com.inno.coffee.function.statistic

import android.app.Application
import com.inno.coffee.di.StatisticProductRepositoryEntryPoint
import com.inno.coffee.viewmodel.settings.statistics.StatisticProductRepository
import com.inno.common.db.entity.ErrorHistory
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.ProductHistory
import com.inno.common.enums.ProductType
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object StatisticManager {
    private const val TAG = "StatisticManager"
    private lateinit var repository: StatisticProductRepository
    private val scope = CoroutineScope(Dispatchers.IO)

    fun init(application: Application) {
        val entryPoint = EntryPointAccessors.fromApplication(
            application,
            StatisticProductRepositoryEntryPoint::class.java
        )
        repository = entryPoint.statisticProductRepository()
    }

    fun countProductType(formula: Formula) {
        scope.launch {
            repository.incrementProductCount(formula)

            val time = LocalDateTime.now().toString()
            val type = ProductType.fromValue(formula.productType!!.type)
            repository.insertProductHistory(
                ProductHistory(formula.productId, time, 0f, true,
                    0f, false, 0, 0f, 0,
                    0, 0, 0, type, 1,
                    false, ""))
        }
    }

    fun countErrorHistory(code: String, detail: String) {
        scope.launch {
            val time = LocalDateTime.now().toString()
            repository.insertErrorHistory(ErrorHistory(time, code, detail))
        }
    }

}