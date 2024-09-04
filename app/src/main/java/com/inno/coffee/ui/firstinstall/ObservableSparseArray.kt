package com.inno.coffee.ui.firstinstall

import android.util.SparseArray

class ObservableSparseArray : SparseArray<Any?>() {

    override fun put(key: Int, value: Any?) {
        super.put(key, value)
        value?.let {
            val viewHolderClass = value.javaClass
            val calendarField = viewHolderClass.getDeclaredField("calendar")
            calendarField.isAccessible = true
            val simpleMonthView = calendarField.get(value)
            modifySimpleMonthView(simpleMonthView)
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