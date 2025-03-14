package com.inno.coffee.ui.notice

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.inno.coffee.R
import com.inno.coffee.data.DialogData
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.IndicatorView
import com.inno.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GlobalDialogRightManager private constructor(private val application: Application) {

    private var windowManager: WindowManager? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var dialogView: View? = null
    private var dialogShowing = false
    private val dialogDataList: MutableList<DialogData> = emptyList<DialogData>().toMutableList()
    private val errorAdapter = ErrorViewPagerAdapter(dialogDataList)
    private var viewPager2: ViewPager2? = null
    private var indicatorView: IndicatorView? = null
    private var windowLayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams
            .FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT
    )
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float,
            positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            indicatorView?.setIndicatorMove(position, positionOffset)
        }
    }
    private var windowContext: Context? = null

    init {
        val display = ScreenDisplayManager.getSecondDisplay()
        display?.let {
            windowContext =
                application.createDisplayContext(it)
                    .createWindowContext(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, null)
            windowManager = windowContext?.getSystemService(WindowManager::class.java)
        }
        scope.launch {
            ErrorDataManager.dialogDataList.collect { dataList ->
                if (dataList.isNotEmpty()) {
                    dialogDataList.clear()
                    dialogDataList.addAll(dataList)
                    updateDialog()
                } else {
                    dismissDialog()
                }
            }
        }
        scope.launch {
            ErrorDataManager.userDismissed.collect { dismissed ->
                if (dismissed) {
                    dismissDialog()
                }
            }
        }

    }

    private fun updateDialog() {
        Logger.d(TAG, "updateDialog() called")
        scope.launch {
            if (dialogShowing) {
                updateDialogContent()
            } else {
                showDialog()
            }
            indicatorView?.setIndicatorCount(dialogDataList.size)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDialogContent() {
        errorAdapter.notifyDataSetChanged()
    }

    fun showDialog() {
        if (dialogShowing || dialogDataList.isEmpty()) {
            return
        }
        Logger.d(TAG, "showDialog() called")
        dialogShowing = true
        showDialogView()
    }

    private fun dismissDialog() {
        Logger.d(TAG, "dismissDialog() called")
        dialogView?.let {
            windowManager?.removeView(it)
            viewPager2?.adapter = null
            viewPager2?.unregisterOnPageChangeCallback(pageChangeCallback)
            dialogView = null
            indicatorView = null
            viewPager2 = null
        }
        dialogShowing = false
    }

    private fun showDialogView() {
        if (dialogView == null) {
            dialogView =
                LayoutInflater.from(windowContext).inflate(R.layout.global_dialog_layout, null)
            dialogView?.let {
                viewPager2 = it.findViewById<ViewPager2>(R.id.dialog_error_vp).apply {
                    adapter = errorAdapter
                    registerOnPageChangeCallback(pageChangeCallback)
                }
                indicatorView = it.findViewById(R.id.dialog_indicator)
                it.findViewById<ImageView>(R.id.global_warning_close_iv).setOnClickListener {
                    ErrorDataManager.setUserDismissed(true)
                }
            }
        }
        windowManager?.addView(dialogView, windowLayoutParams)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GlobalDialogRightManager? = null
        private var application: Application? = null
        private const val TAG = "GlobalDialogRightManager"

        fun init(context: Application) {
            application = context
            getInstance()
        }

        fun getInstance(): GlobalDialogRightManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GlobalDialogRightManager(application!!).also { INSTANCE = it }
            }
        }
    }

}