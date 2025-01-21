package com.inno.serialport.utilities.statusenum

enum class CleanMachineEnum(val value: Int) {
    FAIL(-1),
    CLEAN_COFFEE_FINISH(1202),
    CLEAN_FOAM_FINISH(1203);

    companion object {
        fun getStatus(value: Int): CleanMachineEnum {
            for (type in CleanMachineEnum.entries) {
                if (type.value == value) {
                    return type
                }
            }
            return FAIL
        }
    }
}