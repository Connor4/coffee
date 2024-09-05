package com.inno.coffee.ui.firstinstall

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.icu.util.Calendar
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.inno.coffee.R
import com.inno.common.utils.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Proxy
import java.text.SimpleDateFormat
import java.util.Locale

class CoffeeDatePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    var onDateSelected: ((String?) -> Unit)? = null,
) : LinearLayout(context, attrs, defStyleAttr) {
    private var prevButton: ImageButton? = null
    private var nextButton: ImageButton? = null
    private var observableSparseArray: ObservableSparseArray? = null
    private var currentDate: Calendar? = null
    private var dateFormat: SimpleDateFormat? = null

    init {
        orientation = VERTICAL
        addDayPickerView(context, attrs, defStyleAttr)
        val locale = Locale.getDefault()
        currentDate = Calendar.getInstance(locale)
        dateFormat = SimpleDateFormat("MMM d, yyyy", locale)
    }

    fun performPrevClick() {
        prevButton?.performClick()
    }

    fun performNextClick() {
        nextButton?.performClick()
    }

    fun initDate() {
        val currentDateStr = currentDate?.time?.let { dateFormat?.format(it) }
        onDateSelected?.invoke(currentDateStr)
    }

    @SuppressLint("PrivateApi")
    private fun addDayPickerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        try {
            val dayPickerViewClass = Class.forName("android.widget.DayPickerView")
            val constructor: Constructor<*> =
                dayPickerViewClass.getDeclaredConstructor(Context::class.java)
            constructor.isAccessible = true
            val dayPickerViewInstance = constructor.newInstance(context)
            if (dayPickerViewInstance is ViewGroup) {
                addView(dayPickerViewInstance)
                disableButton(dayPickerViewInstance)
                setTextStyle(dayPickerViewInstance, context, attrs, defStyleAttr)
                setDaySelectedListener(dayPickerViewInstance)
            } else {
                Logger.e("CoffeeDatePickerView", "Failed to cast to ViewGroup")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("CoffeeDatePickerView", "exception $e")
        }
    }

    private fun disableButton(dayPickerViewInstance: Any) {
        try {
            val prevButtonField: Field = dayPickerViewInstance::class.java
                .getDeclaredField("mPrevButton")
            prevButtonField.isAccessible = true
            prevButton = prevButtonField.get(dayPickerViewInstance) as ImageButton
            prevButton!!.isEnabled = false

            val nextButtonField: Field = dayPickerViewInstance::class.java
                .getDeclaredField("mNextButton")
            nextButtonField.isAccessible = true
            nextButton = nextButtonField.get(dayPickerViewInstance) as ImageButton
            nextButton!!.isEnabled = false
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("CustomDatePickerView", "Failed to disable: $e")
        }
    }

    private fun setTextStyle(dayPickerViewInstance: Any, context: Context, attrs: AttributeSet?,
        defStyleAttr: Int) {
        try {
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable
                .CoffeeDatePickerView, defStyleAttr, 0)
            val dayTextAppearanceResId = a.getResourceId(
                R.styleable.CoffeeDatePickerView_coffeeDateTextAppearance,
                R.style.coffee_datePicker_dayTextAppearance
            )
            val dayOfWeekTextAppearanceResId = a.getResourceId(
                R.styleable.CoffeeDatePickerView_coffeeDateTextAppearance,
                R.style.coffee_datePicker_dayOfWeekTextAppearance
            )
            val daySelectorColor = context.getColorStateList(R.color.day_selector_color)
            a.recycle()

            val adapterField: Field = dayPickerViewInstance::class.java
                .getDeclaredField("mAdapter")
            adapterField.isAccessible = true
            adapterField.get(dayPickerViewInstance)?.let {
                val adapterClass = it::class.java
                val methodSetDayText =
                    adapterClass.getDeclaredMethod("setDayTextAppearance", Int::class.java)
                methodSetDayText.isAccessible = true
                methodSetDayText.invoke(it, dayTextAppearanceResId)

                val methodSetDayOfWeekText =
                    adapterClass.getDeclaredMethod("setDayOfWeekTextAppearance", Int::class.java)
                methodSetDayOfWeekText.isAccessible = true
                methodSetDayOfWeekText.invoke(it, dayOfWeekTextAppearanceResId)

                val methodSetDaySelectorColor =
                    adapterClass.getDeclaredMethod("setDaySelectorColor",
                        ColorStateList::class.java)
                methodSetDaySelectorColor.isAccessible = true
                methodSetDaySelectorColor.invoke(it, daySelectorColor)

                replaceItemsWithObservable(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("CustomDatePickerView", "Failed to setTextStyle: $e")
        }
    }

    private fun replaceItemsWithObservable(adapter: Any) {
        val itemsField: Field = adapter.javaClass.getDeclaredField("mItems")
        itemsField.isAccessible = true

        observableSparseArray = ObservableSparseArray()
        itemsField.set(adapter, observableSparseArray)
    }

    @SuppressLint("PrivateApi")
    private fun setDaySelectedListener(dayPickerViewInstance: Any) {
        val dayPickerViewClass = Class.forName("android.widget.DayPickerView")
        val onDaySelectedListenerClass =
            Class.forName("android.widget.DayPickerView\$OnDaySelectedListener")
        val setOnDaySelectedListenerMethod =
            dayPickerViewClass.getDeclaredMethod("setOnDaySelectedListener",
                onDaySelectedListenerClass)
        setOnDaySelectedListenerMethod.isAccessible = true

        val listener = Proxy.newProxyInstance(
            onDaySelectedListenerClass.classLoader,
            arrayOf(onDaySelectedListenerClass)
        ) { _, method, args ->
            if (method.name == "onDaySelected") {
                val selectedDay = args[1] as Calendar
                Logger.d("CustomDatePickerView", "Selected day: ${selectedDay.time}")
                updateDate(selectedDay)
            }
            null
        }
        setOnDaySelectedListenerMethod.invoke(dayPickerViewInstance, listener)
    }

    private fun updateDate(selectedDay: Calendar) {
        Logger.d("updateDate ${selectedDay.time}")
        currentDate?.timeInMillis = selectedDay.timeInMillis
        val monthDay = currentDate?.time?.let { dateFormat?.format(it) }
        onDateSelected?.invoke(monthDay)
    }

}