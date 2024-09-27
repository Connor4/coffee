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
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.inno.coffee.R
import com.inno.coffee.data.DialogData
import com.inno.coffee.ui.common.IndicatorView
import com.inno.common.utils.Logger
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class GlobalDialogLeftManager private constructor(private val application: Application) {

    private val windowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var dialogView: View? = null
    private var dialogShowing = false
    private val dialogDataList = mutableListOf<DialogData>()
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
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }
    private val pageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float,
            positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            indicatorView?.setIndicatorMove(position, positionOffset)
        }
    }
    private var selfCleanJob: Job? = null
    private val selfCleanWaitTime = 5_000L
    private val _warningExist = MutableStateFlow(false)
    val warningExist = _warningExist.asStateFlow()

    init {
        DataCenter.subscribe(ReceivedDataType.SERIAL_PORT_ERROR, subscriber)
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT_LIST, subscriber)
    }

    private fun parseReceivedData(data: Any) {
        // SerialErrorData发生，一定不会有HeatBeatList；出现HeatBeatList，一定已经解决SerialErrorData
        when (val receivedData = data as ReceivedData) {
            is ReceivedData.SerialErrorData -> {
                if (validDialogData(receivedData.code)) {
                    val dialogData = DialogData().apply {
                        errorCode = receivedData.code
                        val detail = application.resources.getString(
                            serialErrorMap[errorCode] ?: R.string.error_no_resource)
                        message = "E $errorCode: $detail"
                    }
                    dialogDataList.clear()
                    dialogDataList.add(dialogData)
                    updateDialog()
                } else {
                    activeSelfClean()
                }
            }
            is ReceivedData.HeatBeatList -> {
                receivedData.list.let { list ->
                    val tempList = mutableListOf<DialogData>()
                    list.forEach { heartbeat ->
                        heartbeat.error?.let { error ->
                            if (validDialogData(error.status.value)) {
                                val dialogData = DialogData().apply {
                                    errorCode = error.status.value
                                    val detail = application.resources.getString(
                                        machineErrorMap[errorCode] ?: R.string.error_no_resource)
                                    message = "E $errorCode: $detail"
                                }
                                tempList.add(dialogData)
                            }
                        }
                    }
                    dialogDataList.clear()
                    dialogDataList.addAll(tempList)
                    if (tempList.isNotEmpty()) {
                        updateDialog()
                    } else {
                        dismissDialog()
                        dialogDataList.clear()
                        _warningExist.value = false
                    }
                }
            }
            else -> {}
        }
    }

    private fun validDialogData(code: Int): Boolean {
        return dialogDataList.none { it.errorCode == code }
    }

    private fun activeSelfClean() {
        Logger.d(TAG, "activeSelfClean() called")
        selfCleanJob?.cancel()
        selfCleanJob = scope.launch {
            val result = withTimeoutOrNull(selfCleanWaitTime) {
                delay(selfCleanWaitTime + 1)
            }
            if (result == null) {
                Logger.d(TAG, "activeSelfClean")
                dismissDialog()
                dialogDataList.clear()
                _warningExist.value = false
            }
        }
    }

    private fun updateDialog() {
        Logger.d(TAG, "updateDialog() called")
        scope.launch {
            _warningExist.value = true
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
            windowManager.removeView(it)
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
                LayoutInflater.from(application).inflate(R.layout.global_dialog_layout, null)
            dialogView?.let {
                viewPager2 = it.findViewById<ViewPager2>(R.id.dialog_error_vp).apply {
                    adapter = errorAdapter
                    registerOnPageChangeCallback(pageChangeCallback)
                }
                indicatorView = it.findViewById(R.id.dialog_indicator)
                it.findViewById<ImageView>(R.id.global_warning_close_iv).setOnClickListener {
                    dismissDialog()
                }
            }
        }
        windowManager.addView(dialogView, windowLayoutParams)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GlobalDialogLeftManager? = null
        private var application: Application? = null
        private const val TAG = "GlobalDialogLeftManager"
        private val serialErrorMap = mapOf(
            -1 to R.string.error_serial_read_fail,
            -2 to R.string.error_max_read_retry,
            -3 to R.string.error_max_open_retry,
            -4 to R.string.error_serial_io_exception,
            -5 to R.string.error_serial_format_invalid,
            -6 to R.string.error_crc_check_failed,
            -7 to R.string.error_read_no_data,
            -8 to R.string.error_heart_beat_miss,
            -9 to R.string.error_frame_size_error,
            -10 to R.string.error_serial_no_reply,
            -11 to R.string.error_command_no_reply,
        )
        private val machineErrorMap = mapOf(
            2000 to R.string.error_front_bean_container_empty,
            2001 to R.string.error_back_bean_container_empty,
            2002 to R.string.error_bean_container_empty,
            3000 to R.string.error_left_grinder_error,
            3001 to R.string.error_right_grinder_error,
            3002 to R.string.error_left_boiler_error,
            3003 to R.string.error_right_boiler_error,
            3004 to R.string.error_left_brewer_error,
            3005 to R.string.error_right_brewer_error,
            3006 to R.string.error_steam_boiler_error,
            4000 to R.string.error_no_water,
        )

        fun init(context: Application) {
            application = context
            getInstance()
        }

        fun getInstance(): GlobalDialogLeftManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GlobalDialogLeftManager(application!!).also { INSTANCE = it }
            }
        }
    }

}