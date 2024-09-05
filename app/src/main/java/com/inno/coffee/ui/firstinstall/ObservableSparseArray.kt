package com.inno.coffee.ui.firstinstall

import android.graphics.Color
import android.text.TextPaint
import android.util.SparseArray
import java.lang.reflect.Field

class ObservableSparseArray : SparseArray<Any?>() {

    override fun put(key: Int, value: Any?) {
        super.put(key, value)
        value?.let {
            val viewHolderClass = value.javaClass
            val calendarField = viewHolderClass.getDeclaredField("calendar")
            calendarField.isAccessible = true
            val simpleMonthView = calendarField.get(value)
            modifySimpleMonthView(simpleMonthView)

            val paintField: Field = simpleMonthView.javaClass.getDeclaredField("mMonthPaint")
            paintField.isAccessible = true
            val monthPaint = paintField.get(simpleMonthView) as? TextPaint
            monthPaint?.setColor(Color.parseColor("#ff191a1d"))
            paintField.set(simpleMonthView, monthPaint)
        }
    }

    private fun modifySimpleMonthView(simpleMonthView: Any?) {
        simpleMonthView?.let {
            val dayOfWeekHeight = it.javaClass.getDeclaredField("mDesiredDayOfWeekHeight")
            dayOfWeekHeight.isAccessible = true
            dayOfWeekHeight.set(it, 60)

            val dayHeight = it.javaClass.getDeclaredField("mDesiredDayHeight")
            dayHeight.isAccessible = true
            dayHeight.set(it, 60)

            val cellWidth = it.javaClass.getDeclaredField("mDesiredCellWidth")
            cellWidth.isAccessible = true
            cellWidth.set(it, 25)

            val daySelectorRadius = it.javaClass.getDeclaredField("mDesiredDaySelectorRadius")
            daySelectorRadius.isAccessible = true
            daySelectorRadius.set(it, 25)
        }
    }

}