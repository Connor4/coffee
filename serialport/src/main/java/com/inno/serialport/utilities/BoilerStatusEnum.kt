package com.inno.serialport.utilities

enum class BoilerStatusEnum(val value: Int) {
    FAIL(-1),
    LEFT_BOILER_TEMPERATURE(1006),
    RIGHT_BOILER_TEMPERATURE(1007);

    companion object {
        fun getStatus(value: Int): BoilerStatusEnum {
            for (type in BoilerStatusEnum.entries) {
                if (type.value == value) {
                    return type
                }
            }
            return FAIL
        }
    }
}