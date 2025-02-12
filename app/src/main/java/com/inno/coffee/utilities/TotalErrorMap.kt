package com.inno.coffee.utilities

import com.inno.coffee.R

val errorMap = mapOf(
    -1 to R.string.error_serial_read_fail,
    -2 to R.string.error_max_read_retry,
    -3 to R.string.error_max_open_retry,
    -4 to R.string.error_serial_io_exception,
    -5 to R.string.error_serial_format_invalid,
    -6 to R.string.error_crc_check_failed,
    -7 to R.string.error_read_no_data,
    -8 to R.string.error_heart_beat_miss,
    -9 to R.string.error_frame_size_error,
    -10 to R.string.error_serial_no_reply,
    -11 to R.string.error_command_no_reply,
    -12 to R.string.error_wait_command_timeout,
    2000 to R.string.error_front_bean_hopper_empty,
    2001 to R.string.error_rear_bean_hopper_empty,
    2002 to R.string.error_bean_hopper_empty,
    3000 to R.string.error_left_grinder_error,
    3001 to R.string.error_right_grinder_error,
    3002 to R.string.error_left_boiler_error,
    3003 to R.string.error_right_boiler_error,
    3004 to R.string.error_left_brewer_error,
    3005 to R.string.error_right_brewer_error,
    3006 to R.string.error_steam_boiler_error,
    4000 to R.string.error_no_water,
)