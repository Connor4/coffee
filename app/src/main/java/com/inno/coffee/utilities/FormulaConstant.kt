package com.inno.coffee.utilities

import com.inno.coffee.R
import com.inno.common.enums.ProductType

val formulaProductTypeMultilingual = mapOf(
    Pair(ProductType.NONE.value, R.string.formula_product_type_none),
    Pair(ProductType.ESPRESSO.value, R.string.formula_product_type_espresso),
    Pair(ProductType.COFFEE.value, R.string.formula_product_type_coffee),
    Pair(ProductType.AMERICANO.value, R.string.formula_product_type_americano),
    Pair(ProductType.DRIP_COFFEE.value, R.string.formula_product_type_drip_coffee),
    Pair(ProductType.POD_COFFEE.value, R.string.formula_product_type_pod_coffee),
    Pair(ProductType.DRIP_POD_COFFEE.value, R.string.formula_product_type_drip_pod_coffee),
    Pair(ProductType.HOT_WATER.value, R.string.formula_product_type_hot_water),
    Pair(ProductType.MILK.value, R.string.formula_product_type_milk),
    Pair(ProductType.FOAM.value, R.string.formula_product_type_foam),
    Pair(ProductType.STEAM.value, R.string.formula_product_type_steam),
)

