package com.inno.common.annotations

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(HOME, STATISTIC, FORMULA, DISPLAY, MACHINE_SETTING, MACHINE_OPERATION, VAT_AND_GRIND,
    WASH_MACHINE, PERMISSION, MAINTENANCE, SERIAL_TEST)
annotation class UserModule

const val HOME = "0"
const val STATISTIC = "1"
const val FORMULA = "2"
const val DISPLAY = "3"
const val MACHINE_SETTING = "4"
const val MACHINE_OPERATION = "5"
const val VAT_AND_GRIND = "6"
const val WASH_MACHINE = "7"
const val PERMISSION = "8"
const val MAINTENANCE = "9"
const val SERIAL_TEST = "10"