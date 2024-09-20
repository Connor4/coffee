package com.inno.coffee.function.display

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.view.Display


object ScreenDisplayManager {
    private const val TAG = "ScreenDisplayManager"
    private var secondDisplay: Display? = null

    fun init(context: Context) {
        context.getSystemService(DisplayManager::class.java).getDisplays(DisplayManager
            .DISPLAY_CATEGORY_PRESENTATION).firstOrNull()?.let {
            secondDisplay = it
        }
    }

    fun getSecondDisplay(): Display? = secondDisplay

    fun isMainDisplay(packageContext: Context): Boolean {
        return (packageContext as Activity).windowManager.defaultDisplay.displayId == Display.DEFAULT_DISPLAY
    }

    fun manualRoute(packageContext: Context, targetCls: Class<*>?, defaultScreen: Boolean =
        false, key: String = "", extra: String = "") {
        if (defaultScreen) {
            route(packageContext, targetCls, key, extra)
        } else {
            val displayId = secondDisplay?.displayId ?: Display.DEFAULT_DISPLAY
            route(packageContext, targetCls, displayId, key, extra)
        }
    }

    fun autoRoute(packageContext: Context, targetCls: Class<*>?, key: String = "",
        extra: String = "") {
        val display = (packageContext as Activity).windowManager.defaultDisplay
        if (display == secondDisplay) {
            route(packageContext, targetCls, display.displayId, key, extra)
        } else {
            route(packageContext, targetCls, key, extra)
        }
    }

    private fun route(packageContext: Context, targetCls: Class<*>?, displayId: Int, key: String,
        extra: String) {
        val options = ActivityOptions.makeBasic().apply {
            launchDisplayId = displayId
        }
        val intent = Intent(packageContext, targetCls)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (key.isNotEmpty() and extra.isNotEmpty()) {
            intent.putExtra(key, extra)
        }
        packageContext.startActivity(intent, options.toBundle())
    }

    private fun route(packageContext: Context, targetCls: Class<*>?, key: String, extra: String) {
        val intent = Intent(packageContext, targetCls)
        if (key.isNotEmpty() and extra.isNotEmpty()) {
            intent.putExtra(key, extra)
        }
        packageContext.startActivity(intent)
    }

}