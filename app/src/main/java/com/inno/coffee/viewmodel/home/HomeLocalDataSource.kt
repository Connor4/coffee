package com.inno.coffee.viewmodel.home

import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.common.enums.ProductType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLocalDataSource @Inject constructor() {
    val specialItem = mutableListOf(1, 3, 4, 11, 12)

    val drinksTypes = listOf(
        DrinksModel(
            productId = 1,
            type = ProductType.OPERATION,
            name = R.string.home_item_stop,
            imageRes = R.drawable.home_stop_normal_ic
        ),
        DrinksModel(
            productId = 2,
            type = ProductType.OPERATION,
            name = R.string.home_item_hot_water,
            imageRes = R.drawable.drink_hot_water_ic
        ),
        DrinksModel(
            productId = 3,
            type = ProductType.FOAM,
            name = R.string.home_item_foam,
            imageRes = R.drawable.operate_milk_foam_ic
        ),
        DrinksModel(
            productId = 4,
            type = ProductType.OPERATION,
            name = R.string.home_item_rinse,
            imageRes = R.drawable.operate_rinse_ic
        ),
        DrinksModel(
            productId = 5,
            type = ProductType.COFFEE,
            name = R.string.home_item_espresso,
            imageRes = R.drawable.drink_espresso_ic
        ),
        DrinksModel(
            productId = 6,
            type = ProductType.COFFEE,
            name = R.string.home_item_double_espresso,
            imageRes = R.drawable.drink_double_espresso_ic
        ),
        DrinksModel(
            productId = 7,
            type = ProductType.COFFEE,
            name = R.string.home_item_americano,
            imageRes = R.drawable.drink_americano_ic
        ),
        DrinksModel(
            productId = 8,
            type = ProductType.COFFEE,
            name = R.string.home_item_double_americano,
            imageRes = R.drawable.drink_double_americano_ic,
        ),
        DrinksModel(
            productId = 9,
            type = ProductType.MILK,
            name = R.string.home_item_milk,
            imageRes = R.drawable.drink_milk_ic
        ),
        DrinksModel(
            productId = 10,
            type = ProductType.COFFEE,
            name = R.string.home_item_pod_coffee,
            imageRes = R.drawable.drink_pot_coffee_ic
        ),
        DrinksModel(
            productId = 11,
            type = ProductType.OPERATION,
            name = R.string.home_item_auto_foam,
            imageRes = R.drawable.operate_auto_milk_ic
        ),
        DrinksModel(
            productId = 12,
            type = ProductType.OPERATION,
            name = R.string.home_item_manual_foam,
            imageRes = R.drawable.operate_manual_milk_ic
        ),
        DrinksModel(
            productId = 13,
            type = ProductType.COFFEE,
            name = R.string.home_item_latte,
            imageRes = R.drawable.drink_latte_ic
        ),
        DrinksModel(
            productId = 14,
            type = ProductType.COFFEE,
            name = R.string.home_item_double_latte,
            imageRes = R.drawable.drink_double_latte_ic
        ),
        DrinksModel(
            productId = 15,
            type = ProductType.COFFEE,
            name = R.string.home_item_cappuccino,
            imageRes = R.drawable.drink_cappuccino_ic
        ),
        DrinksModel(
            productId = 16,
            type = ProductType.COFFEE,
            name = R.string.home_item_double_cappuccino,
            imageRes = R.drawable.drink_double_cappuccino_ic
        ),
        DrinksModel(
            productId = 17,
            type = ProductType.COFFEE,
            name = R.string.home_item_latte_macchiato,
            imageRes = R.drawable.drink_latte_macchiato_ic
        ),
        DrinksModel(
            productId = 18,
            type = ProductType.COFFEE,
            name = R.string.home_item_double_latte_macchiato,
            imageRes = R.drawable.drink_double_latte_macchiato_ic
        ),
        DrinksModel(
            productId = 19,
            type = ProductType.COFFEE,
            name = R.string.home_item_es_macchiato,
            imageRes = R.drawable.drink_espresso_ic
        ),
        DrinksModel(
            productId = 20,
            type = ProductType.COFFEE,
            name = R.string.home_item_double_es_macchiato,
            imageRes = R.drawable.drink_double_es_macchiato_ic
        ),
    )
}