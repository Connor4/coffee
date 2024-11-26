package com.inno.coffee.ui.firstinstall

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.icu.util.Calendar
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ListView
import androidx.core.view.updateLayoutParams
import com.inno.coffee.R
import com.inno.common.utils.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.text.SimpleDateFormat
import java.util.Locale

class CoffeeDatePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    var onDateSelected: ((String?, String?, Long) -> Unit)? = null,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val TAG = "CoffeeDatePickerView"
    private val DEFAULT_START_YEAR = 2024
    private val DEFAULT_END_YEAR = 2100
    private var prevButton: ImageButton? = null
    private var nextButton: ImageButton? = null
    private var yearPickerView: View? = null
    private var dayInstance: Any? = null
    private var yearInstance: Any? = null
    private var observableSparseArray: ObservableSparseArray? = null
    private var selectedDate: Calendar = Calendar.getInstance()
    private var displayDate: Calendar = Calendar.getInstance()
    private var minDate: Calendar = Calendar.getInstance()
    private var maxDate: Calendar = Calendar.getInstance()
    private var mDYDateFormat: SimpleDateFormat? = null
    private var mYDateFormat: SimpleDateFormat? = null

    init {
        val locale = Locale.getDefault()
        minDate.set(DEFAULT_START_YEAR, Calendar.JANUARY, 1)
        maxDate.set(DEFAULT_END_YEAR, Calendar.DECEMBER, 31)
        val dyFormat = DateFormat.getBestDateTimePattern(locale, "MMM d, yyyy")
        val myFormat = DateFormat.getBestDateTimePattern(locale, "MMM, yyyy")
        mDYDateFormat = SimpleDateFormat(dyFormat, locale)
        mYDateFormat = SimpleDateFormat(myFormat, locale)
        addDayPickerView(context, attrs, defStyleAttr)
        addYearPickerView(context, attrs)
        showYearPickerView(false)
    }

    fun performPrevClick() {
        prevButton?.performClick()
    }

    fun performNextClick() {
        nextButton?.performClick()
    }

    fun showYearPickerView(show: Boolean) {
        if (show) {
            yearPickerView?.visibility = View.VISIBLE
        } else {
            yearPickerView?.visibility = View.GONE
        }
    }

    fun updateDate() {
        val monthDayYear = selectedDate.time?.let { mDYDateFormat?.format(it) }
        val monthYear = displayDate.time?.let { mYDateFormat?.format(it) }
        onDateSelected?.invoke(monthDayYear, monthYear, selectedDate.timeInMillis)
    }

    @SuppressLint("PrivateApi")
    private fun addYearPickerView(context: Context, attrs: AttributeSet?) {
        try {
            val yearPickerViewClass = Class.forName("android.widget.YearPickerView")
            val constructor: Constructor<*> =
                yearPickerViewClass.getDeclaredConstructor(Context::class.java,
                    AttributeSet::class.java)
            constructor.isAccessible = true
            yearInstance = constructor.newInstance(context, attrs)
            val yearPickerViewInstance = yearInstance
            if (yearPickerViewInstance is ListView) {
                yearPickerView = yearPickerViewInstance
                addView(yearPickerViewInstance)
                setYearPickerMethodParams(yearPickerViewClass, yearPickerViewInstance)
                setYearPickerSize()
                setYearSelectedListener(yearPickerViewClass, yearPickerViewInstance)
            } else {
                Logger.e(TAG, "Failed to cast to ListView")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(TAG, "addYearPickerView Exception: $e")
        }
    }

    private fun setYearPickerMethodParams(
        yearPickerViewClass: Class<*>,
        yearPickerViewInstance: Any,
    ) {
        try {
            val methodSetRange =
                yearPickerViewClass.getDeclaredMethod("setRange", Calendar::class.java,
                    Calendar::class.java)
            methodSetRange.isAccessible = true
            methodSetRange.invoke(yearPickerViewInstance, minDate, maxDate)

            val methodSetYear =
                yearPickerViewClass.getDeclaredMethod("setYear", Int::class.java)
            methodSetYear.isAccessible = true
            methodSetYear.invoke(yearPickerViewInstance, selectedDate.get(Calendar.YEAR))
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(TAG, "setYearPickerMethodParams $e")
        }
    }
    @SuppressLint("PrivateApi")
    private fun updateYearPickerViewYear(year: Int) {
        try {
            val yearPickerViewClass = Class.forName("android.widget.YearPickerView")
            val methodSetYear = yearPickerViewClass.getDeclaredMethod("setYear", Int::class.java)
            methodSetYear.invoke(yearInstance, year)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setYearPickerSize() {
        yearPickerView?.setBackgroundColor(resources.getColor(R.color.white, null))
        yearPickerView?.updateLayoutParams {
            width = 400
            height = 300
        }
    }

    @SuppressLint("PrivateApi")
    private fun setYearSelectedListener(
        yearPickerViewClass: Class<*>,
        yearPickerViewInstance: Any,
    ) {
        try {
            val onYearSelectedListenerClass =
                Class.forName("android.widget.YearPickerView\$OnYearSelectedListener")
            val setOnYearSelectedListenerMethod =
                yearPickerViewClass.getDeclaredMethod("setOnYearSelectedListener",
                    onYearSelectedListenerClass)
            setOnYearSelectedListenerMethod.isAccessible = true

            val listener = Proxy.newProxyInstance(
                onYearSelectedListenerClass.classLoader,
                arrayOf(onYearSelectedListenerClass)
            ) { _, method, args ->
                if (method.name == "onYearChanged") {
                    val selectedYear = args[1] as Int
                    Logger.d(TAG, "selected year: $selectedYear")
                    val day = selectedDate.get(Calendar.DAY_OF_MONTH)
                    val month = selectedDate.get(Calendar.MONTH)
                    val daysInMonth = getDaysInMonth(month, day)
                    if (day > daysInMonth) {
                        selectedDate.set(Calendar.DAY_OF_MONTH, daysInMonth)
                    }
                    selectedDate.set(Calendar.YEAR, selectedYear)
                    if (selectedDate < minDate) {
                        selectedDate.timeInMillis = minDate.timeInMillis
                    } else if (selectedDate > maxDate) {
                        selectedDate.timeInMillis = maxDate.timeInMillis
                    }
                    displayDate.timeInMillis = selectedDate.timeInMillis

                    updateDayPickerViewDate()
                    updateDate()
                }
                null
            }
            setOnYearSelectedListenerMethod.invoke(yearPickerViewInstance, listener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("PrivateApi")
    private fun addDayPickerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        try {
            val dayPickerViewClass = Class.forName("android.widget.DayPickerView")
            val constructor: Constructor<*> =
                dayPickerViewClass.getDeclaredConstructor(Context::class.java)
            constructor.isAccessible = true
            dayInstance = constructor.newInstance(context)
            val dayPickerViewInstance = dayInstance
            if (dayPickerViewInstance is ViewGroup) {
                addView(dayPickerViewInstance)
                disableButton(dayPickerViewInstance)
                setTextStyle(dayPickerViewInstance, context, attrs, defStyleAttr)
                setDayPickerMethodParams(dayPickerViewClass, dayPickerViewInstance)
                setDaySelectedListener(dayPickerViewClass, dayPickerViewInstance)
                setPageChangeListener(dayPickerViewInstance)
            } else {
                Logger.e(TAG, "Failed to cast to ViewGroup")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(TAG, "addDayPickerView Exception: $e")
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
            Logger.e(TAG, "Failed to disable: $e")
        }
    }

    private fun setTextStyle(
        dayPickerViewInstance: Any, context: Context, attrs: AttributeSet?,
        defStyleAttr: Int,
    ) {
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
            Logger.e(TAG, "Failed to setTextStyle: $e")
        }
    }

    private fun replaceItemsWithObservable(adapter: Any) {
        val itemsField: Field = adapter.javaClass.getDeclaredField("mItems")
        itemsField.isAccessible = true

        observableSparseArray = ObservableSparseArray()
        itemsField.set(adapter, observableSparseArray)
    }

    @SuppressLint("PrivateApi")
    private fun setDayPickerMethodParams(dayPickerViewClass: Class<*>, dayPickerViewInstance: Any) {
        try {
            val methodSetFirstDayOfWeek =
                dayPickerViewClass.getDeclaredMethod("setFirstDayOfWeek", Int::class.java)
            methodSetFirstDayOfWeek.isAccessible = true
            methodSetFirstDayOfWeek.invoke(dayPickerViewInstance, 0)

            val methodSetMinDate =
                dayPickerViewClass.getDeclaredMethod("setMinDate", Long::class.java)
            methodSetMinDate.isAccessible = true
            methodSetMinDate.invoke(dayPickerViewInstance, minDate.timeInMillis)

            val methodSetMaxDate =
                dayPickerViewClass.getDeclaredMethod("setMaxDate", Long::class.java)
            methodSetMaxDate.isAccessible = true
            methodSetMaxDate.invoke(dayPickerViewInstance, maxDate.timeInMillis)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(TAG, "setViewParams: $e")
        }
    }

    @SuppressLint("PrivateApi")
    private fun updateDayPickerViewDate() {
        try {
            val dayPickerViewClass = Class.forName("android.widget.DayPickerView")
            val methodSetDate = dayPickerViewClass.getDeclaredMethod("setDate", Long::class.java)
            methodSetDate.invoke(dayInstance, selectedDate.timeInMillis)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("PrivateApi")
    private fun setDaySelectedListener(dayPickerViewClass: Class<*>, dayPickerViewInstance: Any) {
        try {
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
                    Logger.d(TAG, "Selected day: ${selectedDay.time}")
                    selectedDate.timeInMillis = selectedDay.timeInMillis
                    displayDate.timeInMillis = selectedDate.timeInMillis
                    val year = selectedDate.get(Calendar.YEAR)
                    updateYearPickerViewYear(year)
                    updateDate()
                }
                null
            }
            setOnDaySelectedListenerMethod.invoke(dayPickerViewInstance, listener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setPageChangeListener(dayPickerViewInstance: Any) {
        try {
            val listenerField: Field =
                dayPickerViewInstance.javaClass.getDeclaredField("mOnPageChangedListener")
            listenerField.isAccessible = true
            val originalListener = listenerField.get(dayPickerViewInstance)

            val proxyListener = originalListener?.javaClass?.let {
                Proxy.newProxyInstance(
                    it.classLoader, it.interfaces
                ) { _, method: Method, args: Array<out Any>? ->
                    if (method.name == "onPageSelected" && args != null) {
                        val position = args[0] as Int
                        val monthForPosition = getMonthForPosition(position)
                        val yearForPosition = getYearForPosition(position)
                        Logger.d(TAG,
                            "onPageSelected month $monthForPosition year $yearForPosition")
                        displayDate.set(yearForPosition, monthForPosition, 1)
                        updateYearPickerViewYear(yearForPosition)
                        updateDate()
                    }
                    method.invoke(originalListener, *(args ?: emptyArray()))
                }
            }

            val viewPagerField = dayPickerViewInstance.javaClass.getDeclaredField("mViewPager")
            viewPagerField.isAccessible = true
            val viewPagerInstance = viewPagerField.get(dayPickerViewInstance)
            val viewPagerClass = viewPagerInstance.javaClass.superclass
            val setListenerMethod = viewPagerClass?.getDeclaredMethod(
                "setOnPageChangeListener",
                Class.forName("com.android.internal.widget.ViewPager\$OnPageChangeListener")
            )
            setListenerMethod?.isAccessible = true

            setListenerMethod?.invoke(viewPagerInstance, proxyListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if ((year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) 29 else 28
            else -> throw IllegalArgumentException("Invalid Month")
        }
    }

    private fun getMonthForPosition(position: Int): Int {
        val month = (position + minDate.get(Calendar.MONTH)) % 12
        return month.coerceIn(Calendar.JANUARY, Calendar.DECEMBER)
    }

    private fun getYearForPosition(position: Int): Int {
        val yearOffset: Int = (position + minDate.get(Calendar.MONTH)) / 12
        return yearOffset + minDate.get(Calendar.YEAR)
    }

}