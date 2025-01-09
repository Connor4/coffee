package com.inno.coffee.viewmodel.settings.display

import android.content.Context
import android.provider.Settings
import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.function.CommandControlManager
import com.inno.coffee.utilities.DISPLAY_COLOR_BLUE
import com.inno.coffee.utilities.DISPLAY_COLOR_GREEN
import com.inno.coffee.utilities.DISPLAY_COLOR_MIX
import com.inno.coffee.utilities.DISPLAY_COLOR_RED
import com.inno.coffee.utilities.DISPLAY_PER_PAGE_COUNT_12
import com.inno.coffee.utilities.INDEX_AUTO_BACK_TO_FIRST_PAGE
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_COLOR
import com.inno.coffee.utilities.INDEX_NUMBER_OF_PRODUCT_PER_PAGE
import com.inno.coffee.utilities.INDEX_SCREEN_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_SHOW_EXTRACTION_TIME
import com.inno.coffee.utilities.INDEX_SHOW_GRINDER_BUTTON
import com.inno.coffee.utilities.INDEX_SHOW_PRODUCT_NAME
import com.inno.coffee.utilities.INDEX_SHOW_PRODUCT_PRICE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import com.inno.common.utils.SystemLocaleHelper
import com.inno.common.utils.TimeUtils
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.FRAME_ADDRESS_3
import com.inno.serialport.utilities.FRAME_ADDRESS_4
import com.inno.serialport.utilities.FRONT_GRADIENT_COLOR_ID
import com.inno.serialport.utilities.FRONT_SINGLE_COLOR_ID
import com.inno.serialport.utilities.FRONT_TWINKLE_COLOR_ID
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    companion object {
        private const val TAG = "DisplayViewModel"
        private const val BACK_TO_FIRST_PAGE = "back_to_first_page"
        private const val FRONT_LIGHT_COLOR = "front_light_color"
        private const val FRONT_LIGHT_BRIGHTNESS = "front_light_brightness"
        private const val SCREEN_BRIGHTNESS = "screen_brightness"
        private const val SHOW_EXTRACTION_TIME = "show_extraction_time"
    }

    private val _language = MutableStateFlow("")
    val language: StateFlow<String> = _language
    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time

    private val _backToFirstPage = MutableStateFlow(false)
    val backToFirstPage: StateFlow<Boolean> = _backToFirstPage
    private val _numberOfProductPerPage = MutableStateFlow(DISPLAY_PER_PAGE_COUNT_12)
    val numberOfProductPerPage: StateFlow<Int> = _numberOfProductPerPage
    private val _frontLightColor = MutableStateFlow(DISPLAY_COLOR_MIX)
    val frontLightColor: StateFlow<Int> = _frontLightColor
    private val _frontLightBrightness = MutableStateFlow(0)
    val frontLightBrightness: StateFlow<Int> = _frontLightBrightness
    private val _screenBrightness = MutableStateFlow(0)
    val screenBrightness: StateFlow<Int> = _screenBrightness

    private val _showExtractionTime = MutableStateFlow(true)
    val showExtractionTime: StateFlow<Boolean> = _showExtractionTime
    private val _showProductPrice = MutableStateFlow(false)
    val showProductPrice = _showProductPrice
    private val _showProductName = MutableStateFlow(true)
    val showProductName = _showProductName
    private val _showGrinderButton = MutableStateFlow(0)
    val showGrinderButton = _showGrinderButton

    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.FRONT_COLOR, subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        DataCenter.unsubscribe(ReceivedDataType.FRONT_COLOR, subscriber)
    }

    fun initGroup() {
        viewModelScope.launch {
            val systemLanguage = dataStore.getSystemLanguage()
            val autoBackToFirstPage = dataStore.getCoffeePreference(BACK_TO_FIRST_PAGE, false)
            val numberOfProductPerPage = dataStore.getNumberOfProductPerPage()
            val frontLightColor = dataStore.getCoffeePreference(FRONT_LIGHT_COLOR, 1)
            val frontLightBrightness = dataStore.getCoffeePreference(FRONT_LIGHT_BRIGHTNESS, 90)
            val screenBrightness = dataStore.getCoffeePreference(SCREEN_BRIGHTNESS, 90)
            val showExtractionTime = dataStore.getCoffeePreference(SHOW_EXTRACTION_TIME, true)
            val showProductPrice = dataStore.getShowProductPrice()
            val showProductName = dataStore.getShowProductName()

            _language.value = systemLanguage
            _time.value = TimeUtils.getFullFormat()
            _backToFirstPage.value = autoBackToFirstPage
            _numberOfProductPerPage.value = numberOfProductPerPage
            _frontLightColor.value = frontLightColor
            _frontLightBrightness.value = frontLightBrightness
            _screenBrightness.value = screenBrightness
            _showExtractionTime.value = showExtractionTime
            _showProductPrice.value = showProductPrice
            _showProductName.value = showProductName
            _showGrinderButton.value = dataStore.getShowGrinderAdjustButton()
        }
    }

    fun getLanguage() {
        viewModelScope.launch {
            val language = dataStore.getSystemLanguage()
            _language.value = language
            Logger.d(TAG, "getLanguage() language = $language")
        }
    }

    fun getTime() {
        viewModelScope.launch {
            _time.value = TimeUtils.getFullFormat()
        }
    }

    fun selectLanguage(context: Context, language: String) {
        Logger.d(TAG, "selectLanguage() language = $language")
        _language.value = language
        viewModelScope.launch(defaultDispatcher) {
            delay(100)
            SystemLocaleHelper.changeSystemLocale(context, language)
            dataStore.saveSystemLanguage(language)
        }
    }

    fun setSystemTime(context: Context, date: Long, hour: Int, min: Int) {
        viewModelScope.launch(defaultDispatcher) {
            TimeUtils.setDateAndTime(context, date, hour, min)
            Logger.d(TAG, "setSystemTime() called time ${_time.value}")
        }
    }

    fun saveDisplayGroupTwoValue(context: Context, key: Int, value: Any) {
        Logger.d(TAG,
            "saveDisplayGroupTwoValue() called with: key = $key, value = $value")
        viewModelScope.launch(defaultDispatcher) {
            when (key) {
                INDEX_AUTO_BACK_TO_FIRST_PAGE -> {
                    dataStore.saveCoffeePreference(BACK_TO_FIRST_PAGE, value as Boolean)
                    _backToFirstPage.value = value
                }
                INDEX_NUMBER_OF_PRODUCT_PER_PAGE -> {
                    dataStore.saveNumberOfProductPerPage(value as Int)
                    _numberOfProductPerPage.value = value
                }
                INDEX_FRONT_LIGHT_COLOR -> {
                    dataStore.saveCoffeePreference(FRONT_LIGHT_COLOR, value as Int)
                    _frontLightColor.value = value
                    setFrontLightColor(value)
                }
                INDEX_FRONT_LIGHT_BRIGHTNESS -> {
                    dataStore.saveCoffeePreference(FRONT_LIGHT_BRIGHTNESS, value as Int)
                    _frontLightBrightness.value = value
                }
                INDEX_SCREEN_BRIGHTNESS -> {
                    dataStore.saveCoffeePreference(SCREEN_BRIGHTNESS, value as Int)
                    _screenBrightness.value = value
                    setScreenBrightness(context, value)
                }
                INDEX_SHOW_EXTRACTION_TIME -> {
                    dataStore.saveCoffeePreference(SHOW_EXTRACTION_TIME, value as Boolean)
                    _showExtractionTime.value = value
                }
                INDEX_SHOW_PRODUCT_PRICE -> {
                    dataStore.saveShowProductPrice(value as Boolean)
                    _showProductPrice.value = value
                }
                INDEX_SHOW_PRODUCT_NAME -> {
                    dataStore.saveShowProductName(value as Boolean)
                    _showProductName.value = value
                }
                INDEX_SHOW_GRINDER_BUTTON -> {
                    dataStore.saveShowGrinderAdjustButton(value as Int)
                    _showGrinderButton.value = value
                }
            }
        }
    }

    private fun setScreenBrightness(context: Context, @IntRange(from = 0, to = 255) value: Int) {
        viewModelScope.launch(defaultDispatcher) {
            val contentResolver = context.contentResolver
            try {
                val mode =
                    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
                if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
                }
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setFrontLightColor(color: Int) {
        when (color) {
            DISPLAY_COLOR_MIX -> {
                CommandControlManager.sendFrontColorCommand(FRONT_GRADIENT_COLOR_ID,
                    FRAME_ADDRESS_3, 0x32, 0, 0XFF, 0X00, 0X00, 0X00, 0XFF, 0X00)
                CommandControlManager.sendFrontColorCommand(FRONT_GRADIENT_COLOR_ID,
                    FRAME_ADDRESS_4, 0x32, 0, 0XFF, 0X00, 0X00, 0X00, 0XFF, 0X00)
            }
            DISPLAY_COLOR_RED -> {
                CommandControlManager.sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3,
                    0, 0XFF, 0)
                CommandControlManager.sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4,
                    0, 0XFF, 0)
            }
            DISPLAY_COLOR_GREEN -> {
                CommandControlManager.sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3,
                    0XFF, 0, 0)
                CommandControlManager.sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4,
                    0XFF, 0, 0)
            }
            DISPLAY_COLOR_BLUE -> {
                CommandControlManager.sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_3,
                    0, 0, 0XFF)
                CommandControlManager.sendFrontColorCommand(FRONT_SINGLE_COLOR_ID, FRAME_ADDRESS_4,
                    0, 0, 0XFF)
            }
        }
    }

    private fun parseReceivedData(data: Any) {
        if (data is ReceivedData.FrontColor) {
            when (data.commandId) {
                FRONT_SINGLE_COLOR_ID -> {
                }
                FRONT_GRADIENT_COLOR_ID -> {}
                FRONT_TWINKLE_COLOR_ID -> {}
            }
        }
    }

}