package com.inno.serialport.core

import androidx.annotation.IntDef

interface ISerialPort {

    companion object {
        const val PARITY_NONE = 0
        const val PARITY_ODD = 1
        const val PARITY_EVEN = 2
        const val STOP_BITS_1 = 1
        const val STOP_BITS_2 = 2
        const val DATA_BITS_5 = 5
        const val DATA_BITS_6 = 6
        const val DATA_BITS_7 = 7
        const val DATA_BITS_8 = 8
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(PARITY_NONE, PARITY_ODD, PARITY_EVEN)
    annotation class Parity

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STOP_BITS_1, STOP_BITS_2)
    annotation class StopBits

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DATA_BITS_5, DATA_BITS_6, DATA_BITS_7, DATA_BITS_8)
    annotation class DataBits
}