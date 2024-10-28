package com.inno.common.annotations

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(HOME, STATISTIC, FORMULA, DISPLAY, MACHINE_SETTING, MACHINE_OPERATION, BEANS_AND_GRINDER,
    WASH_MACHINE, PERMISSION, INTERFACE, MAINTENANCE, MACHINE_INFO, SERIAL_TEST)
annotation class UserModule

const val HOME = "0"
const val STATISTIC = "1"
const val FORMULA = "2"
const val DISPLAY = "3"
const val MACHINE_SETTING = "4"
const val MACHINE_OPERATION = "5"
const val BEANS_AND_GRINDER = "6"
const val WASH_MACHINE = "7"
const val PERMISSION = "8"
const val INTERFACE = "9"
const val MAINTENANCE = "10"
const val MACHINE_INFO = "11"
const val MACHINE_TEST = "12"
const val SERIAL_TEST = "13"