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
        fun fromValue(type: String): ProductType {
            for (productType in entries) {
                if (productType.value == type) {
                    return productType
                }
            }
            return NONE
        }

        fun redirectToCoffee(type: String?): String? {
            if (type == ESPRESSO.value || type == AMERICANO.value || type == DRIP_COFFEE.value
                    || POD_COFFEE.value == type || DRIP_POD_COFFEE.value == type || type == COFFEE
                        .value) {
                return COFFEE.value
            }
            return type
        }

        fun isOperationType(type: String?): Boolean {
            return type == STOP.value || type == RINSE.value || type == FOAM.value ||
                    type == STEAM.value
        }

        fun isFormulaCanShowType(type: String?): Boolean {
            return isCoffeeType(type) || type == HOT_WATER.value || type == MILK.value
                    || type == STEAM.value || type == FOAM.value
        }

        fun assertType(type: String?, productType: ProductType): Boolean {
            return productType.value == type
        }

        fun isMakingEnableType(type: String?): Boolean {
            return assertType(type, STOP) || assertType(type, FOAM) || assertType(type, STEAM)
        }

        fun isCoffeeType(type: String?): Boolean {
            return type == ESPRESSO.value || type == AMERICANO.value || type == DRIP_POD_COFFEE.value
                    || type == DRIP_COFFEE.value || type == POD_COFFEE.value || type == COFFEE.value
        }

    }

}