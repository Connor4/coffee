package com.inno.coffee.ui.firstinstall

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IntRange
import com.inno.coffee.R
import com.inno.common.utils.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Proxy

class CoffeeTimePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    var onValueSelected: ((Int?, Int?, Boolean) -> Unit)? = null,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val tag = "CoffeeTimePickView"
    private var holdingInstance: Any? = null

    init {
        addTimePickerView(context, attrs, defStyleAttr)
    }

    @SuppressLint("PrivateApi")
    private fun addTimePickerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        try {
            val timePickerViewClass = Class.forName("android.widget.RadialTimePickerView")
            val constructor: Constructor<*> =
                timePickerViewClass.getDeclaredConstructor(Context::class.java)
            constructor.isAccessible = true
            val timePickerViewInstance = constructor.newInstance(context)
            if (timePickerViewInstance is View) {
                holdingInstance = timePickerViewInstance
                addView(timePickerViewInstance)
                setAttributes(timePickerViewInstance, context, attrs, defStyleAttr)
                set24HourMode(timePickerViewInstance)
                setTextSize(timePickerViewInstance)
                setSelectRadius(timePickerViewInstance)
                setTimeValueSelectListener(timePickerViewClass, timePickerViewInstance)
            } else {
                Logger.e(tag, "Failed to cast to View")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setShowType(@IntRange(from = 0, to = 1) type: Int) {
        try {
            val methodSetCurrentItemShowing =
                holdingInstance!!::class.java.getDeclaredMethod("setCurrentItemShowing",
                    Int::class.java, Boolean::class.java)
            methodSetCurrentItemShowing.isAccessible = true
            methodSetCurrentItemShowing.invoke(holdingInstance, type, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("PrivateApi")
    private fun setTimeValueSelectListener(timePickerViewClass: Class<*>, timePickerViewInstance:
    Any) {
        try {
            val onValueSelectedListenerClass =
                Class.forName("android.widget.RadialTimePickerView\$OnValueSelectedListener")
            val setOnValueSelectedListenerMethod =
                timePickerViewClass.getDeclaredMethod("setOnValueSelectedListener",
                    onValueSelectedListenerClass)
            setOnValueSelectedListenerMethod.isAccessible = true

            val listener = Proxy.newProxyInstance(
                onValueSelectedListenerClass.classLoader,
                arrayOf(onValueSelectedListenerClass)
            ) { _, method, args ->
                if (method.name == "onValueSelected") {
                    val pickerType = args[0] as Int
                    val newValue = args[1] as Int
                    val autoAdvance = args[2] as Boolean
                    Logger.d(tag, "type $pickerType  value $newValue auto $autoAdvance")
                    onValueSelected?.invoke(pickerType, newValue, autoAdvance)
                }
                null
            }
            setOnValueSelectedListenerMethod.invoke(timePickerViewInstance, listener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSelectRadius(timePickerViewInstance: Any) {
        try {
            val selectRadiusDimen =
                resources.getDimension(R.dimen.timepicker_selector_radius).toInt()
//            val selectDotRadiusDimen =
//                resources.getDimension(R.dimen.timepicker_selector_dot_radius).toInt()
            val selectStrokeDimen =
                resources.getDimension(R.dimen.timepicker_selector_stroke).toInt()
            val centerDotRadiusDimen =
                resources.getDimension(R.dimen.timepicker_center_dot_radius).toInt()

//            val selectDotRadiusField =
//                timePickerViewInstance::class.java.getDeclaredField("mSelectorDotRadius")
//            selectDotRadiusField.isAccessible = true
//            selectDotRadiusField.set(timePickerViewInstance, selectDotRadiusDimen)

            val centerDotRadiusField =
                timePickerViewInstance::class.java.getDeclaredField("mCenterDotRadius")
            centerDotRadiusField.isAccessible = true
            centerDotRadiusField.set(timePickerViewInstance, centerDotRadiusDimen)

            val selectRadiusField =
                timePickerViewInstance::class.java.getDeclaredField("mSelectorRadius")
            selectRadiusField.isAccessible = true
            selectRadiusField.set(timePickerViewInstance, selectRadiusDimen)

            val selectStrokeField =
                timePickerViewInstance::class.java.getDeclaredField("mSelectorStroke")
            selectStrokeField.isAccessible = true
            selectStrokeField.set(timePickerViewInstance, selectStrokeDimen)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setTextSize(timePickerViewInstance: Any) {
        try {
            val textSizeDimen = resources.getDimension(R.dimen.timepicker_text_size_normal).toInt()
            val textSizeField = timePickerViewInstance::class.java.getDeclaredField("mTextSize")
            textSizeField.isAccessible = true
            val textSize = textSizeField.get(timePickerViewInstance) as IntArray
            textSize[0] = textSizeDimen
            textSize[1] = textSizeDimen
            textSize[2] = textSizeDimen
            textSizeField.set(timePickerViewInstance, textSize)

            val textInsetDimen =
                resources.getDimension(R.dimen.timepicker_text_inset_normal).toInt()
            val textInsetInnerDimen = resources.getDimension(R.dimen.timepicker_text_inset_inner)
                .toInt()
            val textInsetField = timePickerViewInstance::class.java.getDeclaredField("mTextInset")
            textInsetField.isAccessible = true
            val textInset = textInsetField.get(timePickerViewInstance) as IntArray
            textInset[0] = textInsetDimen
            textInset[1] = textInsetDimen
            textInset[2] = textInsetInnerDimen
            textInsetField.set(timePickerViewInstance, textInset)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun set24HourMode(timePickerViewInstance: Any) {
        try {
            val hourModeField = timePickerViewInstance::class.java.getDeclaredField("mIs24HourMode")
            hourModeField.isAccessible = true
            hourModeField.set(timePickerViewInstance, true)
//            val amFmField = timePickerViewInstance::class.java.getDeclaredField("mAmOrPm")
//            amFmField.isAccessible = true
//            amFmField.set(timePickerViewInstance, 1)
            val methodInitDate = timePickerViewInstance::class.java.getDeclaredMethod("initData")
            methodInitDate.isAccessible = true
            methodInitDate.invoke(timePickerViewInstance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAttributes(timePickerViewInstance: Any, context: Context, attrs:
    AttributeSet?, defStyleAttr: Int) {
        try {
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable
                .CoffeeTimePickerView, defStyleAttr, 0)
            val backgroundColor =
                a.getColor(R.styleable.CoffeeTimePickerView_coffeeTimePickerBackgroundColor,
                    context.getColor(R.color.time_picker_background_color))
            val selectColor =
                a.getColor(R.styleable.CoffeeTimePickerView_coffeeTimePickerSelectColor,
                    context.getColor(R.color.time_picker_select_color))
            a.recycle()

            val backgroundPaintField: Field =
                timePickerViewInstance::class.java.getDeclaredField("mPaintBackground")
            backgroundPaintField.isAccessible = true
            val backgroundPaint = backgroundPaintField.get(timePickerViewInstance) as Paint
            backgroundPaint.setColor(backgroundColor)

            val centerPaintField =
                timePickerViewInstance::class.java.getDeclaredField("mPaintCenter")
            centerPaintField.isAccessible = true
            val centerPaint = centerPaintField.get(timePickerViewInstance) as Paint
            centerPaint.setColor(selectColor)

            val textColorField: Field =
                timePickerViewInstance::class.java.getDeclaredField("mTextColor")
            textColorField.isAccessible = true
            val textColor = textColorField.get(timePickerViewInstance) as Array<ColorStateList?>
            textColor[0] = ColorStateList.valueOf(0xFFFFFFFF.toInt())
            textColor[1] = ColorStateList.valueOf(0xFFFFFFFF.toInt())
            textColor[2] = ColorStateList.valueOf(0xFFFFFFFF.toInt())
            textColorField.set(timePickerViewInstance, textColor)

            val selectColorField =
                timePickerViewInstance::class.java.getDeclaredField("mSelectorColor")
            selectColorField.isAccessible = true
            selectColorField.set(timePickerViewInstance, selectColor)

            val selectDotColorField =
                timePickerViewInstance::class.java.getDeclaredField("mSelectorDotColor")
            selectDotColorField.isAccessible = true
            selectDotColorField.set(timePickerViewInstance, selectColor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}