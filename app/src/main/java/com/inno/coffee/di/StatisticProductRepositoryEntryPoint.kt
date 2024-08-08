package com.inno.coffee.di

import com.inno.coffee.viewmodel.settings.statistics.StatisticProductRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StatisticProductRepositoryEntryPoint {
    fun statisticProductRepository(): StatisticProductRepository
}