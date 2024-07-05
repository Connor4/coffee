package com.inno.serialport.annotation

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(STOP_BITS_1, STOP_BITS_2)
annotation class StopBits

const val STOP_BITS_1 = 1
const val STOP_BITS_2 = 2