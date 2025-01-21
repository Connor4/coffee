package com.inno.serialport.utilities.statusenum

enum class ErrorStatusEnum(val value: Int) {
    FAIL(-1),
    FRONT_VAT_EMPTY(2000),
    BACK_VAT_EMPTY(2001),
    VAT_EMPTY(2002),
    NO_PILL_LEFT(2003),
    NO_PILL_RIGHT(2004),
    LEFT_GRINDER_ERROR(3000),
    RIGHT_GRINDER_ERROR(3001),
    LEFT_BOILER_ERROR(3002),
    RIGHT_BOILER_ERROR(3003),
    LEFT_EXTRACT_ERROR(3004),
    RIGHT_EXTRACT_ERROR(3005),
    STREAM_BOILER_ERROR(3006),
    NO_WATER_ERROR(4000);

    companion object {
        fun getStatus(value: Int): ErrorStatusEnum {
            for (type in ErrorStatusEnum.entries) {
                if (type.value == value) {
                    return type
                }
            }
            return FAIL
        }
    }
}