package com.inno.coffee.utilities

import com.inno.coffee.R
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import kotlin.reflect.full.memberProperties

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

val formulaProperties = Formula::class.memberProperties

val formulaPropertyNames = listOf(
    "productType",
    "productName",
    "vat",
    FORMULA_PROPERTY_COFFEE_WATER,
    FORMULA_PROPERTY_POWDER_DOSAGE,
    "pressWeight",
    "preMakeTime",
    "postPreMakeWaitTime",
    "secPressWeight",
    "hotWater",
    "waterSequence",
    "coffeeCycles",
    "bypassWater",
)

val formulaStringKeys = listOf(
    R.string.formula_product_type,
    R.string.formula_product_name,
    R.string.formula_vat_position,
    R.string.formula_water_dosage,
    R.string.formula_powder_dosage,
    R.string.formula_press_weight,
    R.string.formula_pre_make_time,
    R.string.formula_pre_make_wait_time,
    R.string.formula_second_press_weight,
    R.string.formula_hot_water_dosage,
    R.string.formula_americano_seq,
    R.string.formula_coffee_cycles,
    R.string.formula_bypass_dosage,
)