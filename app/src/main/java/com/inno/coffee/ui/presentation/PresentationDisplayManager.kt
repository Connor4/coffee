package com.inno.coffee.ui.presentation

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.WindowManager


object PresentationDisplayManager {
    private const val TAG = "PresentationDisplayManager"
    private var validDisplay: Display? = null

    fun init(context: Context) {
        context.getSystemService(DisplayManager::class.java).getDisplays(DisplayManager
            .DISPLAY_CATEGORY_PRESENTATION).firstOrNull()?.let {
            validDisplay = it
        }
    }

    fun getDisplay(): Display? = validDisplay

    fun manualRoute(packageContext: Context, targetCls: Class<*>?, main: Boolean = true) {
        if (main) {
            route(packageContext, targetCls)
        } else {
            val displayId = validDisplay?.displayId ?: Display.DEFAULT_DISPLAY
            route(packageContext, targetCls, displayId)
        }
    }

    fun autoRoute(packageContext: Context, targetCls: Class<*>?) {
        packageContext.getSystemService(WindowManager::class.java).defaultDisplay?.let {
            route(packageContext, targetCls, it.displayId)
        } ?: {
            route(packageContext, targetCls)
        }
    }

    private fun route(packageContext: Context, targetCls: Class<*>?, displayId: Int) {
        val options = ActivityOptions.makeBasic().apply {
            launchDisplayId = displayId
        }
        val intent = Intent(packageContext, targetCls)
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        packageContext.startActivity(intent, options.toBundle())
    }

    private fun route(packageContext: Context, targetCls: Class<*>?) {
        packageContext.startActivity(Intent(packageContext, targetCls))
    }

}