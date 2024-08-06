package com.inno.coffee.function.presentation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.view.Display


object PresentationDisplayManager {
    private const val TAG = "PresentationDisplayManager"
    private var secondDisplay: Display? = null

    fun init(context: Context) {
        context.getSystemService(DisplayManager::class.java).getDisplays(DisplayManager
            .DISPLAY_CATEGORY_PRESENTATION).firstOrNull()?.let {
            secondDisplay = it
        }
    }

    fun getSecondDisplay(): Display? = secondDisplay

    fun isSecondDisplay(packageContext: Context): Boolean {
        return (packageContext as Activity).windowManager.defaultDisplay == secondDisplay
    }

    fun manualRoute(packageContext: Context, targetCls: Class<*>?, defaultScreen: Boolean = false) {
        if (defaultScreen) {
            route(packageContext, targetCls)
        } else {
            val displayId = secondDisplay?.displayId ?: Display.DEFAULT_DISPLAY
            route(packageContext, targetCls, displayId)
        }
    }

    fun autoRoute(packageContext: Context, targetCls: Class<*>?) {
        val display = (packageContext as Activity).windowManager.defaultDisplay
        if (display == secondDisplay) {
            route(packageContext, targetCls, display.displayId)
        } else {
            route(packageContext, targetCls)
        }
    }

    private fun route(packageContext: Context, targetCls: Class<*>?, displayId: Int) {
        val options = ActivityOptions.makeBasic().apply {
            launchDisplayId = displayId
        }
        val intent = Intent(packageContext, targetCls)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        packageContext.startActivity(intent, options.toBundle())
    }

    private fun route(packageContext: Context, targetCls: Class<*>?) {
        packageContext.startActivity(Intent(packageContext, targetCls))
    }

}