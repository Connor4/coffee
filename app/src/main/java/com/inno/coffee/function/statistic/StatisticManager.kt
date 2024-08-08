package com.inno.coffee.function.statistic

import android.app.Application
import com.inno.coffee.di.StatisticProductRepositoryEntryPoint
import com.inno.coffee.viewmodel.settings.statistics.StatisticProductRepository
import dagger.hilt.android.EntryPointAccessors

object StatisticManager {
    // 统计当个产品数量
    // 统计当前产品组成用量
    // 需要异步自己执行
    private const val TAG = "StatisticManager"
    private lateinit var repository: StatisticProductRepository

    fun init(application: Application) {
        val entryPoint = EntryPointAccessors.fromApplication(
            application,
            StatisticProductRepositoryEntryPoint::class.java
        )
        repository = entryPoint.statisticProductRepository()
    }

    fun countProductType(productId: Int, size: Boolean) {

    }

}