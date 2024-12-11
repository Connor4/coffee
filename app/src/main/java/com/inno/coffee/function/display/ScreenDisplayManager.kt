package com.inno.coffee.function.display

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.Display


object ScreenDisplayManager {
    private const val TAG = "ScreenDisplayManager"
    private var secondDisplay: Display? = null

    fun init(context: Context) {
        require(context != null) { "packageContext cannot be null" }

        context.getSystemService(DisplayManager::class.java).getDisplays(DisplayManager
            .DISPLAY_CATEGORY_PRESENTATION).firstOrNull()?.let {
            secondDisplay = it
        }
    }

    fun getSecondDisplay(): Display? = secondDisplay

    fun isMainDisplay(packageContext: Context): Boolean {
        require(packageContext != null) { "packageContext cannot be null" }

        return (packageContext as Activity).windowManager.defaultDisplay.displayId == Display.DEFAULT_DISPLAY
    }

    fun manualRoute(
        packageContext: Context, targetCls: Class<*>?, defaultScreen: Boolean = false,
        bundle: Bundle? = null,
    ) {
        require(packageContext != null) { "packageContext cannot be null" }

        if (defaultScreen) {
            route(packageContext, targetCls, bundle)
        } else {
            val displayId = secondDisplay?.displayId ?: Display.DEFAULT_DISPLAY
            route(packageContext, targetCls, displayId, bundle)
        }
    }

    fun autoRoute(packageContext: Context, targetCls: Class<*>?, bundle: Bundle? = null) {
        require(packageContext != null) { "packageContext cannot be null" }

        val display = (packageContext as Activity).windowManager.defaultDisplay
        if (display == secondDisplay) {
            route(packageContext, targetCls, display.displayId, bundle)
        } else {
            route(packageContext, targetCls, bundle)
        }
    }

    private fun route(
        packageContext: Context, targetCls: Class<*>?, displayId: Int,
        bundle: Bundle?,
    ) {
        val options = ActivityOptions.makeBasic().apply {
            launchDisplayId = displayId
        }
        val intent = Intent(packageContext, targetCls)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        bundle?.let {
            intent.putExtras(bundle)
        }
        packageContext.startActivity(intent, options.toBundle())
    }

    private fun route(packageContext: Context, targetCls: Class<*>?, bundle: Bundle?) {
        val intent = Intent(packageContext, targetCls)
        bundle?.let {
            intent.putExtras(bundle)
        }
        packageContext.startActivity(intent)
    }

}