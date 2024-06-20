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

enum class SerialErrorType(val value: Int, val errorMsg: String) {
    MAX_READ_TRY(1, "Max data retry count reached"),
    MAX_OPEN_TRY(2, "Max open retry count reached"),
    IO_EXCEPTION(3, "IOException"),
    FRAME_FLAG_ILLEGAL(4, "Invalid frame format"),
    CRC_CHECK_FAILED(5, "CRC check failed"),
    SERIAL_CONNECTION_ERROR(6, "connection error")
}