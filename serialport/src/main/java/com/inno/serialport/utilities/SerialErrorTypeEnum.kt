package com.inno.serialport.utilities

enum class SerialErrorTypeEnum(val value: Int, val errorMsg: String) {
    READ_FAIL(-1, "read byte fail"),
    MAX_READ_TRY(-2, "Max data retry count reached"),
    MAX_OPEN_TRY(-3, "Max open retry count reached"),
    IO_EXCEPTION(-4, "IOException"),
    FRAME_FORMAT_ILLEGAL(-5, "Invalid frame format"),
    CRC_CHECK_FAILED(-6, "CRC check failed"),
    READ_NO_DATA(-7, "read no data"),
    HEART_BEAT_MISS(-8, "heart beat miss, need reboot"),
    FRAME_SIZE_ERROR(-9, "frame size error");

    companion object {
        fun getErrorMsgByValue(value: Int): String {
            for (errorType in entries) {
                if (errorType.value == value) {
                    return errorType.errorMsg
                }
            }
            return "miss match error message"
        }
    }
}