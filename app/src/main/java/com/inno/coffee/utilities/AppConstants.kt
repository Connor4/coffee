package com.inno.coffee.utilities

const val INVALID_INT = -1
const val LOCK_AND_CLEAN_TIME = 15
const val VIEW_FAST_CLICK_INTERVAL_TIME = 300
const val DEFAULT_PERMISSION_MODULE = 762
const val HOME_LEFT_COFFEE_BOILER_TEMP = 0
const val HOME_RIGHT_COFFEE_BOILER_TEMP = 0
const val DEFAULT_SYSTEM_TIME = 1704038400000 // 2024/01/01
const val HEAD_INDEX = 0
const val HOUR = 0
const val MINUTES = 1
const val DATE = "date"
const val TIME = "time"

const val FIRST_INSTALL_KEY_ENGLISH = "en"
const val FIRST_INSTALL_KEY_SIMPLIFIED_CHINESE = "zh-CN"
const val FIRST_INSTALL_KEY_TRADITIONAL_CHINESE = "zh-TW"
const val FIRST_INSTALL_KEY_JAPANESE = "ja-JP"
const val FIRST_INSTALL_KEY_KOREAN = "ko-KR"
const val FIRST_INSTALL_KEY_FRENCH = "fr-FR"
const val FIRST_INSTALL_VALUE_ENGLISH = "English"
const val FIRST_INSTALL_VALUE_SIMPLIFIED_CHINESE = "中文(简体)"
const val FIRST_INSTALL_VALUE_TRADITIONAL_CHINESE = "中文(繁體)"
const val FIRST_INSTALL_VALUE_JAPANESE = "日本語"
const val FIRST_INSTALL_VALUE_KOREAN = "한국어"
const val FIRST_INSTALL_VALUE_FRENCH = "Français"

const val ONE_IN_BYTE = 1.toByte()
const val MAKE_DRINK_REPLY_VALUE = 0

const val KEY_COUNTER = "counter"
const val KEY_DAY_COUNTER = "daycounter"
const val KEY_PERIOD_COUNTER = "periodcounter"
const val KEY_TOTAL_COUNTER = "totalcounter"
const val KEY_HISTORY = "history"
const val HISTORY_VALUE_PRODUCT = "product"
const val HISTORY_VALUE_CLEAN = "clean"
const val HISTORY_VALUE_RINSE = "rinse"
const val HISTORY_VALUE_ERROR = "error"
const val HISTORY_VALUE_MAINTENANCE = "maintenance"

const val PERMISSION_USERNAME = 1
const val PERMISSION_PASSWORD = 2
const val PERMISSION_PASSWORD_AGAIN = 3
const val PERMISSION_MAX_INPUT_SIZE = 15
const val PERMISSION_EDIT_USERNAME = 1
const val PERMISSION_EDIT_PASSWORD = 2
const val PERMISSION_EDIT_REMARKS = 3
const val PERMISSION_KEY_USERNAME = "permission_key_username"

const val FORMULA_PRODUCT_NAME_MAX_SIZE = 15
const val FORMULA_SHOW_LEARN_WATER = 1
const val FORMULA_SHOW_POWDER_TEST = 2
const val FORMULA_PROPERTY_PRODUCT_TYPE = "productType"
const val FORMULA_PROPERTY_VAT = "vat"
const val FORMULA_PROPERTY_PRESS_WEIGHT = "pressWeight"
const val FORMULA_PROPERTY_COFFEE_WATER = "coffeeWater"
const val FORMULA_PROPERTY_POWDER_DOSAGE = "powderDosage"
const val FORMULA_PROPERTY_WATER_SEQUENCE = "waterSequence"
const val MAIN_SCREEN_PRODUCT_ID_LIMIT = 100
const val SECOND_SCREEN_PRODUCT_ID_LIMIT = 1000

const val DISPLAY_SETTING_KEY = "display_setting_key"
const val DISPLAY_SETTING_LANGUAGE = "display_setting_language"
const val DISPLAY_SETTING_TIME = "display_setting_time"
const val INDEX_AUTO_BACK_TO_FIRST_PAGE = 0
const val INDEX_NUMBER_OF_PRODUCT_PER_PAGE = 1
const val INDEX_FRONT_LIGHT_COLOR = 2
const val INDEX_FRONT_LIGHT_BRIGHTNESS = 3
const val INDEX_SCREEN_BRIGHTNESS = 4
const val INDEX_SHOW_EXTRACTION_TIME = 5
const val DISPLAY_PER_PAGE_COUNT_12 = 12
const val DISPLAY_PER_PAGE_COUNT_15 = 15
const val DISPLAY_COLOR_MIX = 1
const val DISPLAY_COLOR_RED = 2
const val DISPLAY_COLOR_GREEN = 3
const val DISPLAY_COLOR_BLUE = 4

const val INDEX_POWER_CONFIGURATION = 0
const val INDEX_LOW_POWER = 1
const val INDEX_MACHINE_TYPE = 2
const val INDEX_TEMPERATURE_UNIT = 3
const val INDEX_OPERATION_MODE = 4
const val INDEX_SMART_MODE = 5
const val INDEX_WATER_TANK_SURVEILLANCE = 6
const val INDEX_DIFFERENT_BOILER = 7
const val INDEX_AMERICANO_TEMP_ADJUST = 8
const val INDEX_HOT_WATER_OUTPUT = 9
const val INDEX_STEAM_WAND_POSITION = 10
const val CONFIG_OPERATION_MODE_NORMAL = 0
const val CONFIG_OPERATION_MODE_SELF_SERVICE = 1
const val SMART_MODE_OFF = 0
const val SMART_MODE_ON = 1
const val SMART_MODE_FRONT = 2
const val SMART_MODE_BACK = 3
const val ADJUSTMENT_NONE = 0
const val ADJUSTMENT_MANUAL = 1
const val ADJUSTMENT_AUTO = 2
const val HOW_WATER_MID = 0
const val HOT_WATER_SIDE = 1
const val STEAM_WAND_BOTH = 0
const val STEAM_WAND_LEFT = 1
const val STEAM_WAND_RIGHT = 2

const val PARAMS_KEY_BOILER_TEMP = 0
const val PARAMS_KEY_COLD_RINSE = 1
const val PARAMS_KEY_WARM_RINSE = 2
const val PARAMS_KEY_GROUNDS_QUANTITY = 3
const val PARAMS_KEY_BREW_BALANCE = 4
const val PARAMS_KEY_BREW_PRE_HEATING = 5
const val PARAMS_KEY_GRINDER_PURGE_FUNCTION = 6
const val PARAMS_KEY_NUMBER_OF_CYCLES_RINSE = 7
const val PARAMS_KEY_STEAM_BOILER_PRESSURE = 8
const val PARAMS_KEY_NTC_LEFT = 9
const val PARAMS_KEY_NTC_RIGHT = 10
const val PARAMS_VALUE_DRAWER_ONLY = 0
const val PARAMS_VALUE_DRAWER_IGNORE = 1
const val PARAMS_VALUE_DRAWER_ONE_KG = 2
const val PARAMS_VALUE_DRAWER_TWO_FIVE_KG = 3
const val PARAMS_VALUE_DRAWER_FIVE_KG = 4
const val PARAMS_VALUE_DRAWER_SEVEN_FIVE_KG = 5
const val PARAMS_VALUE_DRAWER_TEN_KG = 6
const val PARAMS_VALUE_STEAM_BOILER_PRESSURE = 1
const val PARAMS_VALUE_BOILER_TEMP = 90
const val PARAMS_VALUE_PRE_HEATING_OFF = 0
const val PARAMS_VALUE_PRE_HEATING_AUTOMATIC = 1
const val PARAMS_VALUE_PRE_HEATING_FORCE_5 = 2
const val PARAMS_VALUE_PRE_HEATING_FORCE_10 = 3
const val PARAMS_VALUE_PRE_HEATING_REMINDER_5 = 4
const val PARAMS_VALUE_PRE_HEATING_REMINDER_10 = 5
const val PARAMS_VALUE_PURGE_FUNCTION_NONE = 0
const val PARAMS_VALUE_PURGE_FUNCTION_GRINDER_PURGE = 1
const val PARAMS_VALUE_PURGE_FUNCTION_ETC = 2
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_r = 0
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_1_e = 1
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_2 = 2
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_3 = 3
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_4 = 4
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_5 = 5
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_10 = 10
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_15 = 15
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_20 = 20
const val PARAMS_VALUE_NUMBER_OF_CYCLES_RINSE_25 = 25

const val BEAN_KEY_INDEX_REAR_HOPPER = 0
const val BEAN_KEY_INDEX_FRONT_HOPPER = 1
const val BEAN_KEY_INDEX_LEVELLING = 2
const val BEAN_KEY_INDEX_PQC = 3
const val BEAN_KEY_INDEX_GRINDING_CAPACITY_REAR = 4
const val BEAN_KEY_INDEX_GRINDING_CAPACITY_FRONT = 5
const val BEAN_KEY_INDEX_ETC_REAR = 6
const val BEAN_KEY_INDEX_ETC_FRONT = 7
const val BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_REAR = 8
const val BEAN_KEY_INDEX_GRINDER_LIMIT_CAPACITY_FRONT = 9

const val MACHINE_TEST_KEY_COFFEE_INPUTS = 0
const val MACHINE_TEST_KEY_COFFEE_OUTPUTS = 1
const val MACHINE_TEST_KEY_STEAM_INPUTS = 2
const val MACHINE_TEST_KEY_STEAM_OUTPUTS = 3
const val MACHINE_TEST_KEY_MOTOR_TEST = 4
const val MACHINE_TEST_KEY_SERIAL_TEST = 5
const val MACHINE_TEST_KEY_ACTIVITY = "machine_test_key_activity"
const val MACHINE_TEST_VALUE_COFFEE_INPUTS = "coffee_inputs"
const val MACHINE_TEST_VALUE_COFFEE_OUTPUTS = "coffee_outputs"
const val MACHINE_TEST_VALUE_STEAM_INPUTS = "steam_inputs"
const val MACHINE_TEST_VALUE_STEAM_OUTPUTS = "steam_outputs"
const val MACHINE_TEST_VALUE_MOTOR_TEST = "motor_test"
const val MACHINE_TEST_MOTOR_STEP = 0
const val MACHINE_TEST_MOTOR_SPEED = 1
const val MACHINE_TEST_MOTOR_CURRENT = 2
const val MACHINE_TEST_MOTOR_LEFT_TOP = 0
const val MACHINE_TEST_MOTOR_LEFT_BOTTOM = 1
const val MACHINE_TEST_MOTOR_RIGHT_TOP = 3
const val MACHINE_TEST_MOTOR_RIGHT_BOTTOM = 2

const val INFO_KEY_ACTIVITY = "info_key_activity"
const val INFO_VALUE_COFFEE_ACTIVITY = "coffee_activity"
const val INFO_VALUE_STEAM_ACTIVITY = "steam_activity"

const val MAINTENANCE_KEY_ACTIVITY = "maintenance_key_activity"
const val MAINTENANCE_KEY_SERVICE_PARAM = 0
const val MAINTENANCE_VALUE_CUPS = 0
const val MAINTENANCE_VALUE_SCHEDULE = 1
const val MAINTENANCE_KEY_WATER_FILTER = 1
const val MAINTENANCE_KEY_SERVICE_FUNCTIONS = 2
const val MAINTENANCE_KEY_TEST_FUNCTIONS = 3
const val MAINTENANCE_KEY_SAVE_RESTORE = 4
const val MAINTENANCE_KEY_MANUAL = 5

const val CLEAN_MODE = 0
const val CLEAN_SET_TIME = 1
const val CLEAN_TIME_TOLERANCE = 2
const val CLEAN_WEEKEND_CLEAN_MODE = 3
const val CLEAN_MILK_WEEKEND_CLEAN_MODE = 4
const val CLEAN_STANDBY_AFTER_CLEANING = 5
const val CLEAN_STANDBY_BUTTON = 6
const val CLEAN_STANDBY_ON_OFF_TIME = 7
const val CLEAN_PERIOD_TIME = 8
const val CLEAN_MODE_PERIOD = 0
const val CLEAN_MODE_AUTO = 1
const val CLEAN_MODE_MANUAL = 2
const val CLEAN_WEEKEND_OFF = 0
const val CLEAN_WEEKEND_NO_SAT_SUN = 1
const val CLEAN_WEEKEND_NO_FRI_SAT = 2