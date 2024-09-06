package com.inno.coffee.ui.firstinstall

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.inno.coffee.R
import com.inno.common.utils.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.Field

class CoffeeTimePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    var onDateSelected: ((String?, String?, Long) -> Unit)? = null,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val tag = "CoffeeTimePickView"
    private val am = 0
    private val fm = 1

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
                addView(timePickerViewInstance)
                setAttributes(timePickerViewInstance, context, attrs, defStyleAttr)
                set24HourMode(timePickerViewInstance)
                setTextSize(timePickerViewInstance)
            } else {
                Logger.e(tag, "Failed to cast to View")
            }
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}