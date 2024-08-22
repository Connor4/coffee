package com.inno.serialport.utilities.statusenum

enum class BoilerStatusEnum(val value: Int) {
    FAIL(-1),
    BOILER_TEMPERATURE(1006),
    LEFT_GRINDER_POWDER_DOSAGE(1009),
    RIGHT_GRINDER_POWDER_DOSAGE(1010);

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