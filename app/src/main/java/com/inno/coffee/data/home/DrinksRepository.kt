package com.inno.coffee.data.home

import com.inno.coffee.data.DrinksModel
import javax.inject.Inject

class DrinksRepository @Inject constructor(
    private val drinksLocalDataSource: DrinksLocalDataSource
) {
    val drinksType: List<DrinksModel> = drinksLocalDataSource.drinksTypes

}