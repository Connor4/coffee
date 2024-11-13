package com.inno.serialport.utilities.statusenum

enum class GrinderStatusEnum(val value: Int) {
    FAIL(-1),
    LEFT_GRINDER_POWDER_DOSAGE(1009),
    RIGHT_GRINDER_POWDER_DOSAGE(1010);

    companion object {
        fun getStatus(value: Int): GrinderStatusEnum {
            for (type in GrinderStatusEnum.entries) {
                if (type.value == value) {
                    return type
                }
            }
            return FAIL
        }
    }
}