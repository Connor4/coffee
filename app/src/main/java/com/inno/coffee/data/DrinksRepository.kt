package com.inno.coffee.data

import javax.inject.Inject

class DrinksRepository @Inject constructor(
    private val drinksLocalDataSource: DrinksLocalDataSource
) {
    val drinksType: List<DrinksModel> = drinksLocalDataSource.drinksTypes

}