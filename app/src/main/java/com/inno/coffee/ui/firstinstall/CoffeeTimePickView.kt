package com.inno.coffee.ui.firstinstall

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.inno.common.utils.Logger
import java.lang.reflect.Constructor

class CoffeeTimePickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    var onDateSelected: ((String?, String?, Long) -> Unit)? = null,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val tag = "CoffeeTimePickView"
    private val am = 0
    private val fm = 1

    init {
        addTimePickerView(context)
    }

    @SuppressLint("PrivateApi")
    private fun addTimePickerView(context: Context) {
        try {
            val timePickerViewClass = Class.forName("android.widget.RadialTimePickerView")
            val constructor: Constructor<*> =
                timePickerViewClass.getDeclaredConstructor(Context::class.java)
            constructor.isAccessible = true
            val timePickerViewInstance = constructor.newInstance(context)
            if (timePickerViewInstance is View) {
                addView(timePickerViewInstance)
            } else {
                Logger.e(tag, "Failed to cast to View")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}