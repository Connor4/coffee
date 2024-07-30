package com.inno.coffee.viewmodel.home

import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLocalDataSource @Inject constructor() {
    val specialItem = listOf(1, 2, 3)

    val drinksTypes = listOf(
        DrinksModel(
            productId = 1,
            price = 20,
            name = R.string.home_item_stop,
            imageRes = R.drawable.stop
        ),
        DrinksModel(
            productId = 2,
            price = 21,
            name = R.string.home_item_hot_water,
            imageRes = R.drawable.coffee2
        ),
        DrinksModel(
            productId = 3,
            price = 22,
            name = R.string.home_item_foam,
            imageRes = R.drawable.coffee3
        ),
        DrinksModel(
            productId = 4,
            price = 24,
            name = R.string.home_item_rinse,
            imageRes = R.drawable.coffee4
        ),
        DrinksModel(
            productId = 5,
            price = 18,
            name = R.string.home_item_espresso,
            imageRes = R.drawable.coffee5
        ),
        DrinksModel(
            productId = 6,
            price = 10,
            name = R.string.home_item_coffee,
            imageRes = R.drawable.coffee6
        ),
        DrinksModel(
            productId = 7,
            price = 10,
            name = R.string.home_item_americano,
            imageRes = R.drawable.coffee6
        ),
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_auto_foam,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_double_espresso,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_double_coffee,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_double_americano,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_manual_foam,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_cappuccino,
//            imageRes = R.drawable.coffee6
//        ),
//
//        DrinksModel(
//            productId = ,
//            price = 10,
//            name = R.string.home_item_latte,
//            imageRes = R.drawable.coffee6
//        ),
        )
}