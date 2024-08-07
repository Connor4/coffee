package com.inno.coffee.viewmodel.settings.statistics

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.viewmodel.home.HomeLocalDataSource
import javax.inject.Inject

class StatisticProductRepository @Inject constructor(
    private val localDataSource: HomeLocalDataSource,
) {
    val drinkType: List<DrinksModel> = localDataSource.drinksTypes

}