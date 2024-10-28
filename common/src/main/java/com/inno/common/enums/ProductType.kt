package com.inno.common.enums

enum class ProductType(val value: String) {
    NONE("None"),
    ESPRESSO("Espresso"),
    COFFEE("Coffee"),
    AMERICANO("Americano"),
    DRIP_COFFEE("DripCoffee"),
    POD_COFFEE("PodCoffee"),
    DRIP_POD_COFFEE("DripPodCoffee"),
    HOT_WATER("HotWater"),
    MILK("Milk"),
    FOAM("Foam"),
    STEAM("Steam"),
    OPERATION("Operation");

    companion object {
        fun fromValue(value: String): ProductType {
            for (productType in entries) {
                if (productType.value == value) {
                    return productType
                }
            }
            return NONE
        }
    }

}