package com.inno.coffee.function.statistic

import android.app.Application
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.di.StatisticProductRepositoryEntryPoint
import com.inno.coffee.viewmodel.settings.statistics.StatisticProductRepository
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun countProductType(model: DrinksModel) {
        scope.launch {
            repository.incrementProductCount(model)
        }
    }

}