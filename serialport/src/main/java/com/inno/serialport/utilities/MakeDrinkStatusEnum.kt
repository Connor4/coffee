package com.inno.serialport.utilities

enum class MakeDrinkStatusEnum(val value: Int) {
    FAIL(-1),
    LEFT_BREWING(1000),
    RIGHT_BREWING(1001),
    LEFT_BREWING_COMPLETE(1002),
    RIGHT_BREWING_COMPLETE(1003),
    LEFT_FINISHED(1004),
    RIGHT_FINISHED(1005),
    LEFT_BOILER_TEMPERATURE(1006),
    RIGHT_BOILER_TEMPERATURE(1007);

    companion object {
        fun getStatus(value: Int): MakeDrinkStatusEnum {
            for (type in entries) {
                if (type.value == value) {
                    return type
                }
            }
            return FAIL
        }
    }
}