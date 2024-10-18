package com.inno.common.annotations

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(TECHNICIAN, MANAGER, OPERATOR)
annotation class UserRole

const val OPERATOR_NAME = "operator"
const val MANAGER_NAME = "manager"
const val TECHNICIAN_NAME = "technician"
const val OPERATOR = 1
const val MANAGER = 2
const val TECHNICIAN = 3
