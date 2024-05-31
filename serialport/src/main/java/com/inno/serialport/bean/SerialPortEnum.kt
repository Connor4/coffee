package com.inno.serialport.bean

enum class ParityType(val value: Int) {
    NONE_PARITY(0),
    ODD_PARITY(1),
    EVEN_PARITY(2)
}

enum class StopBits(val value: Int) {
    SINGLE(1),
    DOUBLE(2)
}