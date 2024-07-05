package com.inno.serialport.annotation

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(DATA_BITS_5, DATA_BITS_6, DATA_BITS_7, DATA_BITS_8)
annotation class DataBits

const val DATA_BITS_5 = 5
const val DATA_BITS_6 = 6
const val DATA_BITS_7 = 7
const val DATA_BITS_8 = 8