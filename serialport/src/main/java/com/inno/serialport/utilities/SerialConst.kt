package com.inno.serialport.utilities

const val FRAME_DATA_START_INDEX = 1
const val FRAME_FLAG_INDEX = 0
const val FRAME_ADDRESS_INDEX = 1
const val FRAME_CONTROL_INDEX = 2
const val FRAME_LENGTH_INDEX_LOW = 3
const val FRAME_LENGTH_INDEX_HIGH = 4
const val FRAME_CMD_INDEX_LOW = 5
const val FRAME_CMD_INDEX_HIGH = 6
const val FRAME_CONTENT_START_INDEX = 7
const val FRAME_ADDRESS_2 = 0x2.toByte()
const val FRAME_ADDRESS_3 = 0x3.toByte()
const val FRAME_ADDRESS_4 = 0x4.toByte()

const val ERROR_ID = 0
const val HEARTBEAT_COMMAND_ID: Short = 1
const val GET_FW_VERSION_ID: Short = 2
const val START_HEAT_COFFEE_BOILER_ID: Short = 10
const val STOP_HEAT_COFFEE_BOILER_ID: Short = 11
const val START_HEAT_STEAM_BOILER_ID: Short = 12
const val STOP_HEAT_STEAM_BOILER_ID: Short = 13
const val CLEAN_MACHINE_ID: Short = 16
const val CONTINUE_CLEAN_MACHINE_ID: Short = 17
const val CLEAN_COFFEE_MODULE_ID: Short = 18
const val CLEAN_FOAM_MODULE_ID: Short = 19
const val MAKE_DRINKS_COMMAND_ID: Short = 100
const val MACHINE_PARAM_COMMAND_ID: Short = 200
const val COFFEE_INPUT_COMMAND_ID: Short = 201
const val STEAM_INPUT_COMMAND_ID: Short = 202
const val MILK_INPUT_COMMAND_ID: Short = 203
const val COFFEE_OUTPUT_COMMAND_ID: Short = 300
const val MACHINE_TEST_WATER_PUMP_ID: Short = 301
const val MACHINE_TEST_WATER_INLET_VALVE: Short = 302
const val MACHINE_TEST_BREW_VALVE_LEFT: Short = 303
const val MACHINE_TEST_BYPASS_VALVE_LEFT: Short = 304
const val MACHINE_TEST_AMERICANO_LEFT: Short = 305
const val MACHINE_TEST_BREW_VALVE_RIGHT: Short = 306
const val MACHINE_TEST_BYPASS_VALVE_RIGHT: Short = 307
const val MACHINE_TEST_AMERICANO_RIGHT: Short = 308
const val MACHINE_TEST_BALL_DISPENSER_FORWARD: Short = 309
const val MACHINE_TEST_BALL_DISPENSER_BACKWARD: Short = 310
const val MACHINE_TEST_BOILER_LEFT: Short = 311
const val MACHINE_TEST_BOILER_RIGHT: Short = 312
const val MACHINE_TEST_GRINDER_LEFT: Short = 313
const val MACHINE_TEST_GRINDER_RIGHT: Short = 314
const val MACHINE_TEST_FAN_FRONT: Short = 315
const val MACHINE_TEST_FAN_LEFT: Short = 316
const val MACHINE_TEST_FAN_RIGHT: Short = 317
const val STEAM_OUTPUT_COMMAND_ID: Short = 400
const val MACHINE_TEST_WATER_FILL_VALVE: Short = 403
const val MACHINE_TEST_PURGE_VALVE: Short = 404
const val MACHINE_TEST_PURGE_MAX_VALVE: Short = 405
const val MACHINE_TEST_HOT_WATER_VALVE: Short = 406
const val MACHINE_TEST_HOT_WATER_MAX_VALVE: Short = 407
const val MACHINE_TEST_STEAM_VALVE_1: Short = 408
const val MACHINE_TEST_STEAM_VALVE_2: Short = 409
const val MACHINE_TEST_FOAM_VALVE_1: Short = 410
const val MACHINE_TEST_FOAM_VALVE_2: Short = 411
const val MACHINE_TEST_STEAM_BOILER_HEATER1: Short = 412
const val MACHINE_TEST_STEAM_BOILER_HEATER2: Short = 413
const val MACHINE_TEST_AIR_PUMP: Short = 414
const val MACHINE_TEST_MILK_OUTPUT_COMMAND_ID: Short = 450
const val MACHINE_TEST_MILK_OUTPUT_RINSE_VALVE_LEFT: Short = 451
const val MACHINE_TEST_MILK_OUTPUT_TANK_VALVE_LEFT: Short = 452
const val MACHINE_TEST_MILK_OUTPUT_STEAM_VALVE_LEFT: Short = 453
const val MACHINE_TEST_MILK_OUTPUT_DRAIN_VALVE_LEFT: Short = 454
const val MACHINE_TEST_MILK_OUTPUT_AIR_VALVE_LEFT: Short = 455
const val MACHINE_TEST_MILK_OUTPUT_OUTLET_VALVE_LEFT: Short = 456
const val MACHINE_TEST_MILK_OUTPUT_MILK_PUMP_LEFT: Short = 457
const val MACHINE_TEST_MILK_OUTPUT_RINSE_VALVE_RIGHT: Short = 458
const val MACHINE_TEST_MILK_OUTPUT_TANK_VALVE_RIGHT: Short = 459
const val MACHINE_TEST_MILK_OUTPUT_STEAM_VALVE_RIGHT: Short = 460
const val MACHINE_TEST_MILK_OUTPUT_DRAIN_VALVE_RIGHT: Short = 461
const val MACHINE_TEST_MILK_OUTPUT_AIR_VALVE_RIGHT: Short = 462
const val MACHINE_TEST_MILK_OUTPUT_OUTLET_VALVE_RIGHT: Short = 463
const val MACHINE_TEST_MILK_OUTPUT_MILK_PUMP_RIGHT: Short = 464
const val MACHINE_TEST_MILK_OUTPUT_AIR_PUMP: Short = 465
const val MACHINE_TEST_MOTOR_INIT_ID: Short = 500
const val MACHINE_TEST_MOTOR_TEST_ID: Short = 501
const val INFO_COFFEE_STATUS_ID: Short = 550
const val INFO_STEAM_STATUS_ID: Short = 551
const val MAINTENANCE_EMPTY_COFFEE_BOILER_ID: Short = 600
const val MAINTENANCE_WATER_PUMP_PRESSURE_ID: Short = 601
const val MAINTENANCE_DEPRESSURIZE_STEAM_BOILER_ID: Short = 602
const val MAINTENANCE_EMPTY_STEAM_BOILER_ID: Short = 603
const val MAINTENANCE_STEAM_BOILER_OVER_PRESSURE_TEST_ID: Short = 604
const val MAINTENANCE_FRONT_RINSE_ID: Short = 605
const val MAINTENANCE_FLOW_RATE_RINSE_ID: Short = 606
const val MAINTENANCE_GRINDER_SENSOR_TEST_ID: Short = 650
const val MAINTENANCE_FLOW_RATE_TEST_ID: Short = 651
const val MAINTENANCE_CLEANING_BALL_TEST_ID: Short = 652
const val MAINTENANCE_MILK_SENSOR_RIGHT_TEST_ID: Short = 653
const val MAINTENANCE_MILK_SENSOR_LEFT_TEST_ID: Short = 654
const val BEAN_GRINDER_SETTING: Short = 700
const val GRINDER_ADJ_FINER_ID: Short = 710
const val GRINDER_ADJ_COARSER_ID: Short = 711
const val GRINDER_ADJ_GRIND_ID: Short = 712
const val CMD_STOP_MAKE_PRODUCT_LEFT: Short = 998
const val CMD_STOP_MAKE_PRODUCT_RIGHT: Short = 999

const val FRONT_SINGLE_COLOR_ID: Short = 3001
const val FRONT_GRADIENT_COLOR_ID: Short = 3002
const val FRONT_TWINKLE_COLOR_ID: Short = 3004

val fcstab = intArrayOf(
    0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536, 0x74bf,
    0x8c48, 0x9dc1, 0xaf5a, 0xbed3, 0xca6c, 0xdbe5, 0xe97e, 0xf8f7,
    0x1081, 0x0108, 0x3393, 0x221a, 0x56a5, 0x472c, 0x75b7, 0x643e,
    0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed, 0xcb64, 0xf9ff, 0xe876,
    0x2102, 0x308b, 0x0210, 0x1399, 0x6726, 0x76af, 0x4434, 0x55bd,
    0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e, 0xfae7, 0xc87c, 0xd9f5,
    0x3183, 0x200a, 0x1291, 0x0318, 0x77a7, 0x662e, 0x54b5, 0x453c,
    0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef, 0xea66, 0xd8fd, 0xc974,
    0x4204, 0x538d, 0x6116, 0x709f, 0x0420, 0x15a9, 0x2732, 0x36bb,
    0xce4c, 0xdfc5, 0xed5e, 0xfcd7, 0x8868, 0x99e1, 0xab7a, 0xbaf3,
    0x5285, 0x430c, 0x7197, 0x601e, 0x14a1, 0x0528, 0x37b3, 0x263a,
    0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9, 0x8960, 0xbbfb, 0xaa72,
    0x6306, 0x728f, 0x4014, 0x519d, 0x2522, 0x34ab, 0x0630, 0x17b9,
    0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a, 0xb8e3, 0x8a78, 0x9bf1,
    0x7387, 0x620e, 0x5095, 0x411c, 0x35a3, 0x242a, 0x16b1, 0x0738,
    0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb, 0xa862, 0x9af9, 0x8b70,
    0x8408, 0x9581, 0xa71a, 0xb693, 0xc22c, 0xd3a5, 0xe13e, 0xf0b7,
    0x0840, 0x19c9, 0x2b52, 0x3adb, 0x4e64, 0x5fed, 0x6d76, 0x7cff,
    0x9489, 0x8500, 0xb79b, 0xa612, 0xd2ad, 0xc324, 0xf1bf, 0xe036,
    0x18c1, 0x0948, 0x3bd3, 0x2a5a, 0x5ee5, 0x4f6c, 0x7df7, 0x6c7e,
    0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e, 0xf2a7, 0xc03c, 0xd1b5,
    0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66, 0x7eef, 0x4c74, 0x5dfd,
    0xb58b, 0xa402, 0x9699, 0x8710, 0xf3af, 0xe226, 0xd0bd, 0xc134,
    0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7, 0x6e6e, 0x5cf5, 0x4d7c,
    0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028, 0x91a1, 0xa33a, 0xb2b3,
    0x4a44, 0x5bcd, 0x6956, 0x78df, 0x0c60, 0x1de9, 0x2f72, 0x3efb,
    0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9, 0x8120, 0xb3bb, 0xa232,
    0x5ac5, 0x4b4c, 0x79d7, 0x685e, 0x1ce1, 0x0d68, 0x3ff3, 0x2e7a,
    0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a, 0xb0a3, 0x8238, 0x93b1,
    0x6b46, 0x7acf, 0x4854, 0x59dd, 0x2d62, 0x3ceb, 0x0e70, 0x1ff9,
    0xf78f, 0xe606, 0xd49d, 0xc514, 0xb1ab, 0xa022, 0x92b9, 0x8330,
    0x7bc7, 0x6a4e, 0x58d5, 0x495c, 0x3de3, 0x2c6a, 0x1ef1, 0x0f78
)

val HEART_BEAT_COMMAND = byteArrayOf(
    0x7e.toByte(), 0x02.toByte(), 0x01.toByte(), 0x02.toByte(),
    0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0xcc.toByte(),
    0x2b.toByte(), 0x7e.toByte())

val HEART_BEAT_REPLY = byteArrayOf(
    0x7e.toByte(), 0x02.toByte(), 0x02.toByte(), 0x04.toByte(), 0x00.toByte(),
    0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x06.toByte(), 0x84.toByte(),
    0x7e.toByte())

val MULTI_REPLY = byteArrayOf(
    0x7e.toByte(), 0x02.toByte(), 0x02.toByte(), 0x04.toByte(), 0x00.toByte(),
    0x01.toByte(), 0x00.toByte(), 0x02.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x37.toByte(), 0x90.toByte(),
    0x7e.toByte(),
    0x7e.toByte(), 0x02.toByte(), 0x02.toByte(), 0x04.toByte(), 0x00.toByte(),
    0x01.toByte(), 0x00.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x27.toByte(), 0x1e.toByte(),
    0x7e.toByte()
)

/**
 * see{@link ProductProfile}
 */
val PRODUCT_COMMAND_DEMO = byteArrayOf(
    0x7E.toByte(), 0x02.toByte(), 0x01.toByte(), 0x6C.toByte(),
    0x00.toByte(), 0x64.toByte(), 0x00.toByte(), 0x01.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x07.toByte(), 0x00.toByte(), 0xB8.toByte(),
    0x0B.toByte(), 0x8C.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0xE8.toByte(), 0x03.toByte(), 0x14.toByte(), 0x00.toByte(),
    0x20.toByte(), 0x03.toByte(), 0xD0.toByte(), 0x07.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0xD0.toByte(), 0x07.toByte(),
    0x32.toByte(), 0x00.toByte(), 0x96.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x88.toByte(), 0x13.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0xA0.toByte(), 0x0F.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0xA2.toByte(), 0x0F.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0xA3.toByte(), 0x0F.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
    0x00.toByte(), 0x00.toByte(), 0xC1.toByte(), 0x38.toByte(),
    0x7E.toByte()
)

