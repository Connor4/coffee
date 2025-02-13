package com.inno.coffee.utilities

import com.inno.coffee.R
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
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

val previewFormula = Formula(
    productId = 3, productType = FormulaItem.FormulaProductType(ProductType
        .COFFEE.value),
    productName = FormulaItem.FormulaProductName("意式", "home_item_espresso"),
    beanHopper = FormulaItem.FormulaBeanHopperPosition(true),
    coffeeWater = FormulaItem.FormulaUnitValue(20f,
        0f,
        100f,
        "[mm]"),
    powderDosage = FormulaItem.FormulaUnitValue(50f,
        0f,
        1000f,
        "[tick]"), pressWeight = FormulaItem.FormulaPressWeight(20,
        0f,
        50f,
        "[kg]"),
    preMakeTime = FormulaItem.FormulaUnitValue(800f,
        0f,
        1000f,
        "[s]"),
    postPreMakeWaitTime = FormulaItem.FormulaUnitValue(2000f,
        0f,
        1000f,
        "[s]"),
    secPressWeight = FormulaItem.FormulaUnitValue(0f,
        0f,
        1000f,
        "[mm]"),
    hotWater = FormulaItem.FormulaUnitValue(150f,
        0f,
        1000f,
        "[tick]"),
    waterSequence = FormulaItem.FormulaAmericanoSeq(true),
    coffeeCycles = FormulaItem.FormulaUnitValue(
        value = 1f,
        rangeStart = 0f,
        rangeEnd = 10f,
        unit = "[-]"
    ),
    bypassWater = FormulaItem.FormulaUnitValue(
        value = 0f,
        rangeStart = 0f,
        rangeEnd = 10f,
        unit = "[%]"
    ),
    cups = FormulaItem.FormulaCups(
        single = 1000,
        double = 1000,
        current = 2,
    )
)