package com.inno.common.annotation

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(SUPPORT, MANAGER, EMPLOYEE)
annotation class UserRole

const val EMPLOYEE = 1
const val MANAGER = 2
const val SUPPORT = 3
