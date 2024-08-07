package com.inno.common.annotation

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(TECHNICIAN, MANAGER, OPERATOR)
annotation class UserRole

const val OPERATOR = 1
const val MANAGER = 2
const val TECHNICIAN = 3
