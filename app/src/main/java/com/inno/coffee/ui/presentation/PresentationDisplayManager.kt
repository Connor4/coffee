package com.inno.coffee.ui.presentation

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import com.inno.common.utils.Logger


object PresentationDisplayManager {
    private const val TAG = "PresentationDisplayManager"
    private var validDisplay: Display? = null

    fun init(context: Context) {
        val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays: Array<Display> = displayManager.getDisplays(DisplayManager
            .DISPLAY_CATEGORY_PRESENTATION)
        if (displays.isNotEmpty()) {
            validDisplay = displays[0]
        } else {
            Logger.d(TAG, "displays is empty")
        }
    }

    fun getDisplay(): Display? = validDisplay


}