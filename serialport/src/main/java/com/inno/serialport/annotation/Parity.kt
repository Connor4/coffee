package com.inno.serialport.annotation

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(PARITY_NONE, PARITY_ODD, PARITY_EVEN)
annotation class Parity

const val PARITY_NONE = 0
const val PARITY_ODD = 1
const val PARITY_EVEN = 2
