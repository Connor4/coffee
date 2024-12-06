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
    RINSE("Rinse"),
    STOP("Stop");

    companion object {
        fun fromValue(value: String): ProductType {
            for (productType in entries) {
                if (productType.value == value) {
                    return productType
                }
            }
            return NONE
        }

        fun redirectToCoffee(value: String?): String? {
            if (value == ESPRESSO.value || value == AMERICANO.value || value == DRIP_COFFEE.value
                    || POD_COFFEE.value == value || DRIP_POD_COFFEE.value == value || value == COFFEE
                        .value) {
                return COFFEE.value
            }
            return value
        }

        fun isOperationType(value: String?): Boolean {
            return value == STOP.value || value == RINSE.value || value == FOAM.value ||
                    value == STEAM.value
        }

        fun assertType(string: String?, productType: ProductType): Boolean {
            return productType.value == string
        }

        fun isMakingEnableType(string: String?): Boolean {
            return assertType(string, STOP) || assertType(string, FOAM) || assertType(string, STEAM)
        }

        fun isCoffeeType(type: String?): Boolean {
            return type == ESPRESSO.value || type == AMERICANO.value || type == DRIP_POD_COFFEE.value
                    || type == DRIP_COFFEE.value || type == POD_COFFEE.value || type == COFFEE.value
        }

    }

}