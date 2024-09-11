package com.inno.coffee.ui.notice

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inno.coffee.R
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GlobalDialogManager private constructor(private val application: Application) {

    private val windowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val scope = CoroutineScope(Dispatchers.Main)
    private var dialogView: View? = null
    private var dialogShowing = false
    private val dialogDataList = mutableListOf<DialogData>()
    private val errorAdapter = ErrorViewPagerAdapter(dialogDataList)
    private var recyclerView: RecyclerView? = null
    private var updateDialogFlag = false
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
                        message =
                            "ErrorData: ${receivedData.info}, need reboot ${receivedData.reboot}"
                    }
                    dialogDataList.clear()
                    dialogDataList.add(dialogData)
                    updateDialogFlag = true
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
                                    message = "${error.value}"
                                }
                                tempList.add(dialogData)
                            }
                        }
                    }
                    if (tempList.isNotEmpty()) {
                        dialogDataList.clear()
                        dialogDataList.addAll(tempList)
                        updateDialogFlag = true
                    }
                }
            }
            else -> {}
        }
        scope.launch {
            if (updateDialogFlag) {
                updateDialogFlag = false
                _warningExist.value = dialogDataList.isNotEmpty()
                if (dialogShowing) {
                    updateDialogContent()
                } else {
                    showDialog()
                }
            }
        }
    }

    private fun validDialogData(code: Int): Boolean {
        return dialogDataList.none { it.errorCode == code }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDialogContent() {
        errorAdapter.notifyDataSetChanged()
    }

    fun showDialog() {
        if (dialogShowing || dialogDataList.isEmpty()) {
            return
        }
        dialogShowing = true
        showDialogView()
    }

    private fun dismissDialog() {
        dialogView?.let {
            windowManager.removeView(it)
            recyclerView?.adapter = null
            dialogView = null
            recyclerView = null
        }
        dialogShowing = false
    }

    private fun showDialogView() {
        if (dialogView == null) {
            dialogView =
                LayoutInflater.from(application).inflate(R.layout.global_dialog_layout, null)
            dialogView?.let {
                recyclerView = it.findViewById<RecyclerView>(R.id.dialog_error_rv).apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                        false)
                    adapter = errorAdapter
                }
//                it.findViewById<Button>(R.id.dialog_next_bt).setOnClickListener {
//                    recyclerView?.smoothScrollToPosition(1)
//                }
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
        private var INSTANCE: GlobalDialogManager? = null
        private var application: Application? = null
        private const val TAG = "GlobalDialogManager"

        fun init(context: Application) {
            application = context
            getInstance()
        }

        fun getInstance(): GlobalDialogManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GlobalDialogManager(application!!).also { INSTANCE = it }
            }
        }
    }

}

data class DialogData(
    var errorCode: Int = 0,
    var message: String = "",
)