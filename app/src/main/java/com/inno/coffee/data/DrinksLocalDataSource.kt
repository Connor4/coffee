package com.inno.coffee.data

import com.inno.coffee.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinksLocalDataSource @Inject constructor() {

    val drinksTypes = listOf(
        DrinksModel(
            price = 20,
            name = "咖啡1",
            imageRes = R.drawable.coffee1
        ),
        DrinksModel(
            price = 21,
            name = "咖啡2",
            imageRes = R.drawable.coffee2
        ),
        DrinksModel(
            price = 22,
            name = "咖啡3",
            imageRes = R.drawable.coffee3
        ),
        DrinksModel(
            price = 24,
            name = "咖啡4",
            imageRes = R.drawable.coffee4
        ),
        DrinksModel(
            price = 18,
            name = "咖啡5",
            imageRes = R.drawable.coffee5
        ),
        DrinksModel(
            price = 10,
            name = "咖啡6",
            imageRes = R.drawable.coffee6
        ),

        )
}