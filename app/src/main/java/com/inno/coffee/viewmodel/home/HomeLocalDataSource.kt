package com.inno.coffee.viewmodel.home

import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.common.enums.ProductType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLocalDataSource @Inject constructor() {
    val specialItem = listOf(1, 2, 4, 8)

    val drinksTypes = listOf(
        DrinksModel(
            productId = 1,
            type = ProductType.OPERATION,
            name = R.string.home_item_stop,
            imageRes = R.drawable.stop
        ),
        DrinksModel(
            productId = 2,
            type = ProductType.COFFEE,
            name = R.string.home_item_hot_water,
            imageRes = R.drawable.hotwater
        ),
        DrinksModel(
            productId = 3,
            type = ProductType.FOAM,
            name = R.string.home_item_foam,
            imageRes = R.drawable.coffee3
        ),
        DrinksModel(
            productId = 4,
            type = ProductType.OPERATION,
            name = R.string.home_item_rinse,
            imageRes = R.drawable.rinse
        ),
        DrinksModel(
            productId = 5,
            type = ProductType.COFFEE,
            name = R.string.home_item_espresso,
            imageRes = R.drawable.coffee5
        ),
        DrinksModel(
            productId = 6,
            type = ProductType.COFFEE,
            name = R.string.home_item_coffee,
            imageRes = R.drawable.coffee2
        ),
        DrinksModel(
            productId = 7,
            type = ProductType.COFFEE,
            name = R.string.home_item_americano,
            imageRes = R.drawable.coffee4
        ),
        DrinksModel(
            productId = 8,
            type = ProductType.OPERATION,
            name = R.string.home_item_steam,
            imageRes = R.drawable.steam,
        ),
//        DrinksModel(
//            productId = ,
//            name = R.string.home_item_double_espresso,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            name = R.string.home_item_double_coffee,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            name = R.string.home_item_double_americano,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            name = R.string.home_item_manual_foam,
//            imageRes = R.drawable.coffee6
//        ),
//        DrinksModel(
//            productId = ,
//            name = R.string.home_item_cappuccino,
//            imageRes = R.drawable.coffee6
//        ),
//
//        DrinksModel(
//            productId = ,
//            name = R.string.home_item_latte,
//            imageRes = R.drawable.coffee6
//        ),
    )
}